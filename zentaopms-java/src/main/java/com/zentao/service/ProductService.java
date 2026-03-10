package com.zentao.service;

import com.zentao.entity.Product;
import com.zentao.repository.ProductRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<Product> getById(int id) {
        return productRepository.findById(id).filter(p -> p.getDeleted() == 0);
    }

    public Map<Integer, String> getPairs(String mode, int programId) {
        List<Product> products;
        if ("noclosed".equals(mode)) {
            products = productRepository.findByDeletedAndStatusNot(0, "closed");
        } else {
            products = productRepository.findByDeletedOrderByOrderNumAsc(0);
        }
        if (programId > 0) {
            products = products.stream()
                    .filter(p -> programId == p.getProgram())
                    .toList();
        }
        return products.stream().collect(Collectors.toMap(Product::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    /** 按产品 ID 列表返回 id→name，与 project/story 等 pairsByList 约定一致；空列表返回空 Map */
    public Map<Integer, String> getPairsByList(List<Integer> productIdList) {
        if (productIdList == null || productIdList.isEmpty()) return Map.of();
        List<Product> list = productRepository.findAllById(productIdList);
        return list.stream()
                .filter(p -> p.getDeleted() == 0)
                .collect(Collectors.toMap(Product::getId, p -> p.getName() != null ? p.getName() : "", (a, b) -> a));
    }

    public List<Product> getList() {
        return getList(null);
    }

    /** 产品列表，mode=noclosed 时排除已关闭，与 PHP product browse 一致 */
    public List<Product> getList(String mode) {
        return getList(mode, null, 0, 0);
    }

    /**
     * 产品列表，支持 status 筛选与分页；与 PHP product list 参数一致（branch 预留）。
     * recPerPage<=0 时返回全部（不分页）；recPerPage>0 时返回分页结果。
     */
    public List<Product> getList(String mode, String status, int pageID, int recPerPage) {
        List<Product> list;
        if (status != null && !status.isBlank()) {
            list = productRepository.findByDeletedAndStatusOrderByOrderNumAsc(0, status);
        } else if ("noclosed".equals(mode)) {
            list = productRepository.findByDeletedAndStatusNot(0, "closed");
        } else {
            list = productRepository.findByDeletedOrderByOrderNumAsc(0);
        }
        if (recPerPage > 0 && pageID > 0) {
            int from = (pageID - 1) * recPerPage;
            if (from >= list.size()) return List.of();
            int to = Math.min(from + recPerPage, list.size());
            return list.subList(from, to);
        }
        return list;
    }

    /** 分页产品列表，返回 Page 用于统一 pager 结构 */
    public Page<Product> getListPage(String mode, String status, int pageID, int recPerPage) {
        int size = recPerPage > 0 ? Math.min(recPerPage, 100) : 20;
        int page = pageID > 0 ? pageID - 1 : 0;
        PageRequest pr = PageRequest.of(page, size);
        if (status != null && !status.isBlank()) {
            return productRepository.findByDeletedAndStatusOrderByOrderNumAsc(0, status, pr);
        }
        if ("noclosed".equals(mode)) {
            return productRepository.findByDeletedAndStatusNot(0, "closed", pr);
        }
        return productRepository.findByDeletedOrderByOrderNumAsc(0, pr);
    }

    /** 与 PHP 一致：创建产品时设置 createdBy、createdDate（当前用户与时间） */
    public Product create(Product product) {
        product.setDeleted(0);
        product.setCreatedBy(getCurrentAccount());
        product.setCreatedDate(LocalDateTime.now());
        return productRepository.save(product);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public Product activate(int productId) {
        Product product = getById(productId).orElseThrow(() -> new RuntimeException("产品不存在"));
        product.setStatus("normal");
        product.setSubStatus("");
        return productRepository.save(product);
    }

    public Product close(int productId) {
        Product product = getById(productId).orElseThrow(() -> new RuntimeException("产品不存在"));
        product.setStatus("closed");
        return productRepository.save(product);
    }

    public void delete(int productId) {
        Product product = getById(productId).orElseThrow(() -> new RuntimeException("产品不存在"));
        product.setDeleted(1);
        productRepository.save(product);
    }

    @org.springframework.transaction.annotation.Transactional
    public void batchEdit(List<Integer> productIds, Map<String, Object> fields) {
        if (productIds == null || fields == null || fields.isEmpty()) return;
        for (Integer id : productIds) {
            Product p = getById(id).orElse(null);
            if (p == null) continue;
            if (fields.containsKey("name")) p.setName(fields.get("name") != null ? fields.get("name").toString() : p.getName());
            if (fields.containsKey("code")) p.setCode(fields.get("code") != null ? fields.get("code").toString() : p.getCode());
            if (fields.containsKey("line")) p.setLine(fields.get("line") instanceof Number n ? n.intValue() : p.getLine());
            productRepository.save(p);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public void updateOrder(List<Integer> productIds) {
        if (productIds == null) return;
        for (int i = 0; i < productIds.size(); i++) {
            Product p = getById(productIds.get(i)).orElse(null);
            if (p != null) {
                p.setOrderNum(i);
                productRepository.save(p);
            }
        }
    }

    private UserPrincipal getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up;
        }
        return null;
    }
}
