package com.example.franchise.infrastructure.entrypoint.rest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchResponseDto {

    private String id;
    private String franchiseId;
    private String name;
}
