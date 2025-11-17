package com.example.franchise.infrastructure.persistence.mongo.adapter;

import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import com.example.franchise.infrastructure.persistence.mongo.document.FranchiseDocument;
import com.example.franchise.infrastructure.persistence.mongo.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final FranchiseReactiveRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(toDocument(franchise))
                .map(this::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    private FranchiseDocument toDocument(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    private Franchise toDomain(FranchiseDocument doc) {
        if (doc == null) return null;
        return Franchise.builder()
                .id(doc.getId())
                .name(doc.getName())
                .build();
    }
}
