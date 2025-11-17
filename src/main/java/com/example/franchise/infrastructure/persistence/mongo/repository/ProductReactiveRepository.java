package com.example.franchise.infrastructure.persistence.mongo.repository;

import com.example.franchise.infrastructure.persistence.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductReactiveRepository extends ReactiveMongoRepository<ProductDocument, String> {

    Flux<ProductDocument> findByBranchId(String branchId);
}
