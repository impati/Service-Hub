package com.example.servicehub.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.example.servicehub.web.validator.annotation.FileSize;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private static final Integer MB = 1024 * 1024;
    private long maxSizeMB;

    @Override
    public void initialize(final FileSize constraintAnnotation) {
        maxSizeMB = constraintAnnotation.maxSizeInMB();
    }

    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        return file.getSize() <= maxSizeMB * MB;
    }
}
