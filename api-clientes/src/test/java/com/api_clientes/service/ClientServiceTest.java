package com.api_clientes.service;

import com.api_clientes.entity.ClientEntity;
import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.exception.ClientUnderageException;
import com.api_clientes.exception.DuplicateCpfException;
import com.api_clientes.repository.ClientRepository;
import com.api_clientes.request.ClientRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private ClientRequest clientRequest;
    private ClientEntity clientEntity;

    @Test
    public void shouldCreateClientSuccessfully() {
        givenAValidRequestBody();
        whenCallCreateClient();
        thenExpectClientCreated();
    }

    @Test
    public void shouldThrowExceptionWhenDuplicateCpf() {
        givenAValidRequestBody();
        whenCallCreateClientAndThrowsDuplicateCpfException();
        thenExpectDuplicateCpfException();
    }

    @Test
    public void shouldFindClientSuccessfully() {
        givenAValidClientId();
        whenCallFindClientById();
        thenExpectClientEntity();
    }

    @Test
    public void shouldThrowExceptionWhenClientNotFound() {
        givenAnInvalidClientId();
        whenCallFindClientByIdAndThrowsNotFoundException();
        thenExpectClientNotFoundException();
    }

    @Test
    public void shouldUpdateClientSuccessfully() {
        givenAValidClientId();
        whenCallUpdateClient();
        thenExpectClientUpdated();
    }

    @Test
    public void shouldThrowExceptionWhenUpdatingNonExistentClient() {
        givenAValidRequestBody();
        givenAnInvalidClientId();
        whenCallUpdateClientAndThrowsNotFoundException();
        thenExpectClientNotFoundException();
    }

    @Test
    public void shouldDeleteClientSuccessfully() {
        givenAValidClientId();
        whenCallDeleteClient();
        thenExpectClientDeleted();
    }

    @Test
    public void shouldThrowExceptionWhenDeletingNonExistentClient() {
        givenAnInvalidClientId();
        whenCallDeleteClientAndThrowsNotFoundException();
        thenExpectClientNotFoundException();
    }

    @Test
    public void shouldThrowExceptionWhenClientUnderage() {
        givenAnUnderageClientRequest();
        whenCallCreateClientAndThrowsUnderageException();
        thenExpectUnderageException();
    }

    @Test
    public void shouldThrowExceptionWhenClientIsUnderageInValidation() {
        assertThrows(ClientUnderageException.class, () -> clientService.validateAge(LocalDate.now().minusYears(17)));
    }

    private void givenAValidRequestBody() {
        clientRequest = new ClientRequest();
        clientRequest.setNome("Jaque Gouveia");
        clientRequest.setCpf("98765432100");
        clientRequest.setEmail("jaque.gouveia@gmail.com");
        clientRequest.setEndereco("Rua Teste - Campinas SP");
        clientRequest.setDataNascimento(LocalDate.of(1995, 11, 11));
        clientRequest.setTelefone("19996062332");
        clientRequest.setSaldo(BigDecimal.valueOf(6000));

        clientEntity = ClientEntity.valueOf(clientRequest);
        clientEntity.setId(1L);
    }

    private void givenAValidClientId() {
        givenAValidRequestBody();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientEntity));
    }

    private void givenAnInvalidClientId() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
    }

    private void givenAnUnderageClientRequest() {
        clientRequest = new ClientRequest();
        clientRequest.setCpf("12345678901");
        clientRequest.setDataNascimento(LocalDate.of(2023, 11, 11));
    }

    private void whenCallCreateClient() {
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        clientService.createClient(clientRequest);
    }

    private void whenCallCreateClientAndThrowsDuplicateCpfException() {
        when(clientRepository.save(any(ClientEntity.class))).thenThrow(new DuplicateCpfException("422.003"));
    }

    private void whenCallFindClientById() {
        clientEntity = clientService.findClientById(1L);
    }

    private void whenCallFindClientByIdAndThrowsNotFoundException() {
        assertThrows(ClientNotFoundException.class, () -> clientService.findClientById(99L));
    }

    private void whenCallUpdateClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(clientEntity));
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        clientService.updateClientById(1L, clientRequest);
    }

    private void whenCallUpdateClientAndThrowsNotFoundException() {
        assertThrows(ClientNotFoundException.class, () -> clientService.updateClientById(99L, clientRequest));
    }

    private void whenCallDeleteClient() {
        clientService.deleteClientById(1L);
    }

    private void whenCallDeleteClientAndThrowsNotFoundException() {
        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(99L));
    }

    private void whenCallCreateClientAndThrowsUnderageException() {
        assertThrows(ClientUnderageException.class, () -> clientService.createClient(clientRequest));
    }

    private void thenExpectClientCreated() {
        verify(clientRepository).save(any(ClientEntity.class));
    }

    private void thenExpectDuplicateCpfException() {
        assertThrows(DuplicateCpfException.class, () -> clientService.createClient(clientRequest));
    }

    private void thenExpectClientEntity() {
        assertNotNull(clientEntity);
        assertEquals(1L, clientEntity.getId());
    }

    private void thenExpectClientNotFoundException() {
        verify(clientRepository).findById(anyLong());
    }

    private void thenExpectClientUpdated() {
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(any(ClientEntity.class));
    }

    private void thenExpectClientDeleted() {
        verify(clientRepository).delete(any(ClientEntity.class));
    }

    private void thenExpectUnderageException() {
        verify(clientRepository, never()).save(any(ClientEntity.class));
    }
}