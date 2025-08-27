package de.explore.grabby.fileservice.service;

import de.explore.grabby.booking.rest.request.EntityUploadForm;
import de.explore.grabby.fileservice.exception.FileUploadFailedException;
import de.explore.grabby.lunch.rest.request.MenuUploadForm;
import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class FileService {
  private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

  @Inject
  MinioAsyncClient minioClient;

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
    LOG.info("Uploading file {} to bucket {}", bucket, fileName);
    try {
      minioClient.putObject(createPutObjectArg(bucket, fileName, file));
    } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
             NoSuchAlgorithmException | XmlParserException e) {
      throw new FileUploadFailedException(e);
    }
  }

  private PutObjectArgs createPutObjectArg(String bucket, String fileName, File file) {
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      return PutObjectArgs.builder()
              .bucket(bucket)
              .object(fileName)
              .stream(fileInputStream, -1, -1)
              .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void ensureBucketExists(String bucket) {
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
    } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
             NoSuchAlgorithmException | XmlParserException e) {
      throw new RuntimeException(e);
    }
  }

  private static MakeBucketArgs createMakeBucketArgs(String bucket) {
    return new MakeBucketArgs.Builder().bucket(bucket).build();
  }

  private boolean bucketDoesNotExists(String bucketName) {
    BucketExistsArgs bucketExistsArgs = new BucketExistsArgs.Builder().bucket(bucketName).build();
    try {
      CompletableFuture<Boolean> future = minioClient.bucketExists(bucketExistsArgs);
      boolean exists = future.get();
      return !exists;
    } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
             NoSuchAlgorithmException | XmlParserException | ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public InputStream getImage(String bucket, String fileName) {
    if (bucketDoesNotExists(bucket)) {
      throw new IllegalArgumentException("Provided bucket does not exist");
    }
    GetObjectArgs getObjectArgs = createGetObjectArgs(bucket, fileName);
    try (InputStream is = minioClient.getObject(getObjectArgs).get()
    ) {
      return is;
    } catch (InsufficientDataException | InternalException | InvalidKeyException | IOException |
             NoSuchAlgorithmException | XmlParserException | ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static GetObjectArgs createGetObjectArgs(String bucket, String fileName) {
    return new GetObjectArgs.Builder().bucket(bucket).object(fileName).build();
  }
}
