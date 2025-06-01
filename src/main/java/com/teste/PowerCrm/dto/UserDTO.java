package com.teste.PowerCrm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @Email(message = "O e-mail informado é inválido.")
    private String email;

    @Pattern(regexp = "^$|\\d{10,11}", message = "O telefone informado é inválido")
    private String phone;

    @Pattern(regexp = "\\d{11}", message = "O CPF informado é inválido")
    private String cpf;

    private String zipCode;

    private String address;

    private String number;

    private String complement;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Boolean status;

    @Schema(hidden = true)
    private List<VehicleDTO> vehicles;

    public UserDTO(String name, String email, String phone, String cpf,
                   String zipCode, String address, String number, String complement,
                   LocalDateTime createdAt, Boolean status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cpf = cpf;
        this.zipCode = zipCode;
        this.address = address;
        this.number = number;
        this.complement = complement;
        this.createdAt = createdAt;
        this.status = status;
    }
}
