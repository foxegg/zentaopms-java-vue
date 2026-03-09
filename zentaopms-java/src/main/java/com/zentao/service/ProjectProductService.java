package com.zentao.service;

import com.zentao.entity.Product;
import com.zentao.entity.ProjectProduct;
import com.zentao.repository.ProductRepository;
import com.zentao.repository.ProjectProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目-产品关联服务，对应 PHP project manageProducts。
 */
@Service
@RequiredArgsConstructor
public class ProjectProductService {

    private final ProjectProductRepository projectProductRepository;
    private final ProductRepository productRepository;

    /**
     * 获取项目已关联产品列表（含产品名称）。
     */
    public List<Map<String, Object>> getProductsByProject(int projectId) {
        List<ProjectProduct> ppList = projectProductRepository.findByProject(projectId);
        Set<Integer> productIds = ppList.stream().map(ProjectProduct::getProduct).collect(Collectors.toSet());
        Map<Integer, String> nameMap = productIds.isEmpty() ? Map.of()
                : productRepository.findAllById(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, Product::getName));
        List<Map<String, Object>> out = new ArrayList<>();
        for (ProjectProduct pp : ppList) {
            out.add(Map.<String, Object>of(
                    "id", pp.getId(),
                    "project", pp.getProject(),
                    "product", pp.getProduct(),
                    "productName", nameMap.getOrDefault(pp.getProduct(), ""),
                    "branch", Optional.ofNullable(pp.getBranch()).orElse(0),
                    "plan", Optional.ofNullable(pp.getPlan()).orElse("")
            ));
        }
        return out;
    }

    /**
     * 管理项目关联产品：用 productIDs 覆盖当前关联。
     * body 可传 productIDs: [1,2,3] 或 products: [{ product, branch?, plan? }, ...]
     */
    @Transactional
    public void manageProducts(int projectId, List<Integer> productIDs) {
        projectProductRepository.deleteByProject(projectId);
        for (Integer productId : productIDs) {
            if (productId == null || productId <= 0) continue;
            ProjectProduct pp = new ProjectProduct();
            pp.setProject(projectId);
            pp.setProduct(productId);
            pp.setBranch(0);
            pp.setPlan("");
            projectProductRepository.save(pp);
        }
    }

    @Transactional
    public void manageProductsFromBody(int projectId, Map<String, Object> body) {
        List<Integer> ids = toProductIdList(body.get("productIDs"));
        if (ids.isEmpty() && body.get("products") instanceof List<?> list) {
            for (Object o : list) {
                if (o instanceof Map<?, ?> m) {
                    Object p = m.get("product");
                    if (p instanceof Number) ids.add(((Number) p).intValue());
                }
            }
        }
        manageProducts(projectId, ids);
    }

    private static List<Integer> toProductIdList(Object obj) {
        if (obj instanceof List<?> list) {
            return list.stream()
                    .filter(o -> o instanceof Number)
                    .map(o -> ((Number) o).intValue())
                    .filter(id -> id > 0)
                    .distinct()
                    .toList();
        }
        return List.of();
    }
}
