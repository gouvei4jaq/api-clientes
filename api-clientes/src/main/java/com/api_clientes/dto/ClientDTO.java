package com.api_clientes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    @JsonProperty("Nome")
    @NotBlank(message = "Nome é um campo obrigatório")
    @Size(min = 3, message = "O campo Nome deve ter pelo menos 3 caracteres")
    private String name;

    @JsonProperty("Cpf")
    @CPF(message = "Cpf Inválido")
    @NotBlank(message = "CPF é um campo obrigatório")
    @Size(min = 11, max = 11, message = "CPF precisa ter 11 dígitos")
    private String cpf;

    @JsonProperty("Email")
    @Email(message = "Email inválido")
    private String email;

    @JsonProperty("DataNascimento")
    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dateOfBirth;

    @JsonProperty("Telefone")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String cellPhone;

    @JsonProperty("Endereço")
    @NotBlank(message = "Endereço é um campo obrigatório")
    private String address;

    @JsonProperty("Saldo")
    @PositiveOrZero(message = "O campo Saldo não pode ser negativo")
    private BigDecimal balance;

}
