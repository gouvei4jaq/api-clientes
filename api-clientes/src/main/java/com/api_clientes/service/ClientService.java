package com.api_clientes.service;

import com.api_clientes.request.ClientRequest;
import com.api_clientes.entity.ClientEntity;
import com.api_clientes.exception.ClientNotFoundException;
import com.api_clientes.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient (ClientRequest clientRequest){
        return clientRepository.save(ClientEntity.valueOf(clientRequest));
    }

    public ClientEntity findClientById (Long id){
        return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public ClientEntity updateClientById (Long id, ClientRequest clientRequest){
        return clientRepository.findById(id).map(clientEntity -> {
            clientEntity.updateClient(clientRequest);
            return clientEntity;
        }).orElseThrow(() -> new ClientNotFoundException("404.000"));
    }

    public void deleteClientById(Long id){
        clientRepository.deleteById(id);
    }

}
