package com.zentao.service;

import com.zentao.entity.Doc;
import com.zentao.entity.DocLib;
import com.zentao.repository.DocLibRepository;
import com.zentao.repository.DocRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocService {

    private final DocRepository docRepository;
    private final DocLibRepository docLibRepository;
    private final ActionService actionService;

    public Optional<Doc> getById(int id) {
        return docRepository.findById(id).filter(d -> d.getDeleted() == 0);
    }

    public List<Doc> getByLib(int libId) {
        return docRepository.findByLibAndDeletedOrderByOrderNumAsc(libId, 0);
    }

    /** 按库与模块获取文档列表，与 PHP doc 按 lib+module 浏览一致 */
    public List<Doc> getByLibAndModule(int libId, int moduleId) {
        if (moduleId > 0) {
            return docRepository.findByLibAndModuleAndDeletedOrderByOrderNumAsc(libId, moduleId, 0);
        }
        return getByLib(libId);
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

    /** 文档库 id→name 下拉用，与 PHP doc getLibPairs 简化一致；product≤0 返回全部库，否则仅该产品下库 */
    public Map<Integer, String> getLibPairs(int productId) {
        List<DocLib> list = productId > 0 ? getLibsByProduct(productId) : getAllLibs();
        return list.stream().collect(Collectors.toMap(DocLib::getId, lib -> lib.getName() != null ? lib.getName() : "", (a, b) -> a));
    }

    /** 与 PHP 一致：创建时设置 addedBy、addedDate（当前用户与时间） */
    public Doc create(Doc doc) {
        doc.setDeleted(0);
        doc.setAddedBy(getCurrentAccount());
        doc.setAddedDate(LocalDateTime.now());
        return docRepository.save(doc);
    }

    public Doc update(Doc doc) {
        return docRepository.save(doc);
    }

    private static String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) return up.getUsername();
        return "";
    }

    /** 删除文档（软删除），与 PHP doc delete 一致：置 deleted=1 并记录 action 'deleted'，extra 为可恢复标识 */
    @Transactional
    public void delete(int docId) {
        docRepository.findById(docId).ifPresent(d -> {
            d.setDeleted(1);
            docRepository.save(d);
            actionService.create("doc", docId, "deleted", "", "1");
        });
    }

    /** 批量移动文档到指定库/模块，与 PHP doc batchMoveDoc 一致 */
    @Transactional
    public void batchMoveDoc(List<Integer> docIds, int lib, int module) {
        if (docIds == null) return;
        for (Integer id : docIds) {
            if (id == null || id <= 0) continue;
            docRepository.findById(id).filter(d -> d.getDeleted() == 0).ifPresent(d -> {
                d.setLib(lib);
                d.setModule(module);
                docRepository.save(d);
            });
        }
    }

    public DocLib createLib(DocLib lib) {
        lib.setDeleted(0);
        return docLibRepository.save(lib);
    }
}
