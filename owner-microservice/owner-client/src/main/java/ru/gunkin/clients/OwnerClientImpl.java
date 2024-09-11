package ru.gunkin.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gunkin.contracts.OwnerClient;
import ru.gunkin.dto.OwnerDto;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OwnerClientImpl implements OwnerClient {
    private WebClient webClient;

    @Autowired
    public OwnerClientImpl(WebClient.Builder webClientBuilder, @Value("${owner-microservice.url:http://localhost:8082}") String url) {
        this.webClient = webClientBuilder
                .defaultHeader("Content-Type", "application/json")
                .baseUrl(url)
                .build();
    }

    @Override
    public Optional<OwnerDto> findOwner(long id) {
        return webClient.get()
                .uri("/api/v1/owners/{id}", id)
                .retrieve()
                .bodyToMono(OwnerDto.class)
                .blockOptional();
    }

    @Override
    public List<OwnerDto> findOwners() {
        return webClient.get()
                .uri("/api/v1/owners")
                .retrieve()
                .bodyToFlux(OwnerDto.class)
                .collectList()
                .block();
    }
}
