package com.example.servicehub.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;

import com.example.servicehub.config.ProjectTestConfig;
import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.support.file.DefaultImageResizer;
import com.example.servicehub.support.file.ImageResizer;

@Import({
	TestJpaConfig.class,
	ProjectTestConfig.class,
	DefaultImageResizer.class
})
@DataJpaTest
class ImageResizerTest {

	@Autowired
	private ImageResizer imageResizer;

	@Test
	@DisplayName("250 * 250 보다 작은 이미지 리사이즈 테스트")
	void lessThanStandardSizeImageResizeTest() {
		String fileName = "test.png";

		assertThat(imageResizer.resizeImageAndSave(fileName))
			.isEqualTo(fileName);
	}

	@Test
	@DisplayName("이미지 리사이즈 테스트")
	@Commit
	void imageResizeTest() {
		String fileName = "bigtest.png";

		assertThat(imageResizer.resizeImageAndSave(fileName))
			.isEqualTo(fileName);
	}
}
