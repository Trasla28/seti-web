package com.example.franchise.infrastructure.entrypoint.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FranchiseCreateRequestDto {

    @NotBlank
    private String name;
}
