package com.example.franchise.application.usecase;

import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public Mono<Franchise> execute(String name) {
        log.info("Creating franchise with name={}", name);

        Franchise franchise = Franchise.builder()
                .name(name)
                .build();

        return franchiseRepositoryPort.save(franchise)
                .doOnNext(f -> log.info("Franchise created with id={}", f.getId()));
    }
}
