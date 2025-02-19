package com.api_clientes.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequest {

    @JsonProperty("Nome")
    @NotBlank(message = "Campo Obrigatório")
    @Size(min = 3, message = "O campo Nome deve ter pelo menos 3 caracteres")
    private String name;

    @JsonProperty("Cpf")
    @CPF(message = "Campo Inválido")
    @NotBlank(message = "Campo Obrigatório")
    @Size(min = 11, max = 11, message = "CPF precisa ter 11 dígitos")
    private String cpf;

    @JsonProperty("Email")
    @Email(message = "Campo Inválido")
    private String email;

    @JsonProperty("DataNascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Campo Obrigatório")
    @Past(message = "A data de nascimento deve estar no passado")
    private LocalDate dateOfBirth;

    @JsonProperty("Telefone")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String cellPhone;

    @JsonProperty("Endereço")
    @NotBlank(message = "Campo Obrigatório")
    private String address;

    @JsonProperty("Saldo")
    @PositiveOrZero(message = "O campo Saldo não pode ser negativo")
    private BigDecimal balance;

}
