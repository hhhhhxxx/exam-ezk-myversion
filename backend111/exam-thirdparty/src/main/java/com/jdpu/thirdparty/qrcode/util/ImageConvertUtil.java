package com.jdpu.thirdparty.qrcode.util;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import com.jdpu.thirdparty.aliyun.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * 图片转换工具
 */
@Slf4j
public class ImageConvertUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ImageConvertUtil.class);

    /**
     * 将BufferedImage转换为InputStream
     * @param image
     * @return
     */
    public static InputStream bufferedImageToInputStream(BufferedImage image){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", os);
            InputStream input = new ByteArrayInputStream(os.toByteArray());
            return input;
        } catch (IOException e) {
            log.error("提示:",e);
        }
        return null;
    }

    /**
     * 将inputstream转为Base64
     */
    public static String inputStream2Base64(InputStream is) throws Exception {
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new Exception("输入流关闭异常");
                }
            }
        }

        return "data:image/png;base64,"+ Base64.getEncoder().encodeToString(data);
    }

    /**
     * 上传base64图片到oss
     * @param base64
     * @return
     */
    public static FileDTO upLoadBase64(String base64) {
        String endpoint = "http://oss-cn-guangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tP6J2LZjv4QA8SZpq7J";
        String accessKeySecret = "USdhYjjbZDnogHECZNWtgLkR5D7JiO";
        String bucketName = "examsystem-gdpu";
        String objectName = "classCode/" + UUID.randomUUID().toString().replace("-", "") + ".png";
        String suffix = objectName.substring(objectName.lastIndexOf(".") + 1);
        String fileHost = "examsystem";
        String webUrl = "https://examsystem-gdpu.oss-cn-guangzhou.aliyuncs.com";

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
                        webUrl + "/" + objectName,//文件的web访问地址
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
