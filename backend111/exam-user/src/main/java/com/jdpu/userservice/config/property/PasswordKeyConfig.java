package com.jdpu.userservice.config.property;

/**
 * 公钥私钥配置
 * @author zuck
 */
public class PasswordKeyConfig {
    private String publicKey;

    private String privateKey;

    /**
     * 获取公钥
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 设置公钥
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * 获取私钥
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * 设置私钥
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
