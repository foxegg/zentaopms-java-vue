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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;

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

    /** 返回用户列表，mode 含 nodeleted/noclosed 时仅查未删除用户；与 PHP user getPairs 的列表部分一致 */
    public List<User> getPairs(String mode) {
        return userRepository.findAll((root, query, cb) ->
                cb.equal(root.get("deleted"), 0));
    }

    /** 与 PHP user getPairs(params) 一致：下拉用 account→realname；nodeleted 过滤已删除；不含 noclosed 时追加 closed→Closed；可选 type=inside 过滤 */
    public Map<String, String> getPairsMap(String mode) {
        return getPairsMap(mode, 0, null);
    }

    /** 与 PHP user getPairs(params, ..., maxCount) 一致：maxCount>0 时限制返回条数 */
    public Map<String, String> getPairsMap(String mode, int maxCount) {
        return getPairsMap(mode, maxCount, null);
    }

    /** vision 非空时仅返回 visions 包含该值的用户（与 PHP FIND_IN_SET(config.vision, visions) 一致） */
    public Map<String, String> getPairsMap(String mode, int maxCount, String vision) {
        boolean appendClosed = (mode == null || !mode.contains("noclosed"));
        boolean appendGuest = (mode != null && mode.contains("withguest"));
        boolean noletter = (mode != null && mode.contains("noletter"));
        boolean showid = (mode != null && mode.contains("showid"));
        List<User> list = getPairsUserList(mode, maxCount, vision);
        Map<String, String> map = new LinkedHashMap<>();
        for (User u : list) {
            String k = u.getAccount() != null ? u.getAccount() : "";
            String v;
            if (showid) {
                v = String.valueOf(u.getId());
            } else {
                v = (u.getRealname() != null && !u.getRealname().isEmpty()) ? u.getRealname() : k;
                if (!noletter && !k.isEmpty()) {
                    String first = k.substring(0, 1);
                    v = first.toUpperCase() + ":" + v;
                }
            }
            map.put(k, v);
        }
        if (appendClosed) map.put("closed", "Closed");
        if (appendGuest) map.put("guest", "Guest");
        return map;
    }

    private static boolean visionsContains(String visions, String vision) {
        if (visions == null || vision == null) return false;
        for (String v : visions.split(",")) {
            if (v.trim().equalsIgnoreCase(vision)) return true;
        }
        return false;
    }

    /** 与 PHP getPairs 的 roleOrder DESC, account 一致：pofirst/pdfirst/qafirst/qdfirst/pmfirst/devfirst，优先角色排前 */
    private static Comparator<User> roleOrderComparator(String mode) {
        final List<String> order = roleOrderForMode(mode);
        return (a, b) -> {
            String ra = a.getRole() != null ? a.getRole().trim() : "";
            String rb = b.getRole() != null ? b.getRole().trim() : "";
            int ia = order.indexOf(ra);
            int ib = order.indexOf(rb);
            if (ia < 0) ia = order.size();
            if (ib < 0) ib = order.size();
            if (ia != ib) return Integer.compare(ia, ib);
            String acctA = a.getAccount() != null ? a.getAccount() : "";
            String acctB = b.getAccount() != null ? b.getAccount() : "";
            return acctA.compareTo(acctB);
        };
    }

    /** PHP INSTR 顺序：越靠前在 order 中下标越小，排序时排前面 */
    private static List<String> roleOrderForMode(String mode) {
        List<String> o = new ArrayList<>();
        if (mode.contains("devfirst")) { o.add("dev"); o.add("qa"); o.add("qd"); o.add("pm"); o.add("td"); return o; }
        if (mode.contains("pmfirst")) { o.add("pm"); o.add("td"); return o; }
        if (mode.contains("pofirst")) { o.add("po"); o.add("pd"); return o; }
        if (mode.contains("pdfirst")) { o.add("pd"); o.add("po"); return o; }
        if (mode.contains("qafirst")) { o.add("qa"); o.add("qd"); return o; }
        if (mode.contains("qdfirst")) { o.add("qd"); o.add("qa"); return o; }
        return o;
    }

    /** 与 PHP user getPairs(params) 当 params 含 useid 一致：下拉用 id→realname；便于以 id 为 key 的接口 */
    public Map<Integer, String> getPairsMapById(String mode) {
        return getPairsMapById(mode, 0);
    }

    /** useid + maxCount：返回 id→realname，maxCount>0 时限制条数；含 first 时按角色排序；vision 同 getPairsMap */
    public Map<Integer, String> getPairsMapById(String mode, int maxCount) {
        return getPairsMapById(mode, maxCount, null);
    }

    public Map<Integer, String> getPairsMapById(String mode, int maxCount, String vision) {
        boolean appendClosed = (mode == null || !mode.contains("noclosed"));
        List<User> list = getPairsUserList(mode, maxCount, vision);
        Map<Integer, String> map = new LinkedHashMap<>();
        for (User u : list) {
            String v = u.getRealname() != null && !u.getRealname().isEmpty() ? u.getRealname() : (u.getAccount() != null ? u.getAccount() : "");
            map.put(u.getId(), v);
        }
        if (appendClosed) map.put(-1, "Closed");
        if (mode != null && mode.contains("withguest")) map.put(-2, "Guest");
        return map;
    }

    private List<User> getPairsUserList(String mode, int maxCount, String vision) {
        boolean insideOnly = (mode == null || (!mode.contains("outside") && !mode.contains("all")));
        var spec = (org.springframework.data.jpa.domain.Specification<User>) (root, query, cb) -> {
            var p = cb.equal(root.get("deleted"), 0);
            if (insideOnly) p = cb.and(p, cb.equal(root.get("type"), "inside"));
            if (vision != null && !vision.isEmpty()) p = cb.and(p, cb.isNotNull(root.get("visions")));
            return p;
        };
        List<User> list = (maxCount > 0)
                ? userRepository.findAll(spec, PageRequest.of(0, Math.max(maxCount, 5000))).getContent()
                : userRepository.findAll(spec);
        if (vision != null && !vision.isEmpty()) {
            list = list.stream().filter(u -> visionsContains(u.getVisions(), vision)).toList();
        }
        if (mode != null && mode.contains("first")) {
            list = list.stream().sorted(roleOrderComparator(mode)).toList();
        }
        if (maxCount > 0 && list.size() > maxCount) list = list.subList(0, maxCount);
        return list;
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
