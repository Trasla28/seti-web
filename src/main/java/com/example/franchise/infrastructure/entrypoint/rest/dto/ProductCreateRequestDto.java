package com.example.franchise.infrastructure.entrypoint.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductCreateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private Integer stock;
}
