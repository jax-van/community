package com.jaxvan.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Service
public class AliCouldService {
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    private String endpoint;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    @Value("${ali.cloud.access-key-id}")
    private String accessKeyId;
    @Value("${ali.cloud.access-key-secret}")
    private String accessKeySecret;
    // 填写Bucket名称，例如examplebucket。
    @Value("${ali.cloud.bucket-name}")
    private String bucketName;

    public void upload() {
        // 填写文件名。文件名包含路径，不包含Bucket名称。例如exampledir/exampleobject.txt。
        String objectName = "github.png";

        OSS ossClient = null;
        try {
            // 创建OSSClient实例。
            System.out.println(bucketName);
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream("123".getBytes(StandardCharsets.UTF_8)));
        } catch (OSSException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    public static void main(String[] args) {
        new AliCouldService().upload();
    }
}
