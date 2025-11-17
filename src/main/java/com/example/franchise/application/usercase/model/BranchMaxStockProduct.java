package com.example.franchise.application.usecase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchMaxStockProduct {

    private String franchiseId;
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;
}
