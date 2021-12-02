package com.jaxvan.community.provider;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "alibaba.cloud")
@Component
public class AliCloudProperties {
    private String endpoint = "*** Provide OSS endpoint ***";
    private String accessKeyId = "*** Provide your AccessKeyId ***";
    private String accessKeySecret = "*** Provide your AccessKeySecret ***";
    private String bucketName = "*** Provide bucket name ***";
}
