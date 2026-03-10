package com.zentao.service;

import com.zentao.entity.Action;
import com.zentao.repository.ActionRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 操作记录服务 - 对应 module/action
 */
@Service
@RequiredArgsConstructor
public class ActionService {

    private final ActionRepository actionRepository;

    public Action create(String objectType, int objectId, String actionType) {
        return create(objectType, objectId, actionType, "", "");
    }

    public Action create(String objectType, int objectId, String actionType, String comment) {
        return create(objectType, objectId, actionType, comment, "");
    }

    public Action create(String objectType, int objectId, String actionType, String comment, String extra) {
        var user = getCurrentUser();
        String actor = user != null ? user.getUsername() : "guest";

        Action action = new Action();
        action.setObjectType(objectType);
        action.setObjectID(objectId);
        action.setActor(actor);
        action.setAction(actionType);
        action.setDate(LocalDateTime.now());
        action.setComment(comment != null ? comment : "");
        action.setExtra(extra != null ? extra : "");
        action.setVision("rnd");
        return actionRepository.save(action);
    }

    public List<Action> getList(String objectType, int objectId) {
        return actionRepository.findByObjectTypeAndObjectIDOrderByDateDesc(objectType, objectId);
    }

    /** 分页查询操作记录，与 list 一致；用于返回 pager */
    public Page<Action> getListPage(String objectType, int objectId, Pageable pageable) {
        return actionRepository.findByObjectTypeAndObjectIDOrderByDateDesc(objectType, objectId, pageable);
    }

    public List<Action> getByActor(String actor, Pageable pageable) {
        return actionRepository.findByActorOrderByDateDesc(actor, pageable);
    }

    public List<Action> getByProject(int projectId, Pageable pageable) {
        return actionRepository.findByProjectOrderByDateDesc(projectId, pageable);
    }

    public org.springframework.data.domain.Page<Action> getByExecution(int executionId, Pageable pageable) {
        return actionRepository.findByExecutionOrderByDateDesc(executionId, pageable);
    }

    public Page<Action> getByProduct(int productId, Pageable pageable) {
        return actionRepository.findByObjectTypeAndObjectIDOrderByDateDesc("product", productId, pageable);
    }

    public Optional<Action> getById(int id) {
        return actionRepository.findById(id);
    }

    @Transactional
    public Action updateComment(int actionId, String comment) {
        Action action = actionRepository.findById(actionId).orElseThrow(() -> new RuntimeException("记录不存在"));
        action.setComment(comment != null ? comment : "");
        return actionRepository.save(action);
    }

    private UserPrincipal getCurrentUser() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up;
        }
        return null;
    }
}
