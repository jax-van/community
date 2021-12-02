package com.jaxvan.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.exception.CustomizeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AliCouldProvider {
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    @Value("${ali.cloud.endpoint}")
    private String endpoint;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    @Value("${ali.cloud.access-key-id}")
    private String accessKeyId;
    @Value("${ali.cloud.access-key-secret}")
    private String accessKeySecret;
    // 填写Bucket名称，例如examplebucket。
    @Value("${ali.cloud.bucket-name}")
    private String bucketName;

    public String upload(InputStream inputStream, String fileName) {
        String[] split = fileName.split("\\.");
        String objectName = UUID.randomUUID().toString()
                + "." + split[split.length - 1];

        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException e) {
            e.printStackTrace();
            //return null;
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        endpoint = endpoint.substring(endpoint.indexOf("oss"));
        return "https://" + bucketName + "." + endpoint + "/" + objectName;
    }
}
