package org.mediabox.mediabox.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mediabox.mediabox.dto.file.FileResponse;
import org.mediabox.mediabox.dto.file.Info;
import org.mediabox.mediabox.entity.Extension;
import org.mediabox.mediabox.entity.File;
import org.mediabox.mediabox.entity.User;
import org.mediabox.mediabox.exceptions.ImageUploadException;
import org.mediabox.mediabox.mapper.FileMapper;
import org.mediabox.mediabox.prop.MinioProperties;
import org.mediabox.mediabox.repository.FileRepository;
import org.mediabox.mediabox.security.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;
    private final FileRepository fileRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final FileMapper fileMapper;

    public String upload(MultipartFile file, String token) {
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
        Long userId = getUserId(token);

        String name = file.getOriginalFilename();
        String ext = generateExtension(file);
        Long size = file.getSize();
        LocalDateTime uploadDate = LocalDateTime.now();

        User user = userService.getUserById(userId);

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ImageUploadException("Image upload failed");
        }
        saveImage(inputStream, fileName);

        File saveFile = new File();
        saveFile.setName(name);
        saveFile.setType(ext);
        saveFile.setSize(size);
        saveFile.setPath(fileName);
        saveFile.setUploadedAt(uploadDate);
        saveFile.setUser(user);

        fileRepository.save(saveFile);

        return fileName;
    }

    private Long getUserId(String token) {
        token = token.substring(7);

        return Long.valueOf(jwtTokenProvider.getId(token));
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

    public List<FileResponse> getAllMedia(String token) {
        Long userId = getUserId(token);
        List<File> userFiles = fileRepository.findByUserId(userId);

        if (userFiles.isEmpty()) return null;

        List<FileResponse> files = userFiles
                .stream().map(fileMapper::toDto)
                .toList();

        for (FileResponse file : files) {
            file.setPath(generatePresignedUrl(file.getPath()));
        }

        return files;
    }

    public Info getInfo(String token) {
        Long userId = getUserId(token);
        String username = userService.getUserById(userId).getUsername();

        List<FileResponse> fileResponses = getAllMedia(token);

        return Info.builder()
                .username(username)
                .files(fileResponses)
                .build();
    }

    public List<FileResponse> getAllPhotos(String token) {
        return getFilesByExtension(token, Extension.JPG, Extension.JPEG);
    }

    public List<FileResponse> getAllVideos(String token) {
        return getFilesByExtension(token, Extension.MP4);
    }

    private List<FileResponse> getFilesByExtension(String token, Extension... extensions) {
        List<FileResponse> allFiles = getAllMedia(token);

        if (allFiles == null) return null;
        Set<String> allowedExtensions = Arrays.stream(extensions)
                .map(Extension::name)
                .map(String::toUpperCase)
                .collect(Collectors.toSet());

        return allFiles.stream()
                .filter(file -> {
                    String fileName = file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();

                    return allowedExtensions.contains(extension);
                })
                .toList();
    }

    @SneakyThrows
    private String generatePresignedUrl(String fileName) {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .method(Method.GET)
                .build());
        return url;
    }

    @SneakyThrows
    public void delete(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(file.getPath())
                .build());

        fileRepository.delete(file);
    }
}