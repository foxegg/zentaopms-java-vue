package com.zentao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * ZenTao PMS - Spring Boot 主入口
 * 禅道项目管理系统 Java 版
 */
@SpringBootApplication
@EnableCaching
public class ZentaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZentaoApplication.class, args);
    }
}
