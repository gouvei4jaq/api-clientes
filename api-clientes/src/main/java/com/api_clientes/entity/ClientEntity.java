package com.api_clientes.entity;

import com.api_clientes.request.ClientRequest;
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
    private String nome;

    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "dataNascimento")
    private LocalDate dataNascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "endereço")
    private String endereco;

    @Column(name = "saldo")
    private BigDecimal saldo;

    public static ClientEntity valueOf(ClientRequest clientRequest){
        return ClientEntity.builder()
                .cpf(clientRequest.getCpf())
                .email(clientRequest.getEmail())
                .nome(clientRequest.getNome())
                .dataNascimento(clientRequest.getDataNascimento())
                .telefone(clientRequest.getTelefone())
                .endereco(clientRequest.getEndereco())
                .saldo(clientRequest.getSaldo())
                .build();
    }

    public void updateClient(ClientRequest clientRequest){
        setNome(clientRequest.getNome());
        setCpf(clientRequest.getCpf());
        setEmail(clientRequest.getEmail());
        setDataNascimento(clientRequest.getDataNascimento());
        setTelefone(clientRequest.getTelefone());
        setEndereco(clientRequest.getEndereco());
        setSaldo(clientRequest.getSaldo());
    }
}
