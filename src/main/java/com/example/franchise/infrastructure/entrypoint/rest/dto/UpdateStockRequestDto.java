package com.example.franchise.infrastructure.entrypoint.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStockRequestDto {

    @NotNull
    private Integer stock;
}
