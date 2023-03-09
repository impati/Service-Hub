package com.example.servicehub.web.controller;

import com.example.servicehub.config.TestSecurityConfig;
import com.example.servicehub.domain.Client;
import com.example.servicehub.domain.ProviderType;
import com.example.servicehub.dto.ClickServiceDto;
import com.example.servicehub.dto.ClientEditForm;
import com.example.servicehub.dto.CustomServiceForm;
import com.example.servicehub.dto.ServiceSearchConditionForm;
import com.example.servicehub.service.CategoryAdminister;
import com.example.servicehub.service.ClientAdminister;
import com.example.servicehub.service.ClientServiceAdminister;
import com.example.servicehub.service.CustomServiceAdminister;
import com.example.servicehub.util.FormDataEncoder;
import com.example.servicehub.web.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@DisplayName("ClientController 테스트")
@Import(TestSecurityConfig.class)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryAdminister categoryAdminister;

    @MockBean
    private ClientServiceAdminister clientServiceAdminister;

    @MockBean
    private ClientAdminister clientAdminister;

    @MockBean
    private CustomServiceAdminister customServiceAdminister;

    private FormDataEncoder formDataEncoder = new FormDataEncoder();

    @Test
    @DisplayName("클라이언트 페이지 테스트")
    @WithMockCustomUser
    public void givenClientPageURLWithoutServiceName_whenEnterClientPage_thenRenderClientPage() throws Exception{

        BDDMockito.given(categoryAdminister.getAllCategories())
                        .willReturn(allCategories());

        BDDMockito.given(clientServiceAdminister.servicesOfClient(1L,ServiceSearchConditionForm.of(allCategories(), null)))
                        .willReturn(serviceWithClick());

        BDDMockito.given(clientAdminister.findClientByClientId(1L))
                        .willReturn(subClient());

        mockMvc.perform(MockMvcRequestBuilders.get("/client/{clientId}",1L))
                .andExpect(MockMvcResultMatchers.model().attributeExists("serviceWithClick","allCategories","simpleClient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("client/client-page"));

    }

    private List<String> allCategories(){
        return List.of("IT","BLOG","JOB");
    }

    private List<ClickServiceDto> serviceWithClick(){
        return
              List.of(
                      new ClickServiceDto(1L,1L,"test","test","test","test"),
                      new ClickServiceDto(100L,2L,"test","test","test","test")
              );

    }

    private Client subClient(){
        return Client.of(
                "test","test",
                "test","test",
                "ROLE_ADMIN",ProviderType.KEYCLOAK,
                "test","test"
                );
    }

    @Test
    @DisplayName("클라이언트 프로필 수정 페이지 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenClientProfileEditURL_whenEnterClientProfileEditPage_thenRenderClientProfileEditPage() throws Exception{

        BDDMockito.given(clientAdminister.findClientByClientId(2L)).willReturn(subClient());

        mockMvc.perform(MockMvcRequestBuilders.get("/client/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("clientEditForm"))
                .andExpect(MockMvcResultMatchers.view().name("client/client-edit-page"));

    }

    @Test
    @DisplayName("클라이언트 프로필 수정 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenClientEditForm_whenValidFormAndEditingClient_thenEditClient() throws Exception{

        ClientEditForm clientEditForm = new ClientEditForm("edit User", "hi ", "https://notion.so");

        BDDMockito.willDoNothing().given(clientAdminister).editClientProfile(2L,clientEditForm);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/client/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(formDataEncoder.encode(clientEditForm,ClientEditForm.class))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/" + 2));

    }

    @Test
    @DisplayName("사용자 서비스 수정 페이지 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenClientServiceEditPageURL_whenClickServiceEditButton_theRenderClientServiceEditPage() throws Exception {


        BDDMockito.given(categoryAdminister.getAllCategories()).willReturn(allCategories());

        BDDMockito.given(clientServiceAdminister.servicesOfClient(2L,ServiceSearchConditionForm.of(allCategories(),null)))
                .willReturn(serviceWithClick());

        BDDMockito.given(clientAdminister.findClientByClientId(2L))
                .willReturn(subClient());


        mockMvc.perform(MockMvcRequestBuilders.get("/client/service/edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("serviceWithClick","allCategories","simpleClient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("client/client-service-edit"));

    }

    @Test
    @DisplayName("사용자 서비스 수정 - 삭제 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenDeleteButtonInClientServiceEditPage_whenClickDeleteButton_thenDeleteClientService() throws Exception{

        BDDMockito.willDoNothing().given(clientServiceAdminister).deleteClientService(2L,99L,false);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/client/service/delete/{serviceId}",99L)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Ok"));

    }


    @Test
    @DisplayName("사용자가 서비스를 클릭 - 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenClientServiceInClientPage_whenClickService_thenCallClickServiceMethod() throws Exception{

        String serviceUrl = "https://notion.so";

        BDDMockito.given(clientServiceAdminister.countClickAndReturnUrl(2L,99L,false))
                        .willReturn(serviceUrl);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/client/click")
                        .param("serviceId","99")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:" + serviceUrl));
    }

    @Test
    @DisplayName("서비스 검색 페이지에서 바로 서비스 등록 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenServiceSearchPage_whenClickServiceAddButton_thenAddClientService() throws Exception{

        BDDMockito.willDoNothing().given(clientServiceAdminister).addClientService(2L,99L);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/client/add-service/{serviceId}",99L)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Ok"));


        BDDMockito.then(clientServiceAdminister).should().addClientService(2L,99L);
    }


    @Test
    @DisplayName("서비스 검색 페이지에서 바로 서비스 삭제 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenServiceSearchPage_whenClickServiceDeleteButton_thenDeleteClientService() throws Exception{


        BDDMockito.willDoNothing().given(clientServiceAdminister).deleteClientService(2L,99L,false);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/client/delete-service/{serviceId}",99L)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Ok"));


        BDDMockito.then(clientServiceAdminister).should().deleteClientService(2L,99L,false);
    }

    @Test
    @DisplayName("사용자 페이지에서 커스텀 서비스 추가 테스트")
    @WithMockCustomUser
    public void givenClickButtonAboutCustomService_whenInputCustomServiceUrlAndName_thenAddCustomServiceOfClient() throws Exception{

        BDDMockito.willDoNothing().given(customServiceAdminister)
                .addCustomService(1L,new CustomServiceForm("serviceHub","https://service-hub.org"));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/client/add-custom")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("serviceName","serviceHub")
                                .param("serviceUrl","https://service-hub.org")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Ok"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("사용자가 커스텀 서비스를 클릭 - 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenCustomServiceInClientPage_whenClickService_thenCallClickServiceMethod() throws Exception{

        String serviceUrl = "https://service-hub.org";

        BDDMockito.given(clientServiceAdminister.countClickAndReturnUrl(2L,99L,true))
                .willReturn(serviceUrl);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/client/click")
                                .param("serviceId","99")
                                .param("isCustom","true")
                                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:" + serviceUrl));
    }

    @Test
    @DisplayName("커스텀 서비스 수정 - 삭제 테스트")
    @WithMockCustomUser(id = 2L)
    public void givenDeleteButtonInClientServiceEditPage_whenClickDeleteButton_thenDeleteCustomService() throws Exception{

        BDDMockito.willDoNothing().given(clientServiceAdminister).deleteClientService(2L,99L,true);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/client/service/delete/{serviceId}",99L)
                                .param("isCustom","true")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().string("Ok"));

    }












}