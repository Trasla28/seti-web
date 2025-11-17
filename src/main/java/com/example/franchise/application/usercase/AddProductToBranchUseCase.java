package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.BranchNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddProductToBranchUseCase {

    private final BranchRepositoryPort branchRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    public Mono<Product> execute(String branchId, String productName, Integer stock) {
        log.info("Adding product to branchId={}, name={}, stock={}", branchId, productName, stock);

        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(branch -> createProduct(branch, productName, stock))
                .doOnNext(p -> log.info("Product created with id={} for branchId={}", p.getId(), branchId));
    }

    private Mono<Product> createProduct(Branch branch, String productName, Integer stock) {
        Product product = Product.builder()
                .branchId(branch.getId())
                .name(productName)
                .stock(stock != null ? stock : 0)
                .build();

        return productRepositoryPort.save(product);
    }
}
