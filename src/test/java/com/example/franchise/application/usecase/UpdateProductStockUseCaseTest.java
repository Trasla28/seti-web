package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.ProductNotFoundException;
import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private UpdateProductStockUseCase useCase;

    @Test
    void shouldUpdateStockWhenProductExists() {
        String productId = "p1";
        Integer newStock = 50;

        Product existing = Product.builder()
                .id(productId)
                .branchId("b1")
                .name("Producto X")
                .stock(10)
                .build();

        Product updated = Product.builder()
                .id(productId)
                .branchId("b1")
                .name("Producto X")
                .stock(newStock)
                .build();

        when(productRepositoryPort.findById(productId))
                .thenReturn(Mono.just(existing));

        when(productRepositoryPort.save(any(Product.class)))
                .thenReturn(Mono.just(updated));

        Mono<Product> result = useCase.execute(productId, newStock);

        StepVerifier.create(result)
                .assertNext(product -> {
                    assertThat(product.getId()).isEqualTo(productId);
                    assertThat(product.getStock()).isEqualTo(newStock);
                })
                .verifyComplete();

        verify(productRepositoryPort).findById(productId);
        verify(productRepositoryPort).save(any(Product.class));
    }

    @Test
    void shouldErrorWhenProductDoesNotExist() {
        String productId = "p-unknown";

        when(productRepositoryPort.findById(productId))
                .thenReturn(Mono.empty());

        Mono<Product> result = useCase.execute(productId, 100);

        StepVerifier.create(result)
                .expectError(ProductNotFoundException.class)
                .verify();

        verify(productRepositoryPort).findById(productId);
        verify(productRepositoryPort, never()).save(any());
    }
}
