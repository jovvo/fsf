package com.tlapps.test.fsf.repository;

import com.tlapps.test.fsf.model.FilePermission;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilePermissionRepository extends BaseRepository<FilePermission> {

    @Query(value="select fp from FilePermission fp where fp.authorizedReader.email = ?1 and fp.file.fileId = ?2", nativeQuery = false)
    FilePermission findByAuthorizedReaderAndFile(String userId, String fileId);

    @Query(value="select fp from FilePermission fp where fp.authorizedReader.email = ?1", nativeQuery = false)
    List<FilePermission> findByAuthorizedReader(String userId);
}
