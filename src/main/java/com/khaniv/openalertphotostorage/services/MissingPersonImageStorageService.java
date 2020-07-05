package com.khaniv.openalertphotostorage.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.khaniv.openalertphotostorage.converters.MultipartFileToFileConverter;
import com.khaniv.openalertphotostorage.dto.MissingPersonImageData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissingPersonImageStorageService {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${amazon.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final MultipartFileToFileConverter multipartFileToFileConverter;
    private AmazonS3 amazonClient;

    @PostConstruct
    private void initializeAmazonClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonClient = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public PutObjectResult uploadImage(MissingPersonImageData missingPersonImageData, MultipartFile image) {
        checkBucketExists();
        return amazonClient.putObject(bucket, missingPersonImageData.toString(), multipartFileToFileConverter.convert(image));
    }

    public DeleteObjectsResult deleteAllByMissingPersonId(UUID id) {
        checkBucketExists();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(
                findObjectsWithPrefix(id + MissingPersonImageData.SLASH).getObjectSummaries()
                        .stream()
                        .map(S3ObjectSummary::getKey)
                        .map(DeleteObjectsRequest.KeyVersion::new)
                        .collect(Collectors.toList()));

        return amazonClient.deleteObjects(deleteObjectsRequest);
    }

    public DeleteObjectsResult delete(MissingPersonImageData missingPersonImageData) {
        checkBucketExists();
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucket).withKeys(missingPersonImageData.toString());
        return amazonClient.deleteObjects(request);
    }

    public ListObjectsV2Result findObjectsWithPrefix(String prefix) {
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix);
        return amazonClient.listObjectsV2(listObjectsRequest);
    }

    private void checkBucketExists() {
        if (!amazonClient.doesBucketExistV2(bucket))
            throw new RuntimeException("Bucket with name " + bucket + " does not exist! Try changing bucket name in properties file.");
    }

}
