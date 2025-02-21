package com.api_clientes.service;

import com.api_clientes.exception.ClientUnderageException;
import com.api_clientes.exception.DuplicateCpfException;
import com.api_clientes.request.ClientRequest;
import com.api_clientes.entity.ClientEntity;
import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient (ClientRequest clientRequest){
        validateAge(clientRequest.getDataNascimento());
        try {
            return clientRepository.save(ClientEntity.valueOf(clientRequest));
        }catch (DataIntegrityViolationException ex){
            throw new DuplicateCpfException("422.003");
        }
    }

    public ClientEntity findClientById (Long id){
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public ClientEntity updateClientById (Long id, ClientRequest clientRequest){
        validateAge(clientRequest.getDataNascimento());
        return clientRepository.findById(id).map(clientEntity -> {
            clientEntity.updateClient(clientRequest);
            return clientRepository.save(clientEntity);
        }).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public void deleteClientById(Long id){
        ClientEntity client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("404.000"));
        clientRepository.delete(client);
    }

    public void validateAge(LocalDate birthDate){
        if (Period.between(birthDate, LocalDate.now()).getYears() < 18){
            throw new ClientUnderageException("400.000");
        }
    }

}
