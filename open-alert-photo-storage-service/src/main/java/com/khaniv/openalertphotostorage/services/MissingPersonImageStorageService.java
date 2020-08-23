package com.khaniv.openalertphotostorage.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertphotostorage.converters.MissingPersonImageToFileConverter;
import com.khaniv.openalertphotostorage.converters.S3ObjectToMissingPersonImageConverter;
import com.khaniv.openalertphotostorage.utils.MissingPersonImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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

    private final MissingPersonImageToFileConverter missingPersonImageToFileConverter;
    private final S3ObjectToMissingPersonImageConverter s3ObjectToMissingPersonImageConverter;

    private AmazonS3 amazonClient;

    @PostConstruct
    private void initializeAmazonClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonClient = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public MissingPersonImageDto findMissingPersonImage(MissingPersonImageDto missingPersonImageData) throws IOException {
        checkBucketExists();
        S3Object object = amazonClient.getObject(bucket, MissingPersonImageUtils.getFullName(missingPersonImageData));
        return s3ObjectToMissingPersonImageConverter.convert(object);
    }

    public PutObjectResult uploadImage(MissingPersonImageDto missingPersonImage) {
        checkBucketExists();
        return amazonClient.putObject(bucket, MissingPersonImageUtils.getFullName(missingPersonImage), missingPersonImageToFileConverter.convert(missingPersonImage));
    }

    public DeleteObjectsResult deleteAllByMissingPersonId(UUID id) {
        checkBucketExists();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(
                findObjectsWithPrefix(id + MissingPersonImageUtils.SLASH).getObjectSummaries()
                        .stream()
                        .map(S3ObjectSummary::getKey)
                        .map(DeleteObjectsRequest.KeyVersion::new)
                        .collect(Collectors.toList()));

        return amazonClient.deleteObjects(deleteObjectsRequest);
    }

    public DeleteObjectsResult delete(MissingPersonImageDto missingPersonImage) {
        checkBucketExists();
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucket).withKeys(MissingPersonImageUtils.getFullName(missingPersonImage));
        return amazonClient.deleteObjects(request);
    }

    private ListObjectsV2Result findObjectsWithPrefix(String prefix) {
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request().withBucketName(bucket).withPrefix(prefix);
        return amazonClient.listObjectsV2(listObjectsRequest);
    }

    private void checkBucketExists() {
        if (!amazonClient.doesBucketExistV2(bucket))
            throw new RuntimeException("Bucket with name " + bucket + " does not exist! Try changing bucket name in properties file.");
    }
}
