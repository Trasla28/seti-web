package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;

    public Mono<Branch> execute(String franchiseId, String branchName) {
        log.info("Adding branch to franchiseId={}, name={}", franchiseId, branchName);

        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> createBranch(franchise, branchName))
                .doOnNext(b -> log.info("Branch created with id={} for franchiseId={}", b.getId(), franchiseId));
    }

    private Mono<Branch> createBranch(Franchise franchise, String branchName) {
        Branch branch = Branch.builder()
                .franchiseId(franchise.getId())
                .name(branchName)
                .build();

        return branchRepositoryPort.save(branch);
    }
}
