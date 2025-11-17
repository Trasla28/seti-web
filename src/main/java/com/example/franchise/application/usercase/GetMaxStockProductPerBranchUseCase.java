package com.example.franchise.application.usecase;

import com.example.franchise.application.usecase.model.BranchMaxStockProduct;
import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetMaxStockProductPerBranchUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Flux<BranchMaxStockProduct> execute(String franchiseId) {
        log.info("Getting max stock product per branch for franchiseId={}", franchiseId);

        // 1) Verificar que la franquicia exista
        Mono<Void> checkFranchiseExists = franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .then(); // convertimos a Mono<Void>

        // 2) Una vez verificada, obtener las sucursales y calcular el max product por cada una
        return checkFranchiseExists.thenMany(
                branchRepositoryPort.findByFranchiseId(franchiseId)
                        .flatMap(branch -> maxProductForBranch(franchiseId, branch))
        ).doOnNext(result ->
                        log.info("Max product for branchId={} is productId={} stock={}",
                                result.getBranchId(), result.getProductId(), result.getStock())
        ).doOnError(ex ->
                log.error("Error getting max stock products for franchiseId={}", franchiseId, ex)
        );
    }

    private Mono<BranchMaxStockProduct> maxProductForBranch(String franchiseId, Branch branch) {
        return productRepositoryPort.findByBranchId(branch.getId())
                // reduce para encontrar el producto con mayor stock dentro de la sucursal
                .reduce(this::maxByStock)
                // Si no hay productos en esa sucursal, no devolvemos nada
                .flatMap(product -> Mono.just(toResult(franchiseId, branch, product)));
    }

    private Product maxByStock(Product p1, Product p2) {
        Integer s1 = p1.getStock() != null ? p1.getStock() : 0;
        Integer s2 = p2.getStock() != null ? p2.getStock() : 0;
        return s1 >= s2 ? p1 : p2;
    }

    private BranchMaxStockProduct toResult(String franchiseId, Branch branch, Product product) {
        return BranchMaxStockProduct.builder()
                .franchiseId(franchiseId)
                .branchId(branch.getId())
                .branchName(branch.getName())
                .productId(product.getId())
                .productName(product.getName())
                .stock(product.getStock())
                .build();
    }
}
