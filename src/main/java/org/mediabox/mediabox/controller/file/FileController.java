package org.mediabox.mediabox.controller.file;

import lombok.RequiredArgsConstructor;
import org.mediabox.mediabox.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
}
