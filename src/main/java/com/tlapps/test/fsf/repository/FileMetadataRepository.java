package com.tlapps.test.fsf.repository;

import com.tlapps.test.fsf.model.FileMetadata;
import org.springframework.data.jpa.repository.Query;

public interface FileMetadataRepository extends BaseRepository<FileMetadata> {

    @Query(value="select fm from FileMetadata fm where fm.fileId = ?1", nativeQuery = false)
    FileMetadata findByFileId(String fileId);

}
