package com.zentao.util;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态查询条件构建
 */
public final class SpecificationUtil {

    private SpecificationUtil() {}

    public static <T> Specification<T> buildSpec(Map<String, String> params, Map<String, String> fieldMap) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> e : params.entrySet()) {
                String paramName = e.getKey();
                String value = e.getValue();
                if (value == null || value.isEmpty()) continue;

                String fieldName = fieldMap.getOrDefault(paramName, paramName);
                Path<?> path = getPath(root, fieldName);
                if (path == null) continue;

                Object typedValue = parseValue(path.getJavaType(), value);
                if (typedValue != null) {
                    predicates.add(cb.equal(path, typedValue));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> Path<?> getPath(Root<T> root, String fieldName) {
        try {
            String[] parts = fieldName.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return path;
        } catch (Exception e) {
            return null;
        }
    }

    private static Object parseValue(Class<?> type, String value) {
        if (type == Integer.class || type == int.class) {
            try { return Integer.parseInt(value); } catch (NumberFormatException e) { return null; }
        }
        if (type == Long.class || type == long.class) {
            try { return Long.parseLong(value); } catch (NumberFormatException e) { return null; }
        }
        return value;
    }
}
