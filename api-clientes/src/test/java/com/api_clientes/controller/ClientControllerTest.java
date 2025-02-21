package com.api_clientes.controller;

import com.api_clientes.entity.ClientEntity;
import com.api_clientes.request.ClientRequest;
import com.api_clientes.service.ClientService;
import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.exception.DuplicateCpfException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    private static final String POST_CREATE_CLIENT = "/clientes";
    private static final String CLIENT_WITH_VALID_ID = "/clientes/1";
    private static final String CLIENT_WITH_INVALID_ID = "/clientes/99";
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientRequest clientRequest;
    private ClientEntity clientEntity;
    private ResultActions resultActions;


    @Test
    public void shouldCreateClientSuccessfully() throws Exception {
        givenAValidClientRequest();
        whenCallCreateClient();
        thenExpect201Created();
    }

    @Test
    public void shouldThrowExceptionWhenDuplicateCpf() throws Exception {
        givenAClientRequestWithDuplicateCpf();
        whenCallCreateClient();
        thenExpect422UnprocessableEntity();
    }

    @Test
    public void shouldFindClientSuccessfully() throws Exception {
        givenAValidClientId();
        whenCallFindClient();
        thenExpect200OkWithClientData();
    }

    @Test
    public void shouldThrowExceptionWhenClientNotFound() throws Exception {
        givenAnInvalidClientId();
        whenCallFindClientWithInvalidID();
        thenExpect404NotFound();
    }

    @Test
    public void shouldUpdateClientSuccessfully() throws Exception {
        givenAValidUpdateRequest();
        whenCallUpdateClient();
        thenExpect200OkWithUpdatedClientData();
    }

    @Test
    public void shouldThrowExceptionWhenClientNotFoundOnUpdate() throws Exception {
        givenAValidUpdateRequest();
        givenClientNotFoundOnUpdate();
        whenCallUpdateClientAndThrowsNotFoundException();
        thenExpectClientNotFoundExceptionOnUpdate();
    }

    @Test
    public void shouldDeleteClientSuccessfully() throws Exception {
        givenAValidClientIdForDelete();
        whenCallDeleteClient();
        thenExpect204NoContent();
    }

    @Test
    public void shouldThrowExceptionWhenClientNotFoundOnDelete() throws Exception {
        givenAnInvalidClientIdForDelete();
        whenCallDeleteClientWithIdIncorrect();
        thenExpect404NotFound();
    }

    /*
     * Given Methods
     */

    private void givenAValidClientRequest() {
        clientRequest = new ClientRequest();
        clientRequest.setNome("Jaque Gouveia");
        clientRequest.setCpf("98765432100");
        clientRequest.setEmail("jaque.gouveia@gmail.com");
        clientRequest.setEndereco("Rua Teste - Campinas SP");
        clientRequest.setDataNascimento(LocalDate.of(1995, 11, 11));
        clientRequest.setTelefone("19996062332");
        clientRequest.setSaldo(BigDecimal.valueOf(6000));

        when(clientService.createClient(any(ClientRequest.class))).thenReturn(clientEntity);
    }

    private void givenAClientRequestWithDuplicateCpf() {
        givenAValidClientRequest();
        doThrow(new DuplicateCpfException("422.003"))
                .when(clientService).createClient(any(ClientRequest.class));
    }

    private void givenAValidClientId() {
        clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setNome("Jaque Gouveia");

        when(clientService.findClientById(1L)).thenReturn(clientEntity);
    }

    private void givenAnInvalidClientId() {
        when(clientService.findClientById(99L))
                .thenThrow(new ClientNotFoundException("404.000"));
    }

    private void givenAValidUpdateRequest() {
        clientRequest = new ClientRequest();
        clientRequest.setNome("Jaque Souza");
        clientRequest.setCpf("98765432100");
        clientRequest.setEmail("jaque.souza@gmail.com");
        clientRequest.setEndereco("Av. Nova - São Paulo SP");
        clientRequest.setDataNascimento(LocalDate.of(1995, 11, 11));
        clientRequest.setTelefone("11987654321");
        clientRequest.setSaldo(BigDecimal.valueOf(8000));

        clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setNome("Jaque Souza");

        when(clientService.updateClientById(eq(1L), any(ClientRequest.class)))
                .thenReturn(clientEntity);
    }


    private void givenClientNotFoundOnUpdate() {
        doThrow(new ClientNotFoundException("404.000"))
                .when(clientService)
                .updateClientById(99L, clientRequest);
    }

    private void givenAValidClientIdForDelete() {
        doNothing().when(clientService).deleteClientById(1L);
    }

    private void givenAnInvalidClientIdForDelete() {
        doThrow(new ClientNotFoundException("404.000"))
                .when(clientService).deleteClientById(99L);
    }

    /*
     * When Methods
     */

    private void whenCallCreateClient() throws Exception {
        resultActions = mockMvc.perform(post(POST_CREATE_CLIENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequest)));
    }

    private void whenCallFindClient() throws Exception {
        resultActions = mockMvc.perform(get(CLIENT_WITH_VALID_ID));
    }

    private void whenCallFindClientWithInvalidID() throws Exception {
        resultActions = mockMvc.perform(get(CLIENT_WITH_INVALID_ID));
    }

    private void whenCallUpdateClient() throws Exception {
        resultActions = mockMvc.perform(put(CLIENT_WITH_VALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequest)));
    }

    private void whenCallDeleteClient() throws Exception {
        resultActions = mockMvc.perform(delete(CLIENT_WITH_VALID_ID));
    }

    private void whenCallDeleteClientWithIdIncorrect() throws Exception {
        resultActions = mockMvc.perform(delete(CLIENT_WITH_INVALID_ID));
    }
    private void whenCallUpdateClientAndThrowsNotFoundException() throws Exception {
        resultActions = mockMvc.perform(put(CLIENT_WITH_INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clientRequest)));
    }

    /*
     * Then Methods
     */

    private void thenExpect201Created() throws Exception {
        resultActions.andExpect(status().isCreated());
    }

    private void thenExpect422UnprocessableEntity() throws Exception {
        resultActions.andExpect(status().isUnprocessableEntity());
    }

    private void thenExpect200OkWithClientData() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Jaque Gouveia"));
    }

    private void thenExpect404NotFound() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    private void thenExpect200OkWithUpdatedClientData() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Jaque Souza"));
    }

    private void thenExpect204NoContent() throws Exception {
        resultActions.andExpect(status().isNoContent());
    }

    private void thenExpectClientNotFoundExceptionOnUpdate() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

}
