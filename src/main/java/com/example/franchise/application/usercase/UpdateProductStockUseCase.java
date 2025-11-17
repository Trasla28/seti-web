package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.ProductNotFoundException;
import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public Mono<Product> execute(String productId, Integer newStock) {
        log.info("Updating stock for productId={}, newStock={}", productId, newStock);

        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .map(product -> {
                    product.setStock(newStock);
                    return product;
                })
                .flatMap(productRepositoryPort::save)
                .doOnNext(p -> log.info("Stock updated for productId={}, stock={}", p.getId(), p.getStock()));
    }
}
