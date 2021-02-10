package com.redhat.uxl.services.service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.commonjava.utils.StrUtils;
import com.redhat.uxl.services.service.AmazonS3Service;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Amazon s 3 service.
 */
@Service
@Slf4j
public class AmazonS3ServiceImpl implements AmazonS3Service {
  @Value("${app.aws.s3.accessKey}")
  private String appAwsS3AccessKey;

  @Value("${app.aws.s3.bucket}")
  private String appAwsS3Bucket;

  @Value("${app.aws.s3.endpoint}")
  private String appAwsS3Endpoint;

  @Value("${app.aws.s3.environment}")
  private String appAwsS3Environment;

  @Value("${app.aws.s3.secretKey}")
  private String appAwsS3SecretKey;

  @Value("${app.aws.s3.urlPrefix}")
  private String appAwsS3UrlPrefix;

  private static StaticCredentialsProvider credentialsProviderChain;

    /**
     * Init.
     */
    @PostConstruct
  public void init() {
    credentialsProviderChain = new StaticCredentialsProvider(
        new BasicAWSCredentials(appAwsS3AccessKey, appAwsS3SecretKey));
  }

  @Override
  @Timed
  public String makeExternalURL(String urlPart) {
    StringBuilder sb = new StringBuilder();

    sb.append(appAwsS3UrlPrefix);

    if (StrUtils.isNotBlank(appAwsS3Endpoint)) {
      sb.append(".").append(appAwsS3Endpoint);
    }

    if (StrUtils.isNotBlank(appAwsS3Environment)) {
      sb.append('/').append(appAwsS3Environment);
    }

    sb.append(urlPart);

    return sb.toString();
  }

  @Override
  @Timed
  public void uploadFile(MultipartFile file, String filename)
      throws InterruptedException, IOException {
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      TransferManager transferManager =
          new TransferManager(credentialsProviderChain.getCredentials());

      transferManager.getAmazonS3Client().setEndpoint(appAwsS3Endpoint);

      if (StrUtils.isNotBlank(file.getContentType())) {
        metadata.setContentType(file.getContentType());
      }

      if (file.getSize() > 0) {
        metadata.setContentLength(file.getSize());
      }

      PutObjectRequest putObjectRequest = new PutObjectRequest(appAwsS3Bucket,
          appAwsS3Environment + filename, file.getInputStream(), metadata);

      putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all

      Upload upload = transferManager.upload(putObjectRequest);

      upload.waitForCompletion();
      transferManager.shutdownNow();
    } catch (Exception e) {
      throw e;
    }
  }
}
