package com.zentao.service;

import com.zentao.entity.User;
import com.zentao.entity.UserView;
import com.zentao.repository.UserViewRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 公共逻辑 - 对应 module/common
 */
@Service
@RequiredArgsConstructor
public class CommonService {

    private final UserViewRepository userViewRepository;

    public UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up;
        }
        return null;
    }

    public boolean isAdmin() {
        var user = getCurrentUser();
        return user != null && user.isAdmin();
    }

    /**
     * 获取当前用户可访问的产品ID列表
     */
    public Set<Integer> getViewProducts() {
        var user = getCurrentUser();
        if (user == null) return Collections.emptySet();
        if (user.isAdmin()) return null; // admin 可访问全部

        return userViewRepository.findByAccount(user.getUsername())
                .map(uv -> parseIdList(uv.getProducts()))
                .orElse(Collections.emptySet());
    }

    /**
     * 获取当前用户可访问的项目ID列表
     */
    public Set<Integer> getViewProjects() {
        var user = getCurrentUser();
        if (user == null) return Collections.emptySet();
        if (user.isAdmin()) return null;

        return userViewRepository.findByAccount(user.getUsername())
                .map(uv -> parseIdList(uv.getProjects()))
                .orElse(Collections.emptySet());
    }

    /**
     * 获取当前用户可访问的执行(sprint)ID列表
     */
    public Set<Integer> getViewSprints() {
        var user = getCurrentUser();
        if (user == null) return Collections.emptySet();
        if (user.isAdmin()) return null;

        return userViewRepository.findByAccount(user.getUsername())
                .map(uv -> parseIdList(uv.getSprints()))
                .orElse(Collections.emptySet());
    }

    public boolean canAccessProduct(int productId) {
        var products = getViewProducts();
        if (products == null) return true; // admin
        return products.contains(productId);
    }

    public boolean canAccessProject(int projectId) {
        var projects = getViewProjects();
        if (projects == null) return true;
        return projects.contains(projectId);
    }

    private Set<Integer> parseIdList(String str) {
        if (str == null || str.isEmpty()) return Collections.emptySet();
        Set<Integer> set = new HashSet<>();
        for (String s : str.split(",")) {
            try {
                set.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return set;
    }
}
