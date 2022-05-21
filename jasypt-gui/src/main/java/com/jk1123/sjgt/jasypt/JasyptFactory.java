package com.jk1123.sjgt.jasypt;

import com.jk1123.sjgt.jasypt.config.JasyptConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;



/**
 * @author huyoufu <https://github.com/huyoufu>
 * @TIME 2022/5/21 2:01
 * @description
 */
public class JasyptFactory {
    private static final String ENV_JASYPT_PASSWORD_KEY="JASYPT_PASSWORD";
    private static final String DEFAULT_ALGORITHM="PBEWithMD5AndDES";
    private JasyptConfig jasyptConfig;

    public JasyptFactory() {
    }

    public JasyptFactory(JasyptConfig jasyptConfig) {
        this.jasyptConfig = jasyptConfig;
    }

    public JasyptConfig getJasyptConfig() {
        return jasyptConfig;
    }

    public JasyptFactory setJasyptConfig(JasyptConfig jasyptConfig) {
        this.jasyptConfig = jasyptConfig;
        return this;
    }

    public  StringEncryptor defaultEncryptor(){
        String pass = System.getenv(ENV_JASYPT_PASSWORD_KEY);
        return defaultEncryptor(pass);
    }
    /**
     * 获取一个默认的字符串加密器对象
     * @param jasypt_password
     * @return
     */
    public  StringEncryptor defaultEncryptor(String jasypt_password){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(jasypt_password);
        //PBEWithMD5AndDES 无需额外配置jdk
        //PBEWITHHMACSHA512ANDAES_256 高级的配置但是需要额外配置jdk 不方便
        config.setAlgorithm(DEFAULT_ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        config.setProviderClassName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
