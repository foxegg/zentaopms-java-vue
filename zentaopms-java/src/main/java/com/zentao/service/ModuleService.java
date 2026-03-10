package com.zentao.service;

import com.zentao.entity.Module;
import com.zentao.repository.BugRepository;
import com.zentao.repository.ModuleRepository;
import com.zentao.repository.StoryRepository;
import com.zentao.repository.TaskRepository;
import com.zentao.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ActionService actionService;
    private final StoryRepository storyRepository;
    private final TaskRepository taskRepository;
    private final BugRepository bugRepository;
    private final TestCaseRepository testCaseRepository;

    public Optional<Module> getById(int id) {
        return moduleRepository.findById(id).filter(m -> m.getDeleted() == 0);
    }

    /** 与 PHP tree 一致；rootID≤0 时返回空列表 */
    public List<Module> getByRoot(int rootId, String type) {
        if (rootId <= 0) return List.of();
        if (type != null && !type.isEmpty()) {
            return moduleRepository.findByRootAndTypeAndDeletedOrderByOrderNumAsc(rootId, type, 0);
        }
        return moduleRepository.findByRootAndDeletedOrderByOrderNumAsc(rootId, 0);
    }

    /** 模块 id->层级名称（如 /父/子）供下拉用，与 PHP tree getOptionMenu(rootID, type) 一致；rootID≤0 时仅返回 0->"/" */
    public Map<Integer, String> getOptionMenu(int rootId, String type) {
        if (rootId <= 0) return Map.of(0, "/");
        List<Module> list;
        if (type != null && ("bug".equals(type) || "case".equals(type))) {
            list = moduleRepository.findByRootAndDeletedOrderByOrderNumAsc(rootId, 0);
            list = list.stream().filter(m -> m.getType() != null && ("story".equals(m.getType()) || type.equals(m.getType()))).toList();
        } else {
            list = getByRoot(rootId, type != null ? type : "story");
        }
        Map<Integer, Module> byId = list.stream().collect(Collectors.toMap(Module::getId, m -> m, (a, b) -> a));
        Map<Integer, String> result = new LinkedHashMap<>();
        result.put(0, "/");
        for (Module m : list) {
            String pathStr = m.getPath() != null ? m.getPath() : ",";
            String[] ids = pathStr.split(",");
            StringBuilder sb = new StringBuilder();
            for (String s : ids) {
                if (s.isEmpty()) continue;
                try {
                    int id = Integer.parseInt(s.trim());
                    Module node = byId.get(id);
                    sb.append("/").append(node != null && node.getName() != null ? node.getName() : id);
                } catch (NumberFormatException ignored) {}
            }
            result.put(m.getId(), sb.length() > 0 ? sb.toString() : ("/" + (m.getName() != null ? m.getName() : m.getId())));
        }
        return result;
    }

    public Module create(Module module) {
        module.setDeleted(0);
        return moduleRepository.save(module);
    }

    public Module update(Module module) {
        return moduleRepository.save(module);
    }

    /** 获取模块自身及其所有子模块 ID，与 PHP tree getAllChildId 一致 */
    public List<Integer> getAllChildId(int moduleId) {
        Module module = getById(moduleId).orElse(null);
        if (module == null) return List.of();
        List<Module> all = moduleRepository.findByRootAndDeletedOrderByOrderNumAsc(module.getRoot(), 0);
        Set<Integer> ids = new HashSet<>();
        ids.add(moduleId);
        for (int round = 0; round < 200; round++) {
            Set<Integer> next = new HashSet<>();
            for (Module m : all) {
                if (m.getParent() != null && ids.contains(m.getParent())) next.add(m.getId());
            }
            if (next.isEmpty()) break;
            ids.addAll(next);
        }
        return new ArrayList<>(ids);
    }

    /** 删除模块（软删除），与 PHP tree remove 一致：级联软删除自身及子模块、将被删模块下的 story/task/bug/case 归属到父模块、记录 action、并修复 path */
    @org.springframework.transaction.annotation.Transactional
    public void delete(int moduleId) {
        Module module = getById(moduleId).orElse(null);
        if (module == null) return;
        List<Integer> childs;
        if ("doc".equals(module.getType()) || "api".equals(module.getType())) {
            childs = List.of(moduleId);
        } else {
            childs = getAllChildId(moduleId);
        }
        String objectType = "module";
        for (Integer id : childs) {
            moduleRepository.findById(id).ifPresent(m -> {
                m.setDeleted(1);
                moduleRepository.save(m);
            });
            if (!"deliverable".equals(module.getType())) {
                actionService.create(objectType, id, "deleted", "", "1");
            }
        }
        int parentId = module.getParent() != null ? module.getParent() : 0;
        String type = module.getType() != null ? module.getType() : "story";
        if (!childs.isEmpty()) {
            switch (type) {
                case "task" -> taskRepository.findByModuleInAndDeleted(childs, 0).forEach(t -> { t.setModule(parentId); taskRepository.save(t); });
                case "bug" -> bugRepository.findByModuleInAndDeleted(childs, 0).forEach(b -> { b.setModule(parentId); bugRepository.save(b); });
                case "case" -> testCaseRepository.findByModuleInAndDeleted(childs, 0).forEach(c -> { c.setModule(parentId); testCaseRepository.save(c); });
                case "story" -> {
                    storyRepository.findByModuleInAndDeleted(childs, 0).forEach(s -> { s.setModule(parentId); storyRepository.save(s); });
                    taskRepository.findByModuleInAndDeleted(childs, 0).forEach(t -> { t.setModule(parentId); taskRepository.save(t); });
                    bugRepository.findByModuleInAndDeleted(childs, 0).forEach(b -> { b.setModule(parentId); bugRepository.save(b); });
                    testCaseRepository.findByModuleInAndDeleted(childs, 0).forEach(c -> { c.setModule(parentId); testCaseRepository.save(c); });
                }
                default -> { }
            }
        }
        fix(module.getRoot(), type);
    }

    @org.springframework.transaction.annotation.Transactional
    public void updateOrder(java.util.List<java.util.Map<String, Object>> orders) {
        if (orders == null) return;
        for (java.util.Map<String, Object> o : orders) {
            Object idObj = o.get("id");
            Object orderObj = o.get("order");
            if (idObj instanceof Number idNum && orderObj instanceof Number orderNum) {
                moduleRepository.findById(idNum.intValue()).ifPresent(m -> {
                    m.setOrderNum(orderNum.intValue());
                    moduleRepository.save(m);
                });
            }
        }
    }

    /** 修复模块 path/grade，与 PHP tree fix -> fixModulePath 一致 */
    @org.springframework.transaction.annotation.Transactional
    public void fix(int rootId, String type) {
        Set<String> types = new HashSet<>();
        if ("bug".equals(type) || "case".equals(type)) {
            types.add("story");
            types.add(type);
        } else if (type != null && !type.isEmpty()) {
            types.add(type);
        }
        if (types.isEmpty()) return;
        List<Module> all = moduleRepository.findByRootAndDeletedOrderByOrderNumAsc(rootId, 0);
        List<Module> modules = all.stream().filter(m -> m.getType() != null && types.contains(m.getType())).toList();
        Map<Integer, List<Module>> byParent = modules.stream().collect(Collectors.groupingBy(m -> m.getParent() != null ? m.getParent() : 0));
        Map<Integer, Module> done = new HashMap<>();
        List<Module> toSave = new ArrayList<>();
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(0);
        while (!queue.isEmpty()) {
            int parentId = queue.poll();
            String parentPath = parentId == 0 ? "," : (done.containsKey(parentId) ? done.get(parentId).getPath() : ",");
            int parentGrade = parentId == 0 ? 0 : (done.containsKey(parentId) ? (done.get(parentId).getGrade() != null ? done.get(parentId).getGrade() : 0) : 0);
            for (Module m : byParent.getOrDefault(parentId, List.of())) {
                m.setPath(parentPath + m.getId() + ",");
                m.setGrade(parentGrade + 1);
                done.put(m.getId(), m);
                toSave.add(m);
                queue.add(m.getId());
            }
        }
        for (Module m : toSave) moduleRepository.save(m);
    }
}
