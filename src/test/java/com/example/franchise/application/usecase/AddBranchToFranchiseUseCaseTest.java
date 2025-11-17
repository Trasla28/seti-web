package com.example.franchise.application.usecase;

import com.example.franchise.domain.exception.FranchiseNotFoundException;
import com.example.franchise.domain.model.Branch;
import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.BranchRepositoryPort;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBranchToFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @InjectMocks
    private AddBranchToFranchiseUseCase useCase;

    @Test
    void shouldAddBranchWhenFranchiseExists() {
        String franchiseId = "f1";
        String branchName = "Sucursal Centro";

        Franchise franchise = Franchise.builder()
                .id(franchiseId)
                .name("Franquicia X")
                .build();

        Branch savedBranch = Branch.builder()
                .id("b1")
                .franchiseId(franchiseId)
                .name(branchName)
                .build();

        when(franchiseRepositoryPort.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

        when(branchRepositoryPort.save(any(Branch.class)))
                .thenReturn(Mono.just(savedBranch));

        Mono<Branch> result = useCase.execute(franchiseId, branchName);

        StepVerifier.create(result)
                .assertNext(branch -> {
                    assertThat(branch.getId()).isEqualTo("b1");
                    assertThat(branch.getFranchiseId()).isEqualTo(franchiseId);
                    assertThat(branch.getName()).isEqualTo(branchName);
                })
                .verifyComplete();

        verify(franchiseRepositoryPort).findById(franchiseId);
        verify(branchRepositoryPort).save(any(Branch.class));
    }

    @Test
    void shouldErrorWhenFranchiseDoesNotExist() {
        String franchiseId = "f-unknown";

        when(franchiseRepositoryPort.findById(franchiseId))
                .thenReturn(Mono.empty());

        Mono<Branch> result = useCase.execute(franchiseId, "Sucursal");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepositoryPort).findById(franchiseId);
        verifyNoInteractions(branchRepositoryPort);
    }
}
