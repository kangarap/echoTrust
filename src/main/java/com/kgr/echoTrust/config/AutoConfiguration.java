package com.kgr.echoTrust.config;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author kgr
 * @create 2022-10-21 10:44
 */
@Configuration
@EnableConfigurationProperties(ConfigProperties.class)
@Slf4j
public class AutoConfiguration {

    @Resource
    private ConfigProperties configProperties;


    @Bean
    public Gateway.Builder builder() throws IOException {
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);

        try (
                InputStream adminSignCertInputStream  = this.getClass().getResourceAsStream(configProperties.getAdminSignCerts());
                InputStream adminKeyStoreInputStream = this.getClass().getResourceAsStream(configProperties.getAdminKeyStore())
        ){
            Wallet.Identity user = Wallet.Identity.createIdentity(configProperties.getMspId(), new InputStreamReader(adminSignCertInputStream),new InputStreamReader(adminKeyStoreInputStream));
            wallet.put("admin", user);
            ClassPathResource classPathResource = new ClassPathResource(configProperties.getResourceName());

            Gateway.Builder builder = Gateway.createBuilder();

            builder.identity(wallet, "admin").networkConfig(classPathResource.getInputStream()).discovery(true);

            return builder;
        }
    }
}
