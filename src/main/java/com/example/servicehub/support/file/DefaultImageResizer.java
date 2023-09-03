package com.example.servicehub.support.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DefaultImageResizer implements ImageResizer {

	private static final int NEW_WIDTH = 250;
	private static final int NEW_HEIGHT = 250;

	@Value("${logo.dir}")
	public String logoDir;

	@Override
	public String resizeImageAndSave(final String imageName) {
		try {
			final BufferedImage inputImage = ImageIO.read(getInputFile(imageName));
			final BufferedImage outputImage = new BufferedImage(NEW_WIDTH, NEW_HEIGHT, inputImage.getType());
			final Graphics2D graphics = outputImage.createGraphics();
			graphics.drawImage(inputImage, 0, 0, NEW_WIDTH, NEW_HEIGHT, null);
			graphics.dispose();
			return saveAndReturnStoreName(outputImage, imageName);
		} catch (IOException e) {
			log.error("파일 사이즈를 재조정하는데 실패 했습니다.", e);
			return imageName;
		}
	}

	private File getInputFile(final String imageName) {
		return new File(getPullPath(imageName));
	}

	private String saveAndReturnStoreName(final BufferedImage outputImage, final String imageName) throws IOException {
		final File outputFile = new File(getPullPath(imageName));
		ImageIO.write(outputImage, ImageExtension.getExtension(imageName), outputFile);
		return imageName;
	}

	private String getPullPath(final String imageName) {
		return logoDir + imageName;
	}
}
