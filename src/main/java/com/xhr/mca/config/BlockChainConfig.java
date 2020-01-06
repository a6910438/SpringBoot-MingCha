package com.xhr.mca.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import okhttp3.OkHttpClient;

@Configuration
public class BlockChainConfig {

	@Value("${ethernet.rpc.url}")
	private String ethRpcUrl;
	@Value("${ethernet.privateKey.file}")
	private String file;

	@Bean
	public Web3j web3j() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		builder.readTimeout(30 * 1000, TimeUnit.MILLISECONDS);
		OkHttpClient httpClient = builder.build();
		Web3j web3j = Web3j.build(new HttpService(ethRpcUrl, httpClient, false));
		return web3j;
	}

	@Bean
	public Credentials credentials() throws IOException, CipherException {
		Credentials credentials = WalletUtils.loadCredentials("", file);
		return credentials;
	}

}
