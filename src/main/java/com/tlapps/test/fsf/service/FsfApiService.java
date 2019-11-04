package com.tlapps.test.fsf.service;


import com.tlapps.test.fsf.exception.FindFileException;
import com.tlapps.test.fsf.exception.UploadFileException;
import com.tlapps.test.fsf.model.*;
import com.tlapps.test.fsf.repository.FileMetadataRepository;
import com.tlapps.test.fsf.repository.FilePermissionRepository;
import com.tlapps.test.fsf.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Data
public final class FsfApiService {

    private UserRepository userRepository;
    private FileMetadataRepository fileMetadataRepository;
    private FilePermissionRepository filePermissionRepository;
    private Path uploadFileLocation;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @Autowired
    public FsfApiService(
            UserRepository userRepository,
            FileMetadataRepository fileMetadataRepository,
            FilePermissionRepository filePermissionRepository) {
        this.userRepository = userRepository;
        this.fileMetadataRepository = fileMetadataRepository;
        this.filePermissionRepository = filePermissionRepository;
    }

    @PostConstruct
    void postConstruct(){
        this.uploadFileLocation = Paths.get(fileUploadDirectory).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadFileLocation);
        } catch (Exception ex) {
            throw new UploadFileException("Unable to create upload directory.", ex);
        }
    }

    public List<FileMetadata> fetchOwnedFilesMetadata(Long userId) {
        List<FileMetadata> owned = new ArrayList<>();
        FileMetadata fileMetadata1 = new FileMetadata();
        fileMetadata1.setFileId("file id 1");
        owned.add(fileMetadata1);
        FileMetadata fileMetadata2 = new FileMetadata();
        fileMetadata2.setFileId("file id 2");
        owned.add(fileMetadata2);
        FileMetadata fileMetadata3 = new FileMetadata();
        fileMetadata3.setFileId("file id 3");
        owned.add(fileMetadata3);
        return owned;
    }

    public List<FileMetadata> fetchSharedFilesMetadata(Long userId) {
        List<FileMetadata> shared = new ArrayList<>();
        FileMetadata fileMetadata1 = new FileMetadata();
        fileMetadata1.setFileId("file id 1");
        shared.add(fileMetadata1);
        FileMetadata fileMetadata2 = new FileMetadata();
        fileMetadata2.setFileId("file id 2");
        shared.add(fileMetadata2);
        return shared;
    }

    public Resource findFile(Long userId, String fileId) {
        log.info("fetching file with id + " + fileId);
        try {
            Path filePath = this.uploadFileLocation.resolve(fileId).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FindFileException("File with id " + fileId + " is not found");
            }
        } catch (MalformedURLException ex) {
            throw new FindFileException("File with id " + fileId + " is not found", ex);
        }
    }

    public void uploadFile(Long userId, UploadFileRequest uploadFileRequest, MultipartFile multipartFile) {
        log.info("uploading file with name = " + multipartFile.getName());

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String generatedFileId = UUID.randomUUID().toString();

        try {
            if(fileName.contains("..")) {
                throw new UploadFileException("Invalid upload file path " + fileName);
            }

            Path targetLocation = this.uploadFileLocation.resolve(generatedFileId);
            Files.copy(multipartFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setChangedOn(LocalDateTime.now());
            fileMetadata.setFileId(generatedFileId);
            fileMetadata.setOriginalFileName(fileName);

            fileMetadataRepository.save(fileMetadata);

        } catch (IOException ex) {
            throw new UploadFileException("Upload file error " + generatedFileId, ex);
        }

    }

    public void shareFile(Long userId, ShareFileRequest shareFileRequest) {
        log.info("sharing file. with id = " + shareFileRequest.getFileId() + " to user " + shareFileRequest.getEmail());

        User user = userRepository.findByEmail(shareFileRequest.getEmail());
        FileMetadata file = fileMetadataRepository.findByFileId(shareFileRequest.getFileId());

        FilePermission filePermission = new FilePermission();
        filePermission.setAuthorizedReader(user);
        filePermission.setFile(file);
        filePermission.setChangedOn(LocalDateTime.now());

        filePermissionRepository.save(filePermission);
    }
}