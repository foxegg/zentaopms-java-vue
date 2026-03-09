package com.zentao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zentao")
public class ZentaoProperties {

    private String version = "22.0.0";
    private Db db = new Db();
    private Upload upload = new Upload();

    @Data
    public static class Db {
        private String prefix = "zt_";
    }

    @Data
    public static class Upload {
        private String path = "data/upload/1";
    }
}
