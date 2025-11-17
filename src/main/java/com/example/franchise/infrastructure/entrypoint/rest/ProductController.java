package com.example.franchise.infrastructure.entrypoint.rest;

import com.example.franchise.application.usecase.AddProductToBranchUseCase;
import com.example.franchise.application.usecase.DeleteProductUseCase;
import com.example.franchise.application.usecase.UpdateProductNameUseCase;
import com.example.franchise.application.usecase.UpdateProductStockUseCase;
import com.example.franchise.domain.model.Product;
import com.example.franchise.infrastructure.entrypoint.rest.dto.ProductCreateRequestDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.ProductResponseDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.UpdateNameRequestDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.UpdateStockRequestDto;
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
public class ProductController {

    private final AddProductToBranchUseCase addProductToBranchUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;

    // 3) Agregar producto a sucursal
    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponseDto> addProduct(
            @PathVariable String branchId,
            @Valid @RequestBody ProductCreateRequestDto request
    ) {
        log.info("REST - add product to branchId={}, name={}, stock={}",
                branchId, request.getName(), request.getStock());
        return addProductToBranchUseCase.execute(branchId, request.getName(), request.getStock())
                .map(this::toResponse);
    }

    // 4) Eliminar producto de una sucursal
    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String productId) {
        log.info("REST - delete product id={}", productId);
        return deleteProductUseCase.execute(productId);
    }

    // 5) Modificar stock de un producto
    @PatchMapping("/products/{productId}/stock")
    public Mono<ProductResponseDto> updateStock(
            @PathVariable String productId,
            @Valid @RequestBody UpdateStockRequestDto request
    ) {
        log.info("REST - update product stock id={}, stock={}", productId, request.getStock());
        return updateProductStockUseCase.execute(productId, request.getStock())
                .map(this::toResponse);
    }

    // PLUS: actualizar nombre de un producto
    @PatchMapping("/products/{productId}")
    public Mono<ProductResponseDto> updateName(
            @PathVariable String productId,
            @Valid @RequestBody UpdateNameRequestDto request
    ) {
        log.info("REST - update product name id={}, name={}", productId, request.getName());
        return updateProductNameUseCase.execute(productId, request.getName())
                .map(this::toResponse);
    }

    private ProductResponseDto toResponse(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .branchId(product.getBranchId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}
