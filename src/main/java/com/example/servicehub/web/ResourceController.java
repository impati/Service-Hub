package com.example.servicehub.web;


import com.example.servicehub.support.FileManager;
import com.example.servicehub.support.FileType;
import com.example.servicehub.support.LogoManager;
import com.example.servicehub.support.ProfileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ResourceController {

    private final Map<String, FileManager> fileManagers;

    @GetMapping(value = "/{fileType}/{filename}")
    public ResponseEntity<Resource> findLogo(@PathVariable FileType fileType, @PathVariable String filename) throws IOException {
        String inputFile = fileManagers.get(fileType.getType()).getFullPath(filename);
        Path path = new File(inputFile).toPath();
        FileSystemResource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                .body(resource);
    }

}
