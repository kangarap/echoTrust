package com.kgr.echoTrust.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author kgr
 * @create 2022-10-21 10:45
 */

@Data
@Component
@EnableConfigurationProperties
public class ConfigProperties {

    @Value("${resource-name}")
    private String resourceName;


    @Value("${organizations.bigdataorg.admin.sign-certs}")
    private String adminSignCerts;

    @Value("${organizations.bigdataorg.admin.key-store}")
    private String adminKeyStore;

    @Value("${organizations.bigdataorg.mspid}")
    private String mspId;
}
