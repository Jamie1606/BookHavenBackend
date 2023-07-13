// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 10.7.2023
// Description	: this is to connect to s3 and upload image

package com.bookshop.bookhaven.model;

import java.io.InputStream;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3Service {
	private final S3Client s3client;
	
	public S3Service() {
		this.s3client = S3Client.builder()
				.region(Region.AP_SOUTHEAST_1)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("AKIA33YTKOYCXCNNBLFK", "CfcqjxjeeoAATXOU0J53LeVF59ynxHA8mSQRohc1")))
				.build();
	}
	
	public boolean uploadImage(String key, InputStream inputstream, long filesize) {
		PutObjectResponse response = s3client.putObject(PutObjectRequest.builder()
				.bucket("jadbookhaven10")
				.key(key)
				.build(), RequestBody.fromInputStream(inputstream, filesize));
		if(response.sdkHttpResponse().isSuccessful()) {
			return true;
		}
		else {
			return false;
		}
	}
}