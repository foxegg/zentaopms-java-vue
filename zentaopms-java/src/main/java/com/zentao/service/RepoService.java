package com.zentao.service;

import com.zentao.entity.Repo;
import com.zentao.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoRepository repoRepository;

    public Optional<Repo> getById(int id) {
        return repoRepository.findById(id).filter(r -> r.getDeleted() == 0);
    }

    public List<Repo> getAll() {
        return repoRepository.findByDeletedOrderByIdAsc(0);
    }

    public List<Repo> getByScm(String scm) {
        return scm == null || scm.isBlank()
                ? getAll()
                : repoRepository.findByDeletedAndScmOrderByIdAsc(0, scm);
    }

    public Repo create(Repo repo) {
        repo.setDeleted(0);
        return repoRepository.save(repo);
    }

    public Repo update(Repo repo) {
        return repoRepository.save(repo);
    }

    public void delete(int id) {
        repoRepository.findById(id).ifPresent(r -> {
            r.setDeleted(1);
            repoRepository.save(r);
        });
    }

    /** 创建代码分支，与 PHP repo createBranch 对应；暂无 Git 实现时占位 */
    public void createBranch(int objectID, int repoID) {
        // 实际实现需调用 Git API，此处占位
    }

    /** 解除代码分支关联，与 PHP repo unlinkBranch 对应；占位 */
    public void unlinkBranch(int objectID) {
        // 实际实现需更新任务分支字段等，此处占位
    }
}
