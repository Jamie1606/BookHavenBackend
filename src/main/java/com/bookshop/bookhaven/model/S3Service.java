// Author		: Zay Yar Tun
// Admin No		: 2235035
// Class		: DIT/FT/2A/02
// Group		: 10
// Date			: 16.7.2023
// Description	: to upload image and delete image

package com.bookshop.bookhaven.model;

import java.io.InputStream;
import java.net.URI;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3Service {
	private final S3Client s3client;
	private static final String ACCESS_POINT = "arn:aws:s3:us-east-1:553479871319:accesspoint/bookhavenimagesgroup10";
	
	public S3Service(String accesskey, String secretkey, String sessiontoken) {
		this.s3client = S3Client.builder()
				.region(Region.US_EAST_1)
				.credentialsProvider(StaticCredentialsProvider.create(AwsSessionCredentials.create(accesskey, secretkey, sessiontoken)))
				.endpointOverride(URI.create("https://s3-accesspoint.us-east-1.amazonaws.com"))
				.build();
	}
	
	public boolean uploadImage(String key, InputStream inputstream, long filesize) {
		PutObjectResponse response = s3client.putObject(PutObjectRequest.builder()
				.bucket(ACCESS_POINT)
				.key(key)
				.build(), RequestBody.fromInputStream(inputstream, filesize));
		if(response.sdkHttpResponse().isSuccessful()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean deleteImage(String key) {
		DeleteObjectResponse response = s3client.deleteObject(DeleteObjectRequest.builder()
					.bucket(ACCESS_POINT)
					.key(key)
					.build());
		if(response.sdkHttpResponse().isSuccessful()) {
			return true;
		}
		else {
			return false;
		}
	}
}