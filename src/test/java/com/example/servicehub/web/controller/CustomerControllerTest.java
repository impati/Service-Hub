package com.example.servicehub.web.controller;

import com.example.servicehub.service.*;
import com.example.servicehub.web.WithMockCustomUser;
import com.example.servicehub.web.dto.CustomerEditForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

        BDDMockito.given(categoryAdminister.getAllCategories())
                .willReturn(List.of("IT"));

        Long customerId = 1L;
        mockMvc.perform(get("/customer/{customerId}", customerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(model().attributeExists("serviceWithClick", "allCategories", "simpleClient"))
                .andExpect(view().name("customer/customer-page"))
                .andDo(print());

        BDDMockito.then(categoryAdminister).should().getAllCategories();
    }

    @Test
    @DisplayName("사용자 정보 수정 페이지 테스트")
    @WithMockCustomUser(id = 1L)
    public void customerEditPageTest() throws Exception {

        mockMvc.perform(get("/customer/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("renderClientProfileEditPage"))
                .andExpect(model().attributeExists("customerEditForm"))
                .andExpect(view().name("customer/customer-edit-page"))
                .andDo(print());

    }

    @Test
    @DisplayName("사용자 정보 수정 테스트 - 정상 입력")
    @WithMockCustomUser(id = 1L)
    public void customerEditTest() throws Exception {

        CustomerEditForm editForm = new CustomerEditForm("test", "hi", "https://impati.github.io/");

        BDDMockito.willDoNothing().given(customerEditor)
                .edit(editForm);


        mockMvc.perform(post("/customer/edit")
                        .queryParam("introComment", editForm.getIntroComment())
                        .queryParam("blogUrl", editForm.getBlogUrl())
                        .queryParam("nickname", editForm.getNickname())
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.handler().methodName("editClientProfile"))
                .andExpect(view().name("redirect:/customer/" + 1))
                .andDo(print());

    }

}