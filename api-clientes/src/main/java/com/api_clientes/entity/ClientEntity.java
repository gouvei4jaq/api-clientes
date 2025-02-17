package com.api_clientes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
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
    @NotBlank(message = "Nome é um campo obrigatório")
    @Size(min = 3, message = "O campo Nome deve ter pelo menos 3 caracteres")
    private String name;

    @Column(name = "cpf", unique = true, nullable = false)
    @CPF(message = "Cpf Inválido")
    @NotBlank(message = "CPF é um campo obrigatório")
    @Size(min = 11, max = 11, message = "CPF precisa ter 11 dígitos")
    private String cpf;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email inválido")
    private String email;

    @Column(name = "dataNascimento")
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "telefone")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String cellPhone;

    @Column(name = "endereço")
    @NotBlank(message = "Endereço é um campo obrigatório")
    private String address;

    @Column(name = "saldo")
    @PositiveOrZero(message = "O campo Saldo não pode ser negativo")
    private BigDecimal balance;


}
