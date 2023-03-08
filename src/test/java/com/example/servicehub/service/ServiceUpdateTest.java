package com.example.servicehub.service;

import com.example.servicehub.config.TestJpaConfig;
import com.example.servicehub.domain.Category;
import com.example.servicehub.domain.ServiceCategory;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.ServiceUpdateForm;
import com.example.servicehub.repository.ServicesRepository;
import com.example.servicehub.support.DefaultImageResizer;
import com.example.servicehub.support.LogoManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("서비스 수정 테스트")
@DataJpaTest
@Import({TestJpaConfig.class,ServiceUpdate.class, LogoManager.class, DefaultImageResizer.class})
class ServiceUpdateTest {

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ServiceUpdate serviceUpdate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("서비스 정보 업데이트 - 서비스 이름 업데이트")
    public void givenServiceUpdateForm_whenUpdatingServiceName_thenUpdateService() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        // when
        String updateServiceName = "노션 아니다.";
        updateForm.setServiceName(updateServiceName);
        serviceUpdate.update(updateForm);
        // then
        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();
        assertThat(updatedServices.getServiceName())
                .isEqualTo(updateServiceName);
    }

    @Test
    @DisplayName("서비스 정보 업데이트 - 카테고리 업데이트 추가 ")
    public void givenServiceUpdateForm_whenAddingAndUpdatingServiceCategories_thenUpdateService() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        // when

        List<String> serviceCategoryNames = convertToCategoryNamesFrom(services.getServiceCategories());
        serviceCategoryNames.add("entertainment");
        updateForm.setCategoryNames(serviceCategoryNames);
        serviceUpdate.update(updateForm);
        // then

        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();
        List<String> updatedServiceCategoryNames = convertToCategoryNamesFrom(updatedServices.getServiceCategories());
        assertThat(updatedServiceCategoryNames.containsAll(serviceCategoryNames))
                .isTrue();
    }

    @Test
    @DisplayName("서비스 정보 업데이트 - 카테고리 업데이트 삭제")
    public void givenServiceUpdateForm_whenDeletingAndUpdatingServiceCategories_thenUpdateService() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        // when

        List<String> serviceCategoryNames = convertToCategoryNamesFrom(services.getServiceCategories());
        serviceCategoryNames.remove(0);
        updateForm.setCategoryNames(serviceCategoryNames);
        serviceUpdate.update(updateForm);
        // then

        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();
        List<String> updatedServiceCategoryNames = convertToCategoryNamesFrom(updatedServices.getServiceCategories());
        assertThat(updatedServiceCategoryNames.containsAll(serviceCategoryNames))
                .isTrue();
        assertThat(updatedServiceCategoryNames.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("서비스 정보 업데이트 - 카테고리 업데이트 교체")
    public void givenServiceUpdateForm_whenExchangingAndUpdatingServiceCategories_thenUpdateService() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        // when

        List<String> serviceCategoryNames = convertToCategoryNamesFrom(services.getServiceCategories());
        serviceCategoryNames.remove("IT");
        serviceCategoryNames.add("entertainment");
        updateForm.setCategoryNames(serviceCategoryNames);
        serviceUpdate.update(updateForm);
        // then

        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();
        List<String> updatedServiceCategoryNames = convertToCategoryNamesFrom(updatedServices.getServiceCategories());

        assertThat(updatedServiceCategoryNames.containsAll(serviceCategoryNames))
                .isTrue();

        assertThat(updatedServiceCategoryNames.size()).isEqualTo(2);

        assertThat(updatedServiceCategoryNames
                .stream()
                .anyMatch(ele->ele.contains("IT"))).isFalse();

        assertThat(updatedServiceCategoryNames
                .stream()
                .anyMatch(ele->ele.contains("entertainment"))).isTrue();

    }

    private List<String> convertToCategoryNamesFrom(List<ServiceCategory> serviceCategories){
        return serviceCategories
                .stream()
                .map(ServiceCategory::getCategory)
                .map(Category::getName).collect(toList());
    }

    @Test
    @DisplayName("서비스 정보 업데이트 - 로고 업데이트")
    public void givenServiceUpdateForm_whenUpdatingLogoFile_thenUpdateService() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        String originStoreName = services.getLogoStoreName();
        // when
        MockMultipartFile mockMultipartFile = new MockMultipartFile("test",
                "test.png",
                "image/png",
                new FileInputStream("/Users/jun-yeongchoe/Desktop/project/ServiceHub/src/main/resources/image/default.png"));
        updateForm.setLogoFile(mockMultipartFile);
        serviceUpdate.update(updateForm);

        // then

        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();

        assertThat(updatedServices.getLogoStoreName()).isNotNull();
        assertThat(updatedServices.getLogoStoreName()).isNotEqualTo(originStoreName);

    }

    @Test
    @DisplayName("서비스 정보 업데이트 - 로고 업데이트 하지 않기")
    public void givenServiceFormWithNoLogoFile_whenNonUpdatingLogoFile_thenNothing() throws Exception{
        // given
        Services services = servicesRepository.findById(1L).get();
        ServiceUpdateForm updateForm = ServiceUpdateForm.from(services);
        String originStoreName = services.getLogoStoreName();
        // when
        serviceUpdate.update(updateForm);
        // then
        entityManager.flush();
        entityManager.clear();

        Services updatedServices = servicesRepository.findById(1L).get();
        assertThat(updatedServices.getLogoStoreName()).isNotNull();
        assertThat(updatedServices.getLogoStoreName()).isEqualTo(originStoreName);

    }
}