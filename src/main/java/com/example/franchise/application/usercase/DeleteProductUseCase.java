package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.ProductNotFoundException;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Mono<Void> execute(String productId) {
        log.info("Deleting product with id={}", productId);

        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(product -> productRepositoryPort.deleteById(productId))
                .doOnSuccess(v -> log.info("Product deleted id={}", productId));
    }
}
