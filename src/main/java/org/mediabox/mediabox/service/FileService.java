package org.mediabox.mediabox.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mediabox.mediabox.exceptions.ImageUploadException;
import org.mediabox.mediabox.prop.MinioProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new ImageUploadException("Image upload failed");
        }
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ImageUploadException("Image upload failed");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed");
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = generateExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String generateExtension(MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}

