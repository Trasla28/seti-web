package com.example.franchise.infrastructure.entrypoint.rest;

import com.example.franchise.application.usecase.AddBranchToFranchiseUseCase;
import com.example.franchise.application.usecase.UpdateBranchNameUseCase;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.infrastructure.entrypoint.rest.dto.BranchCreateRequestDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.BranchResponseDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.UpdateNameRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BranchController {

    private final AddBranchToFranchiseUseCase addBranchToFranchiseUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    // 2) Agregar sucursal a una franquicia
    @PostMapping("/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponseDto> addBranch(
            @PathVariable String franchiseId,
            @Valid @RequestBody BranchCreateRequestDto request
    ) {
        log.info("REST - add branch to franchiseId={}, name={}", franchiseId, request.getName());
        return addBranchToFranchiseUseCase.execute(franchiseId, request.getName())
                .map(this::toResponse);
    }

    // PLUS: actualizar nombre de sucursal
    @PatchMapping("/branches/{branchId}")
    public Mono<BranchResponseDto> updateBranchName(
            @PathVariable String branchId,
            @Valid @RequestBody UpdateNameRequestDto request
    ) {
        log.info("REST - update branch name id={}, name={}", branchId, request.getName());
        return updateBranchNameUseCase.execute(branchId, request.getName())
                .map(this::toResponse);
    }

    private BranchResponseDto toResponse(Branch branch) {
        return BranchResponseDto.builder()
                .id(branch.getId())
                .franchiseId(branch.getFranchiseId())
                .name(branch.getName())
                .build();
    }
}
