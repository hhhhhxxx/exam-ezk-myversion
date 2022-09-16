package com.jdpu.thirdparty.qrcode.secure;

import com.jdpu.thirdparty.qrcode.util.EzkEncryptUtil;

public class Test {
    public static void main(String[] args) {
        String content = "test中文";
        String encryptString = EzkEncryptUtil.getEncryptString(content);
        System.out.println("encryptString = " + encryptString);
        String decryptString = EzkEncryptUtil.getDecryptString(encryptString);
        System.out.println("decryptString = " + decryptString);

    }
}
