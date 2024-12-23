package org.mediabox.mediabox.controller.file;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.dto.file.FileResponse;
import org.mediabox.mediabox.dto.file.Info;
import org.mediabox.mediabox.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestHeader("Authorization") String token) {
        return fileService.upload(file, token);
    }

    @GetMapping
    public Info getAllImages(@RequestHeader("Authorization") String token) {
        return fileService.getInfo(token);
    }

    @GetMapping("/photos")
    public List<FileResponse> getAllPhotos(@RequestHeader("Authorization") String token) {
        return fileService.getAllPhotos(token);
    }

    @GetMapping("/videos")
    public List<FileResponse> getAllVideos(@RequestHeader("Authorization") String token) {
        return fileService.getAllVideos(token);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable("id") Long id) {
        fileService.delete(id);
    }
}
