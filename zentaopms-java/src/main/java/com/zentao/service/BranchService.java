package com.zentao.service;

import com.zentao.entity.Branch;
import com.zentao.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final ActionService actionService;

    public Optional<Branch> getById(int id) {
        return branchRepository.findById(id).filter(b -> b.getDeleted() == 0);
    }

    public List<Branch> getByProduct(int productId) {
        return branchRepository.findByProductAndDeletedOrderByOrderNumAsc(productId, 0);
    }

    /** 分支 id->name 下拉用，与 PHP branch getPairs 一致；productID 为 0 时返回空 */
    public Map<Integer, String> getPairs(int productId) {
        if (productId <= 0) return Map.of();
        List<Branch> list = getByProduct(productId);
        return list.stream().collect(Collectors.toMap(Branch::getId, b -> b.getName() != null ? b.getName() : "", (a, b) -> a));
    }

    /** 按分支 ID 列表返回 id→name，与 PHP branch getPairsByIdList(branchIdList) 一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> branchIdList) {
        if (branchIdList == null || branchIdList.isEmpty()) return Map.of();
        List<Branch> list = branchRepository.findAllById(branchIdList);
        return list.stream()
                .filter(b -> b.getDeleted() == 0)
                .collect(Collectors.toMap(Branch::getId, b -> b.getName() != null ? b.getName() : "", (a, b) -> a));
    }

    /** 创建分支，与 PHP branch create 一致：保存后记录 action 'Opened' */
    @Transactional
    public Branch create(Branch branch) {
        branch.setDeleted(0);
        Branch saved = branchRepository.save(branch);
        actionService.create("branch", saved.getId(), "Opened");
        return saved;
    }

    public Branch update(Branch branch) {
        return branchRepository.save(branch);
    }

    /** 删除分支（软删除），与 PHP 一致：置 deleted=1 并记录 action 'deleted' */
    @Transactional
    public void delete(int id) {
        branchRepository.findById(id).ifPresent(b -> {
            b.setDeleted(1);
            branchRepository.save(b);
            actionService.create("branch", id, "deleted", "", "1");
        });
    }
}
