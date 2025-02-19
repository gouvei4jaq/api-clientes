package com.api_clientes.entity;

import com.api_clientes.request.ClientRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class ClientEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "dataNascimento")
    private LocalDate dateOfBirth;

    @Column(name = "telefone")
    private String cellPhone;

    @Column(name = "endereço")
    private String address;

    @Column(name = "saldo")
    private BigDecimal balance;

    public static ClientEntity valueOf(ClientRequest clientRequest){
        return ClientEntity.builder()
                .cpf(clientRequest.getCpf())
                .email(clientRequest.getEmail())
                .name(clientRequest.getName())
                .dateOfBirth(clientRequest.getDateOfBirth())
                .cellPhone(clientRequest.getCellPhone())
                .address(clientRequest.getAddress())
                .balance(clientRequest.getBalance())
                .build();
    }

    public void updateClient(ClientRequest clientRequest){
        setName(clientRequest.getName());
        setCpf(clientRequest.getCpf());
        setEmail(clientRequest.getEmail());
        setDateOfBirth(clientRequest.getDateOfBirth());
        setCellPhone(clientRequest.getCellPhone());
        setAddress(clientRequest.getAddress());
        setBalance(clientRequest.getBalance());
    }
}
