package com.tlapps.test.fsf.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
public class UploadFileProperties {

    private String uploadDirectory;

}
