package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    public Mono<Franchise> execute(String franchiseId, String newName) {
        log.info("Updating franchise name id={}, newName={}", franchiseId, newName);

        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .map(franchise -> {
                    franchise.setName(newName);
                    return franchise;
                })
                .flatMap(franchiseRepositoryPort::save)
                .doOnNext(f -> log.info("Franchise updated id={}, name={}", f.getId(), f.getName()));
    }
}
