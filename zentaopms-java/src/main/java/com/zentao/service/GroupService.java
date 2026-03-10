package com.zentao.service;

import com.zentao.entity.Group;
import com.zentao.entity.GroupPriv;
import com.zentao.entity.UserGroup;
import com.zentao.repository.GroupPrivRepository;
import com.zentao.repository.GroupRepository;
import com.zentao.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 权限组服务 - 对应 module/group
 */
@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupPrivRepository groupPrivRepository;

    /** 与 PHP group getPairs(projectID) 一致：按 project 过滤，projectID=0 表示 project 字段为 0 的全局组 */
    public Map<Integer, String> getPairs(int projectId) {
        return getPairs(projectId, null);
    }

    /** 与 PHP group getPairs 一致：vision 非空时按 project+vision 过滤（多版本场景） */
    public Map<Integer, String> getPairs(int projectId, String vision) {
        List<Group> list = (vision != null && !vision.isEmpty())
                ? groupRepository.findByProjectAndVision(projectId, vision)
                : groupRepository.findByProject(projectId);
        return list.stream().collect(Collectors.toMap(Group::getId, g -> g.getName() != null ? g.getName() : "", (a, b) -> a));
    }

    /** 与 PHP group getList(projectID) 一致：按 project 过滤的组列表 */
    public List<Group> getList(int projectId) {
        return getList(projectId, null);
    }

    /** 与 PHP group getList 一致：vision 非空时按 project+vision 过滤 */
    public List<Group> getList(int projectId, String vision) {
        return (vision != null && !vision.isEmpty())
                ? groupRepository.findByProjectAndVision(projectId, vision)
                : groupRepository.findByProject(projectId);
    }

    public List<UserGroup> getMembers(int groupId) {
        return userGroupRepository.findByGroupId(groupId);
    }

    @Transactional
    public void addMember(int groupId, String account, String project) {
        if (account == null || account.isEmpty()) return;
        if (userGroupRepository.existsByGroupIdAndAccount(groupId, account)) return;
        UserGroup ug = new UserGroup();
        ug.setGroupId(groupId);
        ug.setAccount(account);
        ug.setProject(project != null ? project : "");
        userGroupRepository.save(ug);
    }

    @Transactional
    public void removeMember(int groupId, String account) {
        userGroupRepository.deleteByGroupIdAndAccount(groupId, account);
    }

    @Transactional
    public Optional<Group> copyGroup(int sourceGroupId, Map<String, Object> options) {
        Group source = groupRepository.findById(sourceGroupId).orElse(null);
        if (source == null) return Optional.empty();
        Group target = new Group();
        target.setProject(source.getProject());
        target.setName(options.get("name") != null ? options.get("name").toString() : source.getName() + "_copy");
        target.setRole(source.getRole());
        target.setDescription(source.getDescription());
        target.setAcl(source.getAcl());
        target.setDeveloper(source.getDeveloper());
        target.setVision(source.getVision());
        Group saved = groupRepository.save(target);
        if (Boolean.TRUE.equals(options.get("copyPriv"))) {
            List<GroupPriv> privs = groupPrivRepository.findByGroupId(sourceGroupId);
            for (GroupPriv p : privs) {
                GroupPriv np = new GroupPriv();
                np.setGroupId(saved.getId());
                np.setModule(p.getModule());
                np.setMethod(p.getMethod());
                groupPrivRepository.save(np);
            }
        }
        if (Boolean.TRUE.equals(options.get("copyUser"))) {
            for (UserGroup ug : userGroupRepository.findByGroupId(sourceGroupId)) {
                UserGroup nug = new UserGroup();
                nug.setGroupId(saved.getId());
                nug.setAccount(ug.getAccount());
                nug.setProject(ug.getProject());
                userGroupRepository.save(nug);
            }
        }
        return Optional.of(saved);
    }

    public List<GroupPriv> getPrivs(int groupId) {
        return groupPrivRepository.findByGroupId(groupId);
    }

    @Transactional
    public void setPrivs(int groupId, List<Map<String, String>> privs) {
        groupPrivRepository.deleteByGroupId(groupId);
        if (privs != null) {
            for (Map<String, String> p : privs) {
                String module = p.get("module");
                String method = p.get("method");
                if (module != null) {
                    GroupPriv gp = new GroupPriv();
                    gp.setGroupId(groupId);
                    gp.setModule(module);
                    gp.setMethod(method != null ? method : "");
                    groupPrivRepository.save(gp);
                }
            }
        }
    }
}
