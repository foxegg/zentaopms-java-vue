package com.zentao.service;

import com.zentao.entity.Branch;
import com.zentao.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public Optional<Branch> getById(int id) {
        return branchRepository.findById(id).filter(b -> b.getDeleted() == 0);
    }

    public List<Branch> getByProduct(int productId) {
        return branchRepository.findByProductAndDeletedOrderByOrderNumAsc(productId, 0);
    }

    public Branch create(Branch branch) {
        branch.setDeleted(0);
        return branchRepository.save(branch);
    }

    public Branch update(Branch branch) {
        return branchRepository.save(branch);
    }

    public void delete(int id) {
        branchRepository.findById(id).ifPresent(b -> {
            b.setDeleted(1);
            branchRepository.save(b);
        });
    }
}
