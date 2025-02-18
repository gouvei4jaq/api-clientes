package com.api_clientes.service;

import com.api_clientes.dto.ClientDTO;
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
    public ClientEntity createClient (@Valid ClientDTO clientDTO){
        ClientEntity clientEntity = ClientEntity.builder()
                .cpf(clientDTO.getCpf())
                .email(clientDTO.getEmail())
                .name(clientDTO.getName())
                .dateOfBirth(clientDTO.getDateOfBirth())
                .cellPhone(clientDTO.getCellPhone())
                .address(clientDTO.getAddress())
                .balance(clientDTO.getBalance())
                .build();
        return clientRepository.save(clientEntity);
    }

    public Optional<ClientEntity> findClientById (Long id){
        return clientRepository.findById(id);
    }

    @Transactional
    public ClientEntity updateClientById (Long id, @Valid ClientDTO clientDTO){
        return clientRepository.findById(id).map(clientEntity -> {
            clientEntity.setName(clientDTO.getName());
            clientEntity.setCpf(clientDTO.getCpf());
            clientEntity.setEmail(clientDTO.getEmail());
            clientEntity.setDateOfBirth(clientDTO.getDateOfBirth());
            clientEntity.setCellPhone(clientDTO.getCellPhone());
            clientEntity.setBalance(clientDTO.getBalance());
            return clientEntity;
        }).orElseThrow(() -> new EntityNotFoundException("Cliente Não Encontrado."));
    }

    @Transactional
    public void deleteClientById(Long id){
        clientRepository.deleteById(id);
    }

}
