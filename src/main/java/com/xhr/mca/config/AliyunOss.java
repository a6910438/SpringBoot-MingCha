package com.xhr.mca.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;

import lombok.Data;

@Component
@Data
public class AliyunOss {

	@Value("${aliyun.oss.endpoint}")
	private String endpoint;
	@Value("${aliyun.oss.bucketName}")
	private String bucketName;
	@Value("${aliyun.oss.url}")
	private String url;
	@Value("${aliyun.accessKeyId}")
	private String accessKeyId;
	@Value("${aliyun.accessKeySecret}")
	private String accessKeySecret;

	/***
	 * 头像上传到阿里云oss
	 * 
	 * @param request 内含字节流
	 * @param folder  目录
	 * @param uid
	 * @throws IOException
	 */
	public void uploadImage(MultipartFile file, String folder, String uid) throws IOException {
		if (!folder.contains("/"))
			folder += "/";
		InputStream is = file.getInputStream();
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ossClient.putObject(new PutObjectRequest(bucketName, folder + uid + ".png", is));
		ossClient.shutdown();
	}

	public void uploadImageBase64(String base64, String folder, String uid) throws IOException {
		if (!folder.contains("/"))
			folder += "/";
		InputStream is = new ByteArrayInputStream(Base64.decodeBase64(base64));
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		ossClient.putObject(new PutObjectRequest(bucketName, folder + uid + ".png", is));
		ossClient.shutdown();
	}

}
