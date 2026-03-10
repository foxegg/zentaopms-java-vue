package com.zentao.service;

import com.zentao.config.ZentaoProperties;
import com.zentao.entity.File;
import com.zentao.repository.FileRepository;
import com.zentao.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final ZentaoProperties zentaoProperties;

    public Optional<File> getById(int id) {
        return fileRepository.findById(id).filter(f -> f.getDeleted() == 0);
    }

    public List<File> getByObject(String objectType, int objectId) {
        return fileRepository.findByObjectTypeAndObjectIDAndDeleted(objectType, objectId, 0);
    }

    /** 与 PHP 一致：创建附件记录时设置 addedBy、addedDate（当前用户与时间） */
    public File create(File file) {
        file.setDeleted(0);
        file.setAddedBy(getCurrentAccount());
        file.setAddedDate(LocalDateTime.now());
        return fileRepository.save(file);
    }

    public void delete(int fileId) {
        fileRepository.findById(fileId).ifPresent(f -> {
            f.setDeleted(1);
            fileRepository.save(f);
        });
    }

    public Optional<Path> getRealPath(File file) {
        if (file == null || file.getPathname() == null) return Optional.empty();
        Path p = Paths.get(zentaoProperties.getUpload().getPath(), file.getPathname());
        return Files.exists(p) ? Optional.of(p) : Optional.empty();
    }

    public File uploadFile(MultipartFile multipart, String objectType, int objectId, String extra) throws IOException {
        String ext = "";
        String name = multipart.getOriginalFilename();
        if (name != null && name.contains(".")) {
            ext = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
        }
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 8) + "." + ext;
        String pathname = datePath + "/" + fileName;

        Path dir = Paths.get(zentaoProperties.getUpload().getPath(), datePath);
        Files.createDirectories(dir);
        Path target = dir.resolve(fileName);
        multipart.transferTo(target.toFile());

        File file = new File();
        file.setPathname(pathname);
        file.setTitle(name != null ? name : fileName);
        file.setExtension(ext);
        file.setSize((int) multipart.getSize());
        file.setObjectType(objectType != null ? objectType : "");
        file.setObjectID(objectId);
        file.setExtra(extra != null ? extra : "");
        file.setAddedBy(getCurrentAccount());
        file.setAddedDate(LocalDateTime.now());
        file.setDeleted(0);
        return fileRepository.save(file);
    }

    private String getCurrentAccount() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal up) {
            return up.getUsername();
        }
        return "";
    }
}
