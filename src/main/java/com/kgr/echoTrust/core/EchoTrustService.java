package com.kgr.echoTrust.core;

import lombok.AllArgsConstructor;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

/**
 * @author kgr
 * @create 2022-10-21 11:11
 */

@AllArgsConstructor
public class EchoTrustService {

    /**
     * 这个builder需要关闭，每次都用try with resource自动关
     */
    @Resource
    Gateway.Builder builder;


    /**
     * 提交交易到区块链
     * @param networkName   名称
     * @param contractName  合约
     * @param functionName  合约方法
     * @param params        方法参数
     * @return              上链结果字符串
     * @throws ContractException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public String  submitTransaction(String networkName, String contractName, String functionName, String params) throws ContractException, InterruptedException, TimeoutException {

        try (Gateway gateway = builder.connect()) {
            Network network = gateway.getNetwork(networkName);
            Contract contract = network.getContract(contractName);
            return new String(contract.submitTransaction(functionName, params));
        }
    }


    /**
     * 查询区块链数据
     * @param networkName  名称
     * @param contractName 合约
     * @param functionName 合约方法
     * @param params 方法参数
     * @return json格式string串
     * @throws ContractException
     */
    public String evaluateTransaction(String networkName, String contractName, String functionName, String params) throws ContractException {


        try (Gateway gateway = builder.connect()) {
            Network network = gateway.getNetwork(networkName);
            Contract contract = network.getContract(contractName);

            byte[] result = contract.evaluateTransaction(functionName, params);

            return new String(result);
        }
    }
}
