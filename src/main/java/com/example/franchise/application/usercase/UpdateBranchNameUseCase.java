package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.BranchNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateBranchNameUseCase {

    private final BranchRepositoryPort branchRepositoryPort;

    public Mono<Branch> execute(String branchId, String newName) {
        log.info("Updating branch name id={}, newName={}", branchId, newName);

        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .map(branch -> {
                    branch.setName(newName);
                    return branch;
                })
                .flatMap(branchRepositoryPort::save)
                .doOnNext(b -> log.info("Branch updated id={}, name={}", b.getId(), b.getName()));
    }
}
