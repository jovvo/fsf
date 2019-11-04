package com.tlapps.test.fsf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class FilesMetadataResponse implements Serializable {
    private String message;
    private List<FileMetadata> ownedFiles;
    private List<FileMetadata> sharedFiles;
}
