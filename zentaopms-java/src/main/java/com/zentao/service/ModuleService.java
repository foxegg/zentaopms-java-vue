package com.zentao.service;

import com.zentao.entity.Module;
import com.zentao.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public Optional<Module> getById(int id) {
        return moduleRepository.findById(id).filter(m -> m.getDeleted() == 0);
    }

    public List<Module> getByRoot(int rootId, String type) {
        if (type != null && !type.isEmpty()) {
            return moduleRepository.findByRootAndTypeAndDeletedOrderByOrderNumAsc(rootId, type, 0);
        }
        return moduleRepository.findByRootAndDeletedOrderByOrderNumAsc(rootId, 0);
    }

    public Module create(Module module) {
        module.setDeleted(0);
        return moduleRepository.save(module);
    }

    public Module update(Module module) {
        return moduleRepository.save(module);
    }

    public void delete(int moduleId) {
        moduleRepository.findById(moduleId).ifPresent(m -> {
            m.setDeleted(1);
            moduleRepository.save(m);
        });
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
}
