package com.example.servicehub.web.controller.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.servicehub.support.file.FileManager;
import com.example.servicehub.support.file.FileType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class ResourceController {

	private final Map<String, FileManager> fileManagers;

	@GetMapping(value = "/{fileType}/{filename}")
	public ResponseEntity<Resource> findLogo(
		@PathVariable final FileType fileType,
		@PathVariable final String filename
	) throws IOException {
		final String inputFile = fileManagers.get(fileType.getType()).getFullPath(filename);
		final Path path = new File(inputFile).toPath();
		final FileSystemResource resource = new FileSystemResource(path);
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(Files.probeContentType(path)))
			.body(resource);
	}
}
