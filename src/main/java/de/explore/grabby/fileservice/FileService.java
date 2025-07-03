package de.explore.grabby.fileservice;

import de.explore.grabby.booking.rest.request.EntityUploadForm;
import de.explore.grabby.lunch.rest.request.MenuUploadForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.UUID;

@ApplicationScoped
public class FileService {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

  @Inject
  S3Client s3;

  public String uploadImage(String bucket, EntityUploadForm uploadForm) {
    ensureBucketExists(bucket);
    String fileName = UUID.randomUUID().toString();
    createRequestAndUploadImage(bucket, uploadForm.file, fileName);
    return fileName;
  }

  public String uploadImage(String bucket, MenuUploadForm uploadForm) {
    ensureBucketExists(bucket);
    String fileName = UUID.randomUUID().toString();
    createRequestAndUploadImage(bucket, uploadForm.file, fileName);
    return fileName;
  }

  private void createRequestAndUploadImage(String bucket, File file, String fileName) {
    PutObjectRequest putObjectRequest = createPutObjectRequest(bucket, fileName);
    LOG.info("Uploading file {} to bucket {}", bucket, fileName);
    s3.putObject(putObjectRequest, RequestBody
            .fromFile(file));
  }

  private PutObjectRequest createPutObjectRequest(String bucket, String fileName) {
    return PutObjectRequest.builder()
            .bucket(bucket)
            .key(fileName)
            .build();
  }

  private void ensureBucketExists(String bucket) {
    if (bucketDoesNotExists(bucket)) {
      CreateBucketRequest createBucketRequest = createCreateBucketRequest(bucket);
      LOG.info("Creating new bucket {}", bucket);
      s3.createBucket(createBucketRequest);
    }
  }

  private CreateBucketRequest createCreateBucketRequest(String bucket) {
    return CreateBucketRequest.builder()
            .bucket(bucket)
            .build();
  }

  private boolean bucketDoesNotExists(String bucketName) {
    // TODO: refactor this ._.
    return s3.listBuckets().buckets().stream().filter(b -> b.name().equals(bucketName)).toList().isEmpty();
  }

  public ResponseInputStream<GetObjectResponse> getImage(String bucket, String fileName) {
    if (bucketDoesNotExists(bucket)) {
      // TODO: refactor it
      throw new BadRequestException("Provided bucket does not exist");
    }
    GetObjectRequest getObjectRequest = createGetObjectRequest(bucket, fileName);
    return s3.getObject(getObjectRequest);
  }

  private GetObjectRequest createGetObjectRequest(String bucket, String fileName) {
    return GetObjectRequest.builder().bucket(bucket).key(fileName).build();
  }
}
