package com.example.franchise.infrastructure.entrypoint.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchMaxStockProductResponseDto {

    private String franchiseId;
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;
}
