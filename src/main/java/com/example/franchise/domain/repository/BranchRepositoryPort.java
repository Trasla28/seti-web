package com.example.franchise.domain.repository;

import com.example.franchise.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepositoryPort {

    Mono<Branch> save(Branch branch);

    Mono<Branch> findById(String id);

    Flux<Branch> findByFranchiseId(String franchiseId);

    Mono<Void> deleteById(String id);
}
