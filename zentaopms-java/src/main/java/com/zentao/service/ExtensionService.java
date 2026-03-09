package com.zentao.service;

import com.zentao.entity.Extension;
import com.zentao.repository.ExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExtensionService {

    private final ExtensionRepository extensionRepository;

    public Optional<Extension> getById(int id) {
        return extensionRepository.findById(id);
    }

    public Optional<Extension> getByCode(String code) {
        return extensionRepository.findByCode(code);
    }

    public List<Extension> getByStatus(String status) {
        return extensionRepository.findByStatusOrderByInstalledTimeDesc(status);
    }

    public List<Extension> getAll() {
        return extensionRepository.findAll();
    }

    public Extension create(Extension extension) {
        return extensionRepository.save(extension);
    }

    public Extension update(Extension extension) {
        return extensionRepository.save(extension);
    }

    public void delete(int id) {
        extensionRepository.deleteById(id);
    }
}
