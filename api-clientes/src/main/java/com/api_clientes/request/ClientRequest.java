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

    @NotBlank(message = "400.001")
    @Size(min = 3, message = "400.002")
    private String nome;

    @CPF(message = "422.000")
    @NotBlank(message = "400.001")
    @Size(min = 11, max = 11, message = "400.003")
    private String cpf;

    @Email(message = "422.000")
    @NotBlank(message = "400.001")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "400.001")
    @Past(message = "400.004")
    private LocalDate dataNascimento;

    @Pattern(regexp = "\\d{10,11}", message = "400.005")
    private String telefone;

    @NotBlank(message = "400.001")
    private String endereco;

    @PositiveOrZero(message = "422.001")
    private BigDecimal saldo;

}
