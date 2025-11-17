package com.example.franchise.infrastructure.entrypoint.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto {

    private String id;
    private String branchId;
    private String name;
    private Integer stock;
}
