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
import org.springframework.security.access.AuthorizationServiceException;
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
import java.util.stream.Collectors;

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

    public List<FileMetadata> fetchOwnedFilesMetadata(String userId) {
        List<FileMetadata> ownedFiles = fileMetadataRepository.findByOwner(userId);
        ownedFiles.forEach(file -> file.setOwner(null));
        return ownedFiles;
    }

    public List<FileMetadata> fetchSharedFilesMetadata(String userId) {
        List<FilePermission> sharedFilesPermissions = filePermissionRepository.findByAuthorizedReader(userId);
        List<FileMetadata> sharedFiles = sharedFilesPermissions.stream()
                .map(FilePermission::getFile)
                .collect(Collectors.toList());
        sharedFiles.forEach(file -> file.setOwner(null));
        return sharedFiles;
    }

    public Resource findFile(String userId, String fileId) {

        FilePermission readPermission = filePermissionRepository.findByAuthorizedReaderAndFile(userId, fileId);
        FileMetadata file = fileMetadataRepository.findByFileId(fileId);

        if (!file.getOwner().getEmail().equals(userId) && readPermission == null) {
            throw new AuthorizationServiceException("tampering_error");
        }

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

    public void uploadFile(String userId, UploadFileRequest uploadFileRequest, MultipartFile multipartFile) {
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
            fileMetadata.setOwner(userRepository.findByEmail(userId));

            fileMetadataRepository.save(fileMetadata);

        } catch (IOException ex) {
            throw new UploadFileException("Upload file error " + generatedFileId, ex);
        }

    }

    public void shareFile(String userId, ShareFileRequest shareFileRequest) {
        log.info("sharing file. with id = " + shareFileRequest.getFileId() + " to user " + shareFileRequest.getEmail());
        User currentUser = userRepository.findByEmail(userId);

        User user = userRepository.findByEmail(shareFileRequest.getEmail());
        FileMetadata file = fileMetadataRepository.findByFileId(shareFileRequest.getFileId());

        if(!file.getOwner().equals(currentUser)){
            throw new AuthorizationServiceException("tampering_error");
        }

        FilePermission filePermission = new FilePermission();
        filePermission.setAuthorizedReader(user);
        filePermission.setFile(file);
        filePermission.setChangedOn(LocalDateTime.now());

        filePermissionRepository.save(filePermission);
    }
}