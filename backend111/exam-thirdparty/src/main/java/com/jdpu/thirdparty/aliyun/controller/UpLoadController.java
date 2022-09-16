package com.jdpu.thirdparty.aliyun.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jdpu.thirdparty.aliyun.dto.FileDTO;
import com.jdpu.thirdparty.aliyun.util.AliyunOSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/api/thirdparty/oss")
public class UpLoadController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    /**
     * 文件上传
     * 接收file类型参数
     * */
    @RequestMapping(value = "/uploadFile")
    public FileDTO uploadBlog(@RequestParam("file") MultipartFile file) {
        logger.info("文件上传");
        String filename = file.getOriginalFilename();
        logger.info("文件名："+filename);
        try {
            // 判断文件
            if (file!=null) {
                if (!"".equals(filename.trim())) {
                    File newFile = new File(filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    // 上传到OSS
                    return aliyunOSSUtil.upLoad(newFile);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 上传base64图片
     * */
    @PostMapping(value = "/uploadBase64")
    public FileDTO uploadBase64(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String imgBase64 = jsonObject.getStr("imgBase64");
        logger.info("上传base64图片");
        try {
            return aliyunOSSUtil.upLoadBase64(imgBase64);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
