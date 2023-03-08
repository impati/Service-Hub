package com.example.servicehub.support;

import com.example.servicehub.config.JpaConfig;
import com.example.servicehub.config.ProjectTestConfig;
import com.example.servicehub.config.TestJpaConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.annotation.Inherited;


@Import({TestJpaConfig.class,ProjectTestConfig.class, DefaultImageResizer.class})
@DataJpaTest
public class ImageResizerTest{

    @Autowired private ImageResizer imageResizer;

    @Test
    @DisplayName("250 * 250 보다 작은 이미지 리사이즈 테스트")
    public void lessThanStandardSizeImageResizeTest() throws Exception{
        String fileName = "test.png";
        Assertions.assertThat(imageResizer.resizeImageAndSave(fileName))
                .isEqualTo(fileName);

    }

    @Test
    @DisplayName("이미지 리사이즈 테스트")
    @Commit
    public void imageResizeTest() throws Exception{
        String fileName = "bigtest.png";
        Assertions.assertThat(imageResizer.resizeImageAndSave(fileName))
                .isEqualTo(fileName);

    }

}
