package com.example.franchise.infrastructure.persistence.mongo.repository;

import com.example.franchise.infrastructure.persistence.mongo.document.BranchDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface BranchReactiveRepository extends ReactiveMongoRepository<BranchDocument, String> {

    Flux<BranchDocument> findByFranchiseId(String franchiseId);
}
