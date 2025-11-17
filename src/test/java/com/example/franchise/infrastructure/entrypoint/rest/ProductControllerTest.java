package com.example.franchise.infrastructure.entrypoint.rest;

import com.example.franchise.application.usecase.AddProductToBranchUseCase;
import com.example.franchise.application.usecase.DeleteProductUseCase;
import com.example.franchise.application.usecase.UpdateProductNameUseCase;
import com.example.franchise.application.usecase.UpdateProductStockUseCase;
import com.example.franchise.domain.model.Product;
import com.example.franchise.infrastructure.entrypoint.rest.dto.ProductCreateRequestDto;
import com.example.franchise.infrastructure.entrypoint.rest.dto.UpdateStockRequestDto;
import com.example.franchise.infrastructure.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = ProductController.class)
@Import(GlobalExceptionHandler.class)
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AddProductToBranchUseCase addProductToBranchUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private UpdateProductStockUseCase updateProductStockUseCase;

    @MockBean
    private UpdateProductNameUseCase updateProductNameUseCase;

    @Test
    void shouldAddProductToBranch() {
        Product product = Product.builder()
                .id("p1")
                .branchId("b1")
                .name("Producto 1")
                .stock(10)
                .build();

        when(addProductToBranchUseCase.execute(eq("b1"), eq("Producto 1"), eq(10)))
                .thenReturn(Mono.just(product));

        ProductCreateRequestDto request = new ProductCreateRequestDto();
        request.setName("Producto 1");
        request.setStock(10);

        webTestClient.post()
                .uri("/api/v1/branches/b1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("p1")
                .jsonPath("$.branchId").isEqualTo("b1")
                .jsonPath("$.name").isEqualTo("Producto 1")
                .jsonPath("$.stock").isEqualTo(10);
    }

    @Test
    void shouldUpdateProductStock() {
        Product updated = Product.builder()
                .id("p1")
                .branchId("b1")
                .name("Producto 1")
                .stock(50)
                .build();

        when(updateProductStockUseCase.execute(eq("p1"), eq(50)))
                .thenReturn(Mono.just(updated));

        UpdateStockRequestDto request = new UpdateStockRequestDto();
        request.setStock(50);

        webTestClient.patch()
                .uri("/api/v1/products/p1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("p1")
                .jsonPath("$.stock").isEqualTo(50);
    }
}
