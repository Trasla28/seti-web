package com.example.franchise.infrastructure.persistence.mongo.adapter;

import com.example.franchise.domain.model.Product;
import com.example.franchise.domain.repository.ProductRepositoryPort;
import com.example.franchise.infrastructure.persistence.mongo.document.ProductDocument;
import com.example.franchise.infrastructure.persistence.mongo.repository.ProductReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductReactiveRepository repository;

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(toDocument(product))
                .map(this::toDomain);
    }

    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(String branchId) {
        return repository.findByBranchId(branchId)
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private ProductDocument toDocument(Product product) {
        if (product == null) return null;
        return ProductDocument.builder()
                .id(product.getId())
                .branchId(product.getBranchId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    private Product toDomain(ProductDocument doc) {
        if (doc == null) return null;
        return Product.builder()
                .id(doc.getId())
                .branchId(doc.getBranchId())
                .name(doc.getName())
                .stock(doc.getStock())
                .build();
    }
}
