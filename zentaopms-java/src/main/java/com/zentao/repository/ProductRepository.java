package com.zentao.repository;

import com.zentao.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

    List<Product> findByDeletedOrderByOrderNumAsc(int deleted);

    List<Product> findByDeletedAndStatusNot(int deleted, String excludeStatus);

    List<Product> findByDeletedAndStatusOrderByOrderNumAsc(int deleted, String status);

    Page<Product> findByDeletedOrderByOrderNumAsc(int deleted, Pageable pageable);

    Page<Product> findByDeletedAndStatusNot(int deleted, String excludeStatus, Pageable pageable);

    Page<Product> findByDeletedAndStatusOrderByOrderNumAsc(int deleted, String status, Pageable pageable);
}
