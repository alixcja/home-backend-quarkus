package de.explore.grabby.fileservice.service;

import de.explore.grabby.booking.rest.request.EntityUploadForm;
import de.explore.grabby.fileservice.exception.BucketCreationFailedException;
import de.explore.grabby.fileservice.exception.FileFetchFailedException;
import de.explore.grabby.fileservice.exception.FileUploadFailedException;
import de.explore.grabby.lunch.rest.request.MenuUploadForm;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@ApplicationScoped
public class FileService {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);
  private static final long maxPartSize = 5L * 1024 * 1024 * 1024;

  @Inject
  MinioClient minioClient;

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
    LOG.info("Uploading file {} to bucket {}", fileName, bucket);
    try (FileInputStream fis = new FileInputStream(file)) {
      PutObjectArgs args = PutObjectArgs.builder()
              .bucket(bucket)
              .object(fileName)
              .stream(fis, file.length(), maxPartSize)
              .build();

      minioClient.putObject(args);

      LOG.info("Upload completed for file {}", fileName);
    } catch (IOException e) {
      Thread.currentThread().interrupt();
      throw new FileUploadFailedException(e);
    } catch (MinioException e) {
      throw new FileUploadFailedException(e);
    }
  }

  public void ensureBucketExists(String bucket) {
    if (bucketDoesNotExists(bucket)) {
      LOG.info("Bucket {} does not exists - will create it", bucket);
      LOG.info("Creating new bucket {}", bucket);
      createBucket(bucket);
    } else {
      LOG.info("Bucket {} does exists", bucket);
    }
  }

  private void createBucket(String bucket) {
    try {
      minioClient.makeBucket(createMakeBucketArgs(bucket));
    } catch (MinioException e) {
      throw new BucketCreationFailedException(e);
    }
  }

  private static MakeBucketArgs createMakeBucketArgs(String bucket) {
    return new MakeBucketArgs.Builder().bucket(bucket).build();
  }

  private boolean bucketDoesNotExists(String bucketName) {
    BucketExistsArgs bucketExistsArgs = new BucketExistsArgs.Builder().bucket(bucketName).build();
    try {
      boolean exists = minioClient.bucketExists(bucketExistsArgs);
      return !exists;
    } catch (MinioException e) {
      throw new RuntimeException(e);
    }
  }

  public InputStream getImage(String bucket, String fileName) {
    if (bucketDoesNotExists(bucket)) {
      throw new IllegalArgumentException("Provided bucket does not exist");
    }
    GetObjectArgs getObjectArgs = createGetObjectArgs(bucket, fileName);
    try {
      return minioClient.getObject(getObjectArgs);
    } catch (MinioException e) {
      throw new FileFetchFailedException(e);
    }
  }

  private static GetObjectArgs createGetObjectArgs(String bucket, String fileName) {
    return new GetObjectArgs.Builder().bucket(bucket).object(fileName).build();
  }
}
