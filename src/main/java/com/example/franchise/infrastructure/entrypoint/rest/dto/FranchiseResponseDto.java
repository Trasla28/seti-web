package com.example.franchise.infrastructure.entrypoint.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FranchiseResponseDto {

    private String id;
    private String name;
}
