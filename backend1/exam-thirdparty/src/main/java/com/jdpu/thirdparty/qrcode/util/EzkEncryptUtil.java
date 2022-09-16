package com.jdpu.thirdparty.qrcode.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.jdpu.thirdparty.qrcode.constant.RsaKeyBean;

/**
 * 加密工具类
 * @author zuck
 */
public class EzkEncryptUtil {

    /**
     * 加密
     * @param unEncrypt
     * @return
     */
    public static String getEncryptString(String unEncrypt){
        String content = unEncrypt;

        //密钥
        byte[] key = RsaKeyBean.getKet();
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);

        return encryptHex;
    }

    /**
     * 解密
     */
    public static String getDecryptString(String encryptStr){

        //生成密钥
        byte[] key = RsaKeyBean.getKet();

        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //解密为字符串
        String decryptStr = aes.decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);

        return decryptStr;
    }
}
