package com.zentao.service;

import com.zentao.entity.Host;
import com.zentao.repository.HostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HostService {
    private final HostRepository repo;

    public List<Host> getList() {
        return repo.findByDeletedOrderByIdAsc(0);
    }

    public Optional<Host> getById(int id) {
        return repo.findById(id).filter(h -> h.getDeleted() != null && h.getDeleted() == 0);
    }

    @Transactional
    public Host create(Host e) {
        e.setId(null);
        e.setDeleted(0);
        return repo.save(e);
    }

    @Transactional
    public void update(Host e) {
        repo.findById(e.getId()).ifPresent(x -> repo.save(e));
    }

    @Transactional
    public void delete(int id) {
        repo.findById(id).ifPresent(h -> {
            h.setDeleted(1);
            repo.save(h);
        });
    }

    @Transactional
    public void updateStatus(int id, String status, String reason) {
        repo.findById(id).ifPresent(h -> {
            h.setStatus(status);
            repo.save(h);
        });
    }

    /** 拓扑图数据：按机房/分组，与 PHP getServerroomTreemap/getGroupTreemap 一致 */
    public List<Map<String, Object>> getTreemap(String type) {
        List<Host> list = repo.findByDeletedOrderByIdAsc(0);
        if ("serverroom".equals(type)) {
            return list.stream()
                    .collect(Collectors.groupingBy(Host::getServerRoom))
                    .entrySet().stream()
                    .map(e -> Map.<String, Object>of("id", e.getKey() != null ? e.getKey() : 0,
                            "children", e.getValue().stream()
                                    .map(h -> Map.<String, Object>of("id", h.getId(), "name", h.getName() != null ? h.getName() : ""))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        }
        if ("group".equals(type)) {
            return list.stream()
                    .collect(Collectors.groupingBy(h -> h.getHostGroup() != null ? h.getHostGroup() : ""))
                    .entrySet().stream()
                    .map(e -> Map.<String, Object>of("name", e.getKey(),
                            "children", e.getValue().stream()
                                    .map(h -> Map.<String, Object>of("id", h.getId(), "name", h.getName() != null ? h.getName() : ""))
                                    .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    /** 与 PHP lang host osList 一致，返回 [{text, value}] */
    public List<Map<String, String>> getOSOptions(String type) {
        if ("os".equals(type)) {
            return List.of(
                    Map.of("text", "Linux", "value", "linux"),
                    Map.of("text", "Windows", "value", "windows"),
                    Map.of("text", "MacOS", "value", "macos")
            );
        }
        return List.of();
    }
}
