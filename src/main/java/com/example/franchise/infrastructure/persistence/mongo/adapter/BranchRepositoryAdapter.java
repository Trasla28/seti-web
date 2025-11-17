package com.example.franchise.infrastructure.persistence.mongo.adapter;

import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.infrastructure.persistence.mongo.document.BranchDocument;
import com.example.franchise.infrastructure.persistence.mongo.repository.BranchReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private final BranchReactiveRepository repository;

    @Override
    public Mono<Branch> save(Branch branch) {
        return repository.save(toDocument(branch))
                .map(this::toDomain);
    }

    @Override
    public Mono<Branch> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private BranchDocument toDocument(Branch branch) {
        if (branch == null) return null;
        return BranchDocument.builder()
                .id(branch.getId())
                .franchiseId(branch.getFranchiseId())
                .name(branch.getName())
                .build();
    }

    private Branch toDomain(BranchDocument doc) {
        if (doc == null) return null;
        return Branch.builder()
                .id(doc.getId())
                .franchiseId(doc.getFranchiseId())
                .name(doc.getName())
                .build();
    }
}
