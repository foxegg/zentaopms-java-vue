package com.zentao.service;

import com.zentao.entity.Product;
import com.zentao.repository.ProductRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        return products.stream().collect(Collectors.toMap(Product::getId, Product::getName));
    }

    public List<Product> getList() {
        var principal = getCurrentUser();
        if (principal != null && principal.isAdmin()) {
            return productRepository.findByDeletedOrderByOrderNumAsc(0);
        }
        return productRepository.findByDeletedOrderByOrderNumAsc(0);
    }

    public Product create(Product product) {
        product.setDeleted(0);
        return productRepository.save(product);
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
