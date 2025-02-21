package com.api_clientes.service;

import com.api_clientes.exception.ClientUnderageException;
import com.api_clientes.exception.DuplicateCpfException;
import com.api_clientes.request.ClientRequest;
import com.api_clientes.entity.ClientEntity;
import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient (ClientRequest clientRequest){

        log.info("Creating client with CPF: {}****", clientRequest.getCpf().substring(0, 4));

        validateAge(clientRequest.getDataNascimento());

        try {
            ClientEntity client = clientRepository.save(ClientEntity.valueOf(clientRequest));
            log.info("Client created successfully with ID: {}", client.getId());
            return client;
        }catch (DataIntegrityViolationException ex){
            log.error("Duplicate CPF found: {}", clientRequest.getCpf().substring(0,4));
            throw new DuplicateCpfException("422.003");
        }
    }

    public ClientEntity findClientById (Long id){
        log.info("Searching for client with ID: {}", id);
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public ClientEntity updateClientById (Long id, ClientRequest clientRequest){
        log.info("Updating client with ID: {}", id);
        validateAge(clientRequest.getDataNascimento());

        return clientRepository.findById(id).map(clientEntity -> {
            clientEntity.updateClient(clientRequest);
            log.info("Client updated successfully!");
            return clientRepository.save(clientEntity);

        }).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public void deleteClientById(Long id){
        log.info("Trying delete client with ID: {}", id);
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("404.000"));
        clientRepository.delete(client);
        log.info("Client with ID: {} deleted successfully", id);
    }

    public void validateAge(LocalDate birthDate){
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18){
            throw new ClientUnderageException("400.000");
        }
    }

}
