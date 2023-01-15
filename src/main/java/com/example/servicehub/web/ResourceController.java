package com.example.servicehub.web;

import com.example.servicehub.service.fileUtils.LogoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class ResourceController {
    private final LogoManager logoManager;

    @GetMapping(value = "/logo/{filename}")
    public ResponseEntity<Resource> findLogo(@PathVariable String filename) throws IOException {
        String inputFile = logoManager.getFullPath(filename);
        Path path = new File(inputFile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);
    }
}
