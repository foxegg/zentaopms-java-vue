package com.zentao.service;

import com.zentao.entity.UserQuery;
import com.zentao.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户保存的搜索 - 对应 module/search 的 saveQuery/deleteQuery
 */
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserQueryRepository userQueryRepository;

    public List<UserQuery> getByAccountAndModule(String account, String module) {
        if (module != null && !module.isEmpty()) {
            return userQueryRepository.findByAccountAndModuleOrderByIdDesc(account, module);
        }
        return userQueryRepository.findByAccountOrderByIdDesc(account);
    }

    public Optional<UserQuery> getById(int id) {
        return userQueryRepository.findById(id);
    }

    @Transactional
    public UserQuery save(UserQuery query) {
        return userQueryRepository.save(query);
    }

    @Transactional
    public void delete(int id) {
        userQueryRepository.deleteById(id);
    }
}
