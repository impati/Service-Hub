package com.example.servicehub.support.file;

import java.util.Arrays;

public enum ImageExtension {

    PNG("png"),
    BMP("bmp"),
    RLE("rle"),
    DIB("dib"),
    JPEG("jpeg"),
    JPG("jpg"),
    GIF("gif"),
    TIF("tif"),
    TIFF("tiff"),
    RAW("raw");


    private final String name;

    ImageExtension(String name) {
        this.name = name;
    }

    public static String extractExtension(String fileName) {
        return Arrays.stream(ImageExtension.values())
                .filter(imageExtension -> fileName.toLowerCase().contains(imageExtension.name))
                .map(imageExtension -> "." + imageExtension.name)
                .reduce((first, second) -> second).orElse(".png");
    }

    public static String getExtension(String fileName) {
        return Arrays.stream(ImageExtension.values())
                .filter(imageExtension -> fileName.toLowerCase().contains(imageExtension.name))
                .map(imageExtension -> imageExtension.name)
                .reduce((first, second) -> second).orElse("png");
    }

}
