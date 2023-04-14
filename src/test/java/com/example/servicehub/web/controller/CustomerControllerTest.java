package com.example.servicehub.web.controller;

import com.example.servicehub.dto.custom.CustomServiceForm;
import com.example.servicehub.service.category.CategoryAdminister;
import com.example.servicehub.service.customer.CustomerCustomServiceAdminister;
import com.example.servicehub.service.customer.CustomerEditor;
import com.example.servicehub.service.customer.CustomerServiceAdminister;
import com.example.servicehub.service.customer.CustomerServiceSearch;
import com.example.servicehub.service.services.ServiceClickCounter;
import com.example.servicehub.web.WithMockCustomUser;
import com.example.servicehub.web.controller.customer.CustomerController;
import com.example.servicehub.web.dto.customer.CustomerEditForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("사용자 컨트롤러 테스트") //TODO : 컨트롤러 분리
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerCustomServiceAdminister customerCustomServiceAdminister;
    @MockBean
    private CustomerServiceAdminister customerServiceAdminister;
    @MockBean
    private CategoryAdminister categoryAdminister;
    @MockBean
    private ServiceClickCounter serviceClickCounter;
    @MockBean
    private CustomerServiceSearch customerServiceSearch;
    @MockBean
    private CustomerEditor customerEditor;

    @Test
    @DisplayName("사용자 페이지 테스트")
    @WithMockCustomUser(id = 1L)
    public void customerPageTest() throws Exception {

        given(categoryAdminister.getAllCategories())
                .willReturn(List.of("IT"));

        Long customerId = 1L;
        mockMvc.perform(get("/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("serviceWithClick", "allCategories", "simpleCustomer"))
                .andExpect(view().name("customer/customer-page"))
                .andDo(print());

        then(categoryAdminister).should().getAllCategories();
    }

    @Test
    @DisplayName("사용자 정보 수정 페이지 테스트")
    @WithMockCustomUser(id = 1L)
    public void customerEditPageTest() throws Exception {

        mockMvc.perform(get("/customer/edit"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("rendercustomerProfileEditPage"))
                .andExpect(model().attributeExists("customerEditForm"))
                .andExpect(view().name("customer/customer-edit-page"))
                .andDo(print());

    }

    @Test
    @DisplayName("사용자 정보 수정 테스트 - 정상 입력")
    @WithMockCustomUser(id = 1L)
    public void customerEditTest() throws Exception {

        CustomerEditForm editForm = new CustomerEditForm("test", "hi", "https://impati.github.io/");

        willDoNothing().given(customerEditor)
                .edit(editForm);


        mockMvc.perform(post("/customer/edit")
                        .queryParam("introComment", editForm.getIntroComment())
                        .queryParam("blogUrl", editForm.getBlogUrl())
                        .queryParam("nickname", editForm.getNickname())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("editcustomerProfile"))
                .andExpect(view().name("redirect:/customer/" + 1))
                .andDo(print());

    }

    @Test
    @DisplayName("사용자 서비스 수정 페이지 테스트")
    @WithMockCustomUser
    public void customerServiceEditPageTest() throws Exception {

        given(categoryAdminister.getAllCategories())
                .willReturn(List.of("IT", "BLOG"));

        mockMvc.perform(get("/customer/service/edit"))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("renderCustomerServiceEdit"))
                .andExpect(model().attributeExists("serviceWithClick", "allCategories", "simpleCustomer"))
                .andExpect(view().name("customer/customer-service-edit"))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 서비스 삭제 API 테스트 - 커스텀 서비스")
    @WithMockCustomUser(id = 2L)
    public void deleteCustomerServiceTestIncludeCustomService() throws Exception {

        willDoNothing().given(customerCustomServiceAdminister).deleteCustomService(2L, 2L);

        mockMvc.perform(post("/customer/service/delete/{serviceId}", 2)
                        .param("isCustom", "true")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCustomerService"))
                .andExpect(MockMvcResultMatchers.content().string("OK"))
                .andDo(print());

        then(customerCustomServiceAdminister).should().deleteCustomService(2L, 2L);
    }

    @Test
    @DisplayName("사용자 서비스 추가 API 테스트")
    @WithMockCustomUser(id = 2L)
    public void addCustomerServiceTest() throws Exception {

        willDoNothing().given(customerServiceAdminister).addCustomerService(2L, 2L);

        mockMvc.perform(post("/customer/add-service/{serviceId}", 2)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addCustomerService"))
                .andExpect(MockMvcResultMatchers.content().string("OK"))
                .andDo(print());

        then(customerServiceAdminister).should().addCustomerService(2L, 2L);

    }

    @Test
    @DisplayName("사용자 서비스 삭제 API 테스트")
    @WithMockCustomUser(id = 2L)
    public void deleteCustomerServiceTest() throws Exception {
        willDoNothing().given(customerServiceAdminister).deleteCustomerService(2L, 2L);

        mockMvc.perform(post("/customer/delete-service/{serviceId}", 2)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCustomerService"))
                .andExpect(MockMvcResultMatchers.content().string("OK"))
                .andDo(print());

        then(customerServiceAdminister).should().deleteCustomerService(2L, 2L);

    }

    @Test
    @DisplayName("커스텀 서비스 추가 API 테스트")
    @WithMockCustomUser(id = 3L)
    public void addCustomServiceTest() throws Exception {

        CustomServiceForm form = CustomServiceForm.builder()
                .serviceUrl("https://service-hub.org")
                .serviceName("서비스 허브")
                .build();

        willDoNothing().given(customerCustomServiceAdminister).addCustomService(3L, form);

        mockMvc.perform(post("/customer/add-custom")
                        .param("serviceName", form.getServiceName())
                        .param("serviceUrl", form.getServiceUrl())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addCustomService"))
                .andExpect(MockMvcResultMatchers.content().string("OK"))
                .andDo(print());

        then(customerCustomServiceAdminister).should().addCustomService(3L, form);
    }

    @Test
    @DisplayName("서비스 클릭에 대한 테스트")
    @WithMockCustomUser(id = 3L)
    public void clickServiceTest() throws Exception {

        String serviceUrl = "https://service-hub.org";

        given(customerCustomServiceAdminister.countClickAndReturnUrl(3L, 3L))
                .willReturn(serviceUrl);

        mockMvc.perform(get("/customer/click")
                        .param("isCustom", "true")
                        .param("serviceId", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().methodName("clickService"))
                .andExpect(view().name("redirect:" + serviceUrl));

        then(customerCustomServiceAdminister).should()
                .countClickAndReturnUrl(3L, 3L);
    }

}