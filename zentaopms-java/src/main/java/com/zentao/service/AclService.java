package com.zentao.service;

import com.zentao.entity.Acl;
import com.zentao.repository.AclRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限/白名单服务 - 对应 module/personnel 的 zt_acl 白名单
 */
@Service
@RequiredArgsConstructor
public class AclService {

    private final AclRepository aclRepository;

    /** 与 PHP getWhitelist 一致：按 objectType、objectID 查询白名单 */
    public List<Acl> getWhitelist(String objectType, int objectID) {
        return aclRepository.findByObjectTypeAndObjectIDAndType(objectType, objectID, "whitelist");
    }

    public List<String> getWhitelistAccounts(String objectType, int objectID) {
        return aclRepository.findByObjectTypeAndObjectIDAndType(objectType, objectID, "whitelist")
                .stream()
                .map(Acl::getAccount)
                .filter(a -> a != null && !a.isEmpty())
                .distinct()
                .toList();
    }

    /** 与 PHP updateWhitelist 一致：替换或追加白名单账号 */
    @Transactional
    public void updateWhitelist(String objectType, int objectID, List<String> accounts, boolean replace) {
        if (replace) aclRepository.deleteByObjectTypeAndObjectID(objectType, objectID);
        if (accounts == null) return;
        for (String account : accounts) {
            if (account == null || account.isBlank()) continue;
            if (!replace && aclRepository.findByObjectTypeAndObjectIDAndType(objectType, objectID, "whitelist")
                    .stream().anyMatch(a -> account.equals(a.getAccount()))) continue;
            Acl acl = new Acl();
            acl.setAccount(account);
            acl.setObjectType(objectType);
            acl.setObjectID(objectID);
            acl.setType("whitelist");
            acl.setSource("add");
            aclRepository.save(acl);
        }
    }

    @Transactional
    public void deleteFromWhitelist(String objectType, int objectID, String account) {
        aclRepository.deleteByObjectTypeAndObjectIDAndAccount(objectType, objectID, account);
    }
}
