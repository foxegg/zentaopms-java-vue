package com.zentao.repository;

import com.zentao.entity.Webhook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookRepository extends JpaRepository<Webhook, Integer> {

    List<Webhook> findByDeletedOrderByIdDesc(int deleted);
}
