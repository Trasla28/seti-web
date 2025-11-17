package com.example.franchise.application.usecase;

import com.example.franchise.application.usecase.model.BranchMaxStockProduct;
import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMaxStockProductPerBranchUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private GetMaxStockProductPerBranchUseCase useCase;

    @Test
    void shouldReturnMaxStockProductPerBranchWhenFranchiseExists() {
        String franchiseId = "f1";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Franquicia X")
                .build();

        Branch branch1 = Branch.builder()
                .id("b1")
                .franchiseId(franchiseId)
                .name("Sucursal 1")
                .build();

        Branch branch2 = Branch.builder()
                .id("b2")
                .franchiseId(franchiseId)
                .name("Sucursal 2")
                .build();

        Product p1 = Product.builder().id("p1").branchId("b1").name("P1").stock(10).build();
        Product p2 = Product.builder().id("p2").branchId("b1").name("P2").stock(15).build();

        Product p3 = Product.builder().id("p3").branchId("b2").name("P3").stock(8).build();

        when(franchiseRepositoryPort.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(branchRepositoryPort.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branch1, branch2));

        when(productRepositoryPort.findByBranchId("b1"))
                .thenReturn(Flux.just(p1, p2));

        when(productRepositoryPort.findByBranchId("b2"))
                .thenReturn(Flux.just(p3));

        Flux<BranchMaxStockProduct> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertThat(res.getBranchId()).isEqualTo("b1");
                    assertThat(res.getProductId()).isEqualTo("p2"); // stock 15
                    assertThat(res.getStock()).isEqualTo(15);
                })
                .assertNext(res -> {
                    assertThat(res.getBranchId()).isEqualTo("b2");
                    assertThat(res.getProductId()).isEqualTo("p3");
                    assertThat(res.getStock()).isEqualTo(8);
                })
                .verifyComplete();
    }

        @Test
        void shouldErrorWhenFranchiseDoesNotExist() {
        String franchiseId = "f-unknown";

        when(franchiseRepositoryPort.findById(franchiseId))
                .thenReturn(Mono.empty());

        // Muy importante: devolver un Flux vacío en vez de null
        when(branchRepositoryPort.findByFranchiseId(franchiseId))
                .thenReturn(Flux.empty());

        Flux<BranchMaxStockProduct> result = useCase.execute(franchiseId);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();
        }
}
