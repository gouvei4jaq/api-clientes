package com.api_clientes.service;

import com.api_clientes.entity.ClientEntity;
import com.api_clientes.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public ClientEntity createClient (@Valid ClientEntity clientEntity){
        return clientRepository.save(clientEntity);
    }

    public Optional<ClientEntity> findClientById (Long id){
        return clientRepository.findById(id);
    }

    @Transactional
    public ClientEntity updateClientById (Long id, @Valid ClientEntity updateClient){
        return clientRepository.findById(id).map(clientEntity -> {
            clientEntity.setName(updateClient.getName());
            clientEntity.setCpf(updateClient.getCpf());
            clientEntity.setEmail(updateClient.getEmail());
            clientEntity.setDateOfBirth(updateClient.getDateOfBirth());
            clientEntity.setCellPhone(updateClient.getCellPhone());
            clientEntity.setBalance(updateClient.getBalance());
            return clientEntity;
        }).orElseThrow(() -> new EntityNotFoundException("Cliente Não Encontrado."));
    }

    @Transactional
    public void deleteClientById(Long id){
        clientRepository.deleteById(id);
    }

}
