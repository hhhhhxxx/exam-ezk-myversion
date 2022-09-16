package com.jdpu.thirdparty.qrcode.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jdpu.common.entity.vo.RestResponse;
import com.jdpu.thirdparty.aliyun.dto.FileDTO;
import com.jdpu.thirdparty.qrcode.entity.ClassInsertVo;
import com.jdpu.thirdparty.qrcode.feign.ClassFeignService;
import com.jdpu.thirdparty.qrcode.util.EzkEncryptUtil;
import com.jdpu.thirdparty.qrcode.util.ImageConvertUtil;
import com.jdpu.thirdparty.qrcode.util.QRCodeUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

/**
 * 班级加入邀请二维码
 */
@CrossOrigin
@RestController
@RequestMapping("/api/thirdparty/class")
public class ClassQrCodeController {
    @Autowired
    private ClassFeignService classFeignService;

    /**
     * 后台请求生成二维码
     * @param jsonStr
     * @return
     */
    @PostMapping("/qrcode")
    public RestResponse<FileDTO> qrcode(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        String classId = jsonObject.getStr("classId");
        // 对内容加密
        String content = EzkEncryptUtil.getEncryptString(classId);
        try {
            BufferedImage image = QRCodeUtil.createImage(content, null, true);
            InputStream inputStream = ImageConvertUtil.bufferedImageToInputStream(image);
            String base64 = ImageConvertUtil.inputStream2Base64(inputStream);
            FileDTO fileDTO = ImageConvertUtil.upLoadBase64(base64);
            return RestResponse.ok(fileDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.fail(500,"获取二维码失败");
        }
    }

    /**
     * 学生扫码加入班级
     * @param multipartFile
     * @param userId
     * @return
     */
    @PostMapping("/decode")
    public RestResponse decode(@RequestParam(value = "file") MultipartFile multipartFile,
                               @RequestParam(value = "userId")String userId) {
        File file = new File("cache/file.png");
        String decode = null;
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
            // 二维码解析
            decode = QRCodeUtil.decode(file);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.fail(500,"请上传二维码图片");
        }
        // 解密
        String classId = EzkEncryptUtil.getDecryptString(decode);
        ClassInsertVo classInsertVo = new ClassInsertVo();
        classInsertVo.setUser_id(userId);
        classInsertVo.setClass_id(classId);
        String jsonStr = JSONUtil.toJsonStr(classInsertVo);
        classFeignService.bindStuClass(jsonStr);
        return RestResponse.ok();
    }
}
