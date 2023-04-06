package com.example.servicehub.support;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ImageExtensionTest {

    private static Stream<Arguments> extractExtension() {
        return Stream.of(
                Arguments.of("file.png", ".png"),
                Arguments.of("file.PNG", ".png"),
                Arguments.of("file.dib", ".dib"),
                Arguments.of("file.png.jpeg", ".jpeg"),
                Arguments.of("file.raw", ".raw"),
                Arguments.of("file.tiff", ".tiff"),
                Arguments.of("file.tif", ".tif"),
                Arguments.of("file.gif", ".gif"),
                Arguments.of("file.jpg", ".jpg")
        );
    }

    @ParameterizedTest
    @MethodSource("extractExtension")
    @DisplayName("이미지 파일에서 확장자 분리")
    public void extractExtensionTest(String file, String extension) throws Exception {

        Assertions.assertThat(ImageExtension.extractExtension(file))
                .isEqualTo(extension);

    }

    @Test
    @DisplayName("지원하는 파일 확장자가 없는 경우")
    public void notFoundExtension() throws Exception {
        assertThat(ImageExtension.extractExtension("file.aaa")).isEqualTo(".png");
    }


}