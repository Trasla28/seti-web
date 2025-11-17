package com.example.franchise.infrastructure.entrypoint.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNameRequestDto {

    @NotBlank
    private String name;
}
