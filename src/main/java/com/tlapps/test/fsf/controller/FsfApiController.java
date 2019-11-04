package com.tlapps.test.fsf.controller;


import com.tlapps.test.fsf.model.*;
import com.tlapps.test.fsf.service.FsfApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Slf4j
@CrossOrigin
@RequestMapping("/fsf/api")
@RestController
public final class FsfApiController {

    private FsfApiService fsfApiService;

    @Autowired
    public FsfApiController(FsfApiService fsfApiService) {
        this.fsfApiService = fsfApiService;
    }

    /**
     * This endpoint will return json object with two properties: owned and shared. Both of those will
     * be an array of objects that are representing files owned by authenticated user and files that
     * are shared with him.
     *
     * @param
     * @return
     */
    @GetMapping(value = "/file",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<FilesMetadataResponse> fetchFilesMetadata() {
            String userId = resolveUserId();

            List<FileMetadata> ownedFiles = fsfApiService.fetchOwnedFilesMetadata(userId);
            List<FileMetadata> sharedFiles = fsfApiService.fetchSharedFilesMetadata(userId);

            FilesMetadataResponse response = new FilesMetadataResponse();
            response.setOwnedFiles(ownedFiles);
            response.setSharedFiles(sharedFiles);

            response.setMessage("user_registration_success");
            return new ResponseEntity<FilesMetadataResponse>(response, HttpStatus.ACCEPTED);
        }


    /**
     * This endpoint will be used to download file associated with given ID in uri.
     * For this task assume that all files that users will upload will be plain text files.
     *
     * @param fileId
     * @return
     */
    @GetMapping(value = "/file/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> findFile(
            @PathVariable("id") String fileId, HttpServletRequest request) {
        String userId = resolveUserId();

        Resource resource = fsfApiService.findFile(userId, fileId);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    /**
     * This endpoint will be used to upload the file and on success it will return file ID that can be
     * used with GET endpoint to download file. Make sure that this ID is String.
     * Which content type will be the most suitable for this endpoint?
     *
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile  multipartFile) {
        String userId = resolveUserId();

        fsfApiService.uploadFile(userId, null, multipartFile);

        UploadFileResponse response = new UploadFileResponse();
        response.setMessage("file_upload_success");
        return new ResponseEntity<UploadFileResponse>(response, HttpStatus.CREATED);
    }


    /**
     * This endpoint will accept json object with two string properties: email and file ID.
     * It will be used by file owner to share access to the file with other users. Only the file owner
     * can give the access to his file.
     *
     * @param shareFileRequest
     * @return
     */
    @PostMapping(value = "/share",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ShareFileResponse> shareFile(
            @RequestBody ShareFileRequest shareFileRequest) {
        String userId = resolveUserId();

        fsfApiService.shareFile(userId, shareFileRequest);

        ShareFileResponse response = new ShareFileResponse();
        response.setMessage("share_file_success");
        return new ResponseEntity<ShareFileResponse>(response, HttpStatus.ACCEPTED);
    }

    private String resolveUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        throw new NonceExpiredException("nonce_expired");
    }
}