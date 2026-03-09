package com.zentao.service;

import com.zentao.entity.User;
import com.zentao.repository.UserRepository;
import com.zentao.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getById(int id) {
        return userRepository.findById(id).filter(u -> u.getDeleted() == 0);
    }

    public Optional<User> getByAccount(String account) {
        return userRepository.findByAccountAndDeleted(account, 0);
    }

    public List<User> getPairs(String mode) {
        if ("nodeleted".equals(mode) || "noclosed".equals(mode)) {
            return userRepository.findAll((root, query, cb) ->
                    cb.and(cb.equal(root.get("deleted"), 0)));
        }
        return userRepository.findAll((root, query, cb) ->
                cb.equal(root.get("deleted"), 0));
    }

    public Page<User> getList(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(
                Specification.where(spec).and((root, q, cb) -> cb.equal(root.get("deleted"), 0)),
                pageable);
    }

    @Transactional
    public User create(User user) {
        user.setPassword(PasswordEncoder.encode(user.getPassword()));
        user.setDeleted(0);
        return userRepository.save(user);
    }

    @Transactional
    public User update(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        userRepository.findById(id).ifPresent(u -> {
            u.setDeleted(1);
            userRepository.save(u);
        });
    }

    @Transactional
    public java.util.List<Integer> batchCreate(java.util.List<User> users) {
        if (users == null || users.isEmpty()) return java.util.List.of();
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        for (User u : users) {
            User created = create(u);
            ids.add(created.getId());
        }
        return ids;
    }

    @Transactional
    public void batchEdit(java.util.List<Integer> userIds, java.util.Map<String, Object> fields) {
        if (userIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : userIds) {
            User user = getById(id).orElse(null);
            if (user == null) continue;
            if (fields.containsKey("realname")) user.setRealname(fields.get("realname") != null ? fields.get("realname").toString() : user.getRealname());
            if (fields.containsKey("email")) user.setEmail(fields.get("email") != null ? fields.get("email").toString() : user.getEmail());
            if (fields.containsKey("mobile")) user.setMobile(fields.get("mobile") != null ? fields.get("mobile").toString() : user.getMobile());
            if (fields.containsKey("role")) user.setRole(fields.get("role") != null ? fields.get("role").toString() : user.getRole());
            if (fields.containsKey("dept")) user.setDept(fields.get("dept") instanceof Number n ? n.intValue() : user.getDept());
            if (fields.containsKey("password") && fields.get("password") != null && !fields.get("password").toString().isEmpty()) {
                user.setPassword(PasswordEncoder.encode(fields.get("password").toString()));
            }
            userRepository.save(user);
        }
    }

    @Transactional
    public void unlock(int userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setLocked(null);
            u.setFails(0);
            userRepository.save(u);
        });
    }

    /** 解绑（如第三方账号），与 PHP user unbind 对应，暂无绑定字段时仅占位 */
    @Transactional
    public void unbind(int userId) {
        getById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        // 若有 bind/oauth 等字段可在此清除
    }

    @Transactional
    public void resetPassword(int userId, String newPassword) {
        User user = getById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(PasswordEncoder.encode(newPassword != null ? newPassword : ""));
        user.setLocked(null);
        user.setFails(0);
        user.setResetToken(null);
        userRepository.save(user);
    }

    /** 仅更新资料字段（不修改密码） */
    @Transactional
    public User updateProfile(String account, java.util.Map<String, Object> fields) {
        User user = getByAccount(account).orElseThrow(() -> new RuntimeException("用户不存在"));
        if (fields.containsKey("realname")) user.setRealname(fields.get("realname") != null ? fields.get("realname").toString() : user.getRealname());
        if (fields.containsKey("email")) user.setEmail(fields.get("email") != null ? fields.get("email").toString() : user.getEmail());
        if (fields.containsKey("mobile")) user.setMobile(fields.get("mobile") != null ? fields.get("mobile").toString() : user.getMobile());
        if (fields.containsKey("phone")) user.setPhone(fields.get("phone") != null ? fields.get("phone").toString() : user.getPhone());
        if (fields.containsKey("address")) user.setAddress(fields.get("address") != null ? fields.get("address").toString() : user.getAddress());
        return userRepository.save(user);
    }

    /** 当前用户修改密码（需验证旧密码） */
    @Transactional
    public boolean changePassword(String account, String oldPassword, String newPassword) {
        User user = getByAccount(account).orElse(null);
        if (user == null || oldPassword == null || newPassword == null || newPassword.isEmpty()) return false;
        if (!PasswordEncoder.matches(oldPassword, user.getPassword() != null ? user.getPassword() : "")) return false;
        user.setPassword(PasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }
}
