package com.example.franchise.application.usecase;

import com.example.franchise.domain.model.Franchise;
import com.example.franchise.domain.repository.FranchiseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @InjectMocks
    private CreateFranchiseUseCase createFranchiseUseCase;

    @Test
    void shouldCreateFranchise() {
        // arrange
        String name = "Franquicia Test";

        Franchise saved = Franchise.builder()
                .id("123")
                .name(name)
                .build();

        when(franchiseRepositoryPort.save(any(Franchise.class)))
                .thenReturn(Mono.just(saved));

        // act
        Mono<Franchise> result = createFranchiseUseCase.execute(name);

        // assert
        StepVerifier.create(result)
                .assertNext(franchise -> {
                    assertThat(franchise.getId()).isEqualTo("123");
                    assertThat(franchise.getName()).isEqualTo(name);
                })
                .verifyComplete();

        ArgumentCaptor<Franchise> captor = ArgumentCaptor.forClass(Franchise.class);
        verify(franchiseRepositoryPort).save(captor.capture());

        assertThat(captor.getValue().getName()).isEqualTo(name);
    }
}
