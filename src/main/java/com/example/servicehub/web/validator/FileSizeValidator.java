package com.example.servicehub.web.validator;

import com.example.servicehub.web.validator.annotation.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private static final Integer MB = 1024 * 1024;
    private long maxSizeMB;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        maxSizeMB = constraintAnnotation.maxSizeInMB();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file == null || file.isEmpty()) return true;
        return file.getSize() <= maxSizeMB * MB;
    }
}
