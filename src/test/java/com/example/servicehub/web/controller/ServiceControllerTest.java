package com.example.servicehub.web.controller;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.domain.Services;
import com.example.servicehub.dto.*;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ServiceSearch;
import com.example.servicehub.service.ServiceUpdate;
import com.example.servicehub.service.ServicesRegister;
import com.example.servicehub.support.MetaDataCrawler;
import com.example.servicehub.support.ServiceMetaData;
import com.example.servicehub.util.FormDataEncoder;
import com.example.servicehub.web.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@DisplayName("ServiceController 테스트")
@Import({TestSecurityConfig.class})
@WebMvcTest(ServiceController.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MetaDataCrawler metaDataCrawler;

    @MockBean
    private CategoryAdminister categoryAdminister;

    @MockBean
    private ServicesRegister servicesRegister;

    @MockBean
    private ServiceSearch serviceSearch;

    @MockBean
    private ServiceUpdate serviceUpdate;

    private final FormDataEncoder formDataEncoder = new FormDataEncoder();


    @Test
    @DisplayName("서비스 등록 페이지 테스트")
    @WithMockCustomUser(id = 2L,role = "ADMIN")
    public void givenServiceRegisterURL_whenEnterServiceRegisterPage_thenRenderServiceRegisterPage() throws Exception{

        ServiceMetaData serviceMetaData = getNotion();
        BDDMockito.given(metaDataCrawler.tryToGetMetaData(serviceMetaData.getUrl()))
                .willReturn(serviceMetaData);

        BDDMockito.given(categoryAdminister.getAllCategories())
                .willReturn(List.of("IT","BLOG"));


        mockMvc.perform(
                    MockMvcRequestBuilders.get("/service/registration")
                            .param("serviceUrl",serviceMetaData.getUrl())
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("serviceRegisterForm","categories"))
                .andExpect(MockMvcResultMatchers.view().name("service/registration"));

    }

    @Test
    @DisplayName("서비스 등록 테스트")
    @WithMockCustomUser(id = 2L,role = "ADMIN")
    public void givenServiceData_whenRegisteringService_thenRegisterService() throws Exception{


        ServicesRegisterForm servicesRegisterForm = notionForm();


        BDDMockito.willDoNothing().given(servicesRegister)
                .registerServices(servicesRegisterForm);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/service/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(servicesRegisterForm,ServicesRegisterForm.class))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andDo(MockMvcResultHandlers.print());

        BDDMockito.then(servicesRegister).should().registerServices(servicesRegisterForm);
    }

    @Test
    @DisplayName("서비스 업데이트 페이지 테스트")
    @WithMockCustomUser(id = 2L,role = "ADMIN")
    public void givenServiceUpdateURL_whenEnterServiceUpdatePage_thenRenderServiceUpdatePage() throws Exception{

        BDDMockito.given(serviceSearch.search(1L))
                .willReturn(getService());

        mockMvc.perform(MockMvcRequestBuilders.get("/service/1/update"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("serviceUpdateForm","categories"))
                .andExpect(MockMvcResultMatchers.view().name("service/service-update"));
    }

    @Test
    @DisplayName("서비스 업데이트 테스트")
    @WithMockCustomUser(id = 2L,role = "ADMIN")
    public void givenServiceData_whenUpdatingService_thenUpdateService() throws Exception{


        ServiceUpdateForm serviceUpdateForm = ServiceUpdateForm.builder()
                .serviceId(1L)
                .serviceName("노션")
                .serviceUrl("https://notion.so")
                .description("노션입니다")
                .categoryNames(List.of("IT","BLOG"))
                .title("노션")
                .build();


        mockMvc.perform(
                        MockMvcRequestBuilders.post("/service/1/update")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(formDataEncoder.encode(serviceUpdateForm, ServiceUpdateForm.class))
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/service/1"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("서비스 정보 페이지 테스트")
    @WithMockCustomUser(id = 1L)
    public void givenServicePageURL_whenEnterServicePage_thenRenderServicePage() throws Exception{

        BDDMockito.given(serviceSearch.searchSingleService(1L, Optional.ofNullable(1L)))
                .willReturn(SingleServiceWithCommentsDto.of(getService(),true,List.of()));

        mockMvc.perform(MockMvcRequestBuilders.get("/service/{serviceId}",1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("singleServiceWithCommentsDto"))
                .andExpect(MockMvcResultMatchers.view().name("service/service-page"));

    }


    @Test
    @DisplayName("서비스 검색 테스트")
    @WithMockCustomUser(id = 1L)
    public void givenServiceSearchCondition_whenSearchingService_thenResultService() throws Exception{
        ServiceSearchConditionForm serviceSearchConditionForm = ServiceSearchConditionForm.of(List.of("IT","BLOG"),"노션");

        BDDMockito.given(serviceSearch.search(serviceSearchConditionForm,Optional.of(1L), PageRequest.of(0,10)))
                        .willReturn(new PageImpl<>(List.of(new PopularityServiceDto(
                                1999L,"노션","default.png","https://notion.so",
                                "노션",1L
                        ))));

        mockMvc.perform(
                    MockMvcRequestBuilders.get("/service/search")
                            .param("serviceName",serviceSearchConditionForm.getServiceName())
                            .param("categories",serviceSearchConditionForm.getCategories().get(0))
                            .param("categories",serviceSearchConditionForm.getCategories().get(1))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("firstPage","currentPage","endPage",
                        "searchedServices","serviceSearchConditionForm","categories"))
                .andExpect(MockMvcResultMatchers.view().name("service/search"))
                .andDo(MockMvcResultHandlers.print());

    }


    private Services getService(){
        return Services.of(
                "노션","default.png",
                "https://notion.so","노션",
                "노션은 블로그입니다."
                );
    }

    private static ServicesRegisterForm notionForm() {
        return ServicesRegisterForm.of(
                List.of("IT", "BLOG"), "노션", "https://notion.so", "노션입니다.",
                "노션은 블로그입니다.", "https://www.notion.so/front-static/meta/default.png");
    }


    private static ServiceMetaData getNotion() {
        return new ServiceMetaData("노션", "블로그", "https://notion.so",
                "https://www.notion.so/front-static/meta/default.png", "블로그입니다");
    }


}