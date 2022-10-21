package com.kgr.echoTrust.config;

import com.kgr.echoTrust.core.EchoTrustService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
@Slf4j
@Data
public class AutoConfiguration {


    @Value("${resource-name}")
    private String resourceName;


    @Value("${organizations.bigdataorg.admin.sign-certs}")
    private String adminSignCerts;

    @Value("${organizations.bigdataorg.admin.key-store}")
    private String adminKeyStore;

    @Value("${organizations.bigdataorg.mspid}")
    private String mspId;



    @Bean
    public Gateway.Builder builder() throws IOException {
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallet.createFileSystemWallet(walletPath);

        try (
                InputStream adminSignCertInputStream  = this.getClass().getResourceAsStream(adminSignCerts);
                InputStream adminKeyStoreInputStream = this.getClass().getResourceAsStream(adminKeyStore)
        ){
            Wallet.Identity user = Wallet.Identity.createIdentity(mspId, new InputStreamReader(adminSignCertInputStream),new InputStreamReader(adminKeyStoreInputStream));
            wallet.put("admin", user);
            ClassPathResource classPathResource = new ClassPathResource(resourceName);

            Gateway.Builder builder = Gateway.createBuilder();

            builder.identity(wallet, "admin").networkConfig(classPathResource.getInputStream()).discovery(true);

            return builder;
        }
    }

    @Bean
    public EchoTrustService echoTrustService(Gateway.Builder builder) {
        return new EchoTrustService(builder);
    }
}
