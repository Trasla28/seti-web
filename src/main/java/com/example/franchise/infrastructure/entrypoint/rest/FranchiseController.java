package com.example.franchise.infrastructure.entrypoint.rest;

import com.example.franchise.application.usecase.CreateFranchiseUseCase;
import com.example.franchise.application.usecase.GetMaxStockProductPerBranchUseCase;
import com.example.franchise.application.usecase.UpdateFranchiseNameUseCase;
import com.example.franchise.application.usecase.model.BranchMaxStockProduct;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.infrastructure.entrypoint.rest.dto.BranchMaxStockProductResponseDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.FranchiseCreateRequestDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.FranchiseResponseDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.UpdateNameRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final GetMaxStockProductPerBranchUseCase getMaxStockProductPerBranchUseCase;

    // ❗ Endpoint que estás probando en Postman
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponseDto> createFranchise(
            @Valid @RequestBody FranchiseCreateRequestDto request
    ) {
        log.info("REST - create franchise name={}", request.getName());
        return createFranchiseUseCase.execute(request.getName())
                .map(this::toResponse);
    }

    // Plus: actualizar nombre
    @PatchMapping("/{franchiseId}")
    public Mono<FranchiseResponseDto> updateFranchiseName(
            @PathVariable String franchiseId,
            @Valid @RequestBody UpdateNameRequestDto request
    ) {
        log.info("REST - update franchise name id={}, name={}", franchiseId, request.getName());
        return updateFranchiseNameUseCase.execute(franchiseId, request.getName())
                .map(this::toResponse);
    }

    // Max stock por sucursal
    @GetMapping("/{franchiseId}/branches/max-stock-products")
    public Flux<BranchMaxStockProductResponseDto> getMaxStockPerBranch(
            @PathVariable String franchiseId
    ) {
        log.info("REST - get max stock products for franchiseId={}", franchiseId);
        return getMaxStockProductPerBranchUseCase.execute(franchiseId)
                .map(this::toMaxStockResponse);
    }

    // --- Mappers ---

    private FranchiseResponseDto toResponse(Franchise franchise) {
        return FranchiseResponseDto.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    private BranchMaxStockProductResponseDto toMaxStockResponse(BranchMaxStockProduct result) {
        return BranchMaxStockProductResponseDto.builder()
                .franchiseId(result.getFranchiseId())
                .branchId(result.getBranchId())
                .branchName(result.getBranchName())
                .productId(result.getProductId())
                .productName(result.getProductName())
                .stock(result.getStock())
                .build();
    }

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("pong");
    }
}
