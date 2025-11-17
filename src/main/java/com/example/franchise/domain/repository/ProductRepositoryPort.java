package com.example.franchise.domain.repository;

import com.example.franchise.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {

    Mono<Product> save(Product product);

    Mono<Product> findById(String id);

    Flux<Product> findByBranchId(String branchId);

    Mono<Void> deleteById(String id);
}
