package com.jdpu.thirdparty.aliyun.util;

import com.aliyun.oss.*;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.jdpu.thirdparty.aliyun.config.ConstantConfig;
import com.jdpu.thirdparty.aliyun.dto.FileDTO;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
public class AliyunOSSUtil {
    @Autowired
    private ConstantConfig constantConfig;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);

    /**
     * 上传文件
     */
    public FileDTO upLoad(File file) {
        logger.info("------OSS文件上传开始--------" + file.getName());
        String endpoint = constantConfig.getEndpoint();
        logger.info("获取到的Point为:" + endpoint);
        String accessKeyId = constantConfig.getAccessKeyId();
        String accessKeySecret = constantConfig.getAccessKeySecret();
        String bucketName = constantConfig.getBucketName();
        String fileHost = constantConfig.getFolder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        // 判断文件
        if (file == null) {
            return null;
        }
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!client.doesBucketExist(bucketName)) {
                client.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                client.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + (dateStr + "/" + uuid) + "-" + file.getName();
            // 设置权限(公开读)
            client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            // 上传文件
            PutObjectResult result = client.putObject(new PutObjectRequest(bucketName, fileUrl, file));

            if (result != null) {
                System.out.println(result);
                logger.info("------OSS文件上传成功------" + fileUrl);
                return new FileDTO(
                        file.length(),//文件大小
                        fileUrl,//文件的绝对路径
                        constantConfig.getWebUrl() + "/" + fileUrl,//文件的web访问地址
                        suffix,//文件后缀
                        "",//存储的bucket
                        bucketName,//原文件名
                        fileHost//存储的文件夹
                );

            }
        } catch (OSSException oe) {
            logger.error(oe.getMessage());
        } catch (ClientException ce) {
            logger.error(ce.getErrorMessage());
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }
        return null;
    }

    /**
     * 上传base64图片文件
     */
    public FileDTO upLoadBase64(String base64) {
        String endpoint = constantConfig.getEndpoint();
        String accessKeyId = constantConfig.getAccessKeyId();
        String accessKeySecret = constantConfig.getAccessKeySecret();
        String bucketName = constantConfig.getBucketName();
        String objectName = "jiankao/" + UUID.randomUUID().toString().replace("-", "") + ".jpg";
        String suffix = objectName.substring(objectName.lastIndexOf(".") + 1);
        String fileHost = constantConfig.getFolder();

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 定义base64分隔符
            String partSeparator = ",";
            byte[] bytes = null;
            if (base64.contains(partSeparator)){
                String encodedImg = base64.split(partSeparator)[1];
                bytes = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            }
//            byte[] bytes = Base64Utils.decodeFromString(base64);
            if (bytes == null) {
                return null;
            }
            PutObjectResult result = ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
            if (result != null) {
                return new FileDTO(
                        Long.parseLong(String.valueOf(base64.length())),//文件大小
                        objectName,//文件的绝对路径
                        constantConfig.getWebUrl() + "/" + objectName,//文件的web访问地址
                        suffix,//文件后缀
                        "",//存储的bucket
                        bucketName,//原文件名
                        fileHost//存储的文件夹
                );
            }

        } catch (OSSException oe) {
            logger.error(oe.getMessage());
        } catch (ClientException ce) {
            logger.error(ce.getErrorMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
