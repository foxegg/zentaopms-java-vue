package com.zentao.service;

import com.zentao.entity.Doc;
import com.zentao.entity.DocLib;
import com.zentao.repository.DocLibRepository;
import com.zentao.repository.DocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocService {

    private final DocRepository docRepository;
    private final DocLibRepository docLibRepository;

    public Optional<Doc> getById(int id) {
        return docRepository.findById(id).filter(d -> d.getDeleted() == 0);
    }

    public List<Doc> getByLib(int libId) {
        return docRepository.findByLibAndDeletedOrderByOrderNumAsc(libId, 0);
    }

    public List<DocLib> getLibsByProduct(int productId) {
        return docLibRepository.findByProductAndDeletedOrderByOrderNumAsc(productId, 0);
    }

    public List<DocLib> getLibsByProject(int projectId) {
        return docLibRepository.findByProjectAndDeletedOrderByOrderNumAsc(projectId, 0);
    }

    public List<DocLib> getAllLibs() {
        return docLibRepository.findByDeletedOrderByOrderNumAsc(0);
    }

    public Doc create(Doc doc) {
        doc.setDeleted(0);
        return docRepository.save(doc);
    }

    public Doc update(Doc doc) {
        return docRepository.save(doc);
    }

    public void delete(int docId) {
        docRepository.findById(docId).ifPresent(d -> {
            d.setDeleted(1);
            docRepository.save(d);
        });
    }

    public DocLib createLib(DocLib lib) {
        lib.setDeleted(0);
        return docLibRepository.save(lib);
    }
}
