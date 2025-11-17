package com.example.franchise.infrastructure.entrypoint.rest;

import com.example.franchise.application.usecase.CreateFranchiseUseCase;
import com.example.franchise.application.usecase.GetMaxStockProductPerBranchUseCase;
import com.example.franchise.application.usecase.UpdateFranchiseNameUseCase;
import com.example.franchise.application.usecase.model.BranchMaxStockProduct;
import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.infrastructure.entrypoint.rest.dto.FranchiseCreateRequestDto;
import com.example.franchise.infrastructure.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = FranchiseController.class)
@Import(GlobalExceptionHandler.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CreateFranchiseUseCase createFranchiseUseCase;

    @MockBean
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    @MockBean
    private GetMaxStockProductPerBranchUseCase getMaxStockProductPerBranchUseCase;

    @Test
    void shouldCreateFranchise() {
        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("Franquicia A")
                .build();

        when(createFranchiseUseCase.execute("Franquicia A"))
                .thenReturn(Mono.just(franchise));

        FranchiseCreateRequestDto request = new FranchiseCreateRequestDto();
        request.setName("Franquicia A");

        webTestClient.post()
                .uri("/api/v1/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("f1")
                .jsonPath("$.name").isEqualTo("Franquicia A");
    }

    @Test
    void shouldReturnMaxStockProductsForFranchise() {
        BranchMaxStockProduct r1 = BranchMaxStockProduct.builder()
                .franchiseId("f1")
                .branchId("b1")
                .branchName("Sucursal 1")
                .productId("p1")
                .productName("P1")
                .stock(10)
                .build();

        when(getMaxStockProductPerBranchUseCase.execute("f1"))
                .thenReturn(Flux.just(r1));

        webTestClient.get()
                .uri("/api/v1/franchises/f1/branches/max-stock-products")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].franchiseId").isEqualTo("f1")
                .jsonPath("$[0].branchId").isEqualTo("b1")
                .jsonPath("$[0].productId").isEqualTo("p1")
                .jsonPath("$[0].stock").isEqualTo(10);
    }

    @Test
    void shouldReturn404WhenFranchiseNotFoundInMaxStockEndpoint() {
        when(getMaxStockProductPerBranchUseCase.execute("f-unknown"))
                .thenReturn(Flux.error(new FranchiseNotFoundException("f-unknown")));

        webTestClient.get()
                .uri("/api/v1/franchises/f-unknown/branches/max-stock-products")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found");
    }
}
