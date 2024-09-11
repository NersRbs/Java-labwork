package ru.gunkin.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.gunkin.contracts.CatClient;
import ru.gunkin.dto.CatDto;
import ru.gunkin.models.Breed;
import ru.gunkin.models.Color;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CatClientImpl implements CatClient {
    private WebClient webClient;

    @Autowired
    public CatClientImpl(WebClient.Builder webClientBuilder, @Value("${cat-microservice.url:http://localhost:8081}") String url) {
        this.webClient = webClientBuilder
                .defaultHeader("Content-Type", "application/json")
                .baseUrl(url)
                .build();
    }

    @Override
    public Optional<CatDto> findCat(long id) {
        return webClient.get()
                .uri("/api/v1/cats/{id}", id)
                .retrieve()
                .bodyToMono(CatDto.class)
                .blockOptional();
    }

    @Override
    public List<CatDto> getCatFriends(long id) {
        return webClient.get()
                .uri("/api/v1/cats/{id}/friends", id)
                .retrieve()
                .bodyToFlux(CatDto.class)
                .collectList()
                .block();
    }

    @Override
    public List<CatDto> findCats(String name, Breed breed, Color color, Long ownerId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/cats")
                        .queryParamIfPresent("name", Optional.ofNullable(name))
                        .queryParamIfPresent("breed", Optional.ofNullable(breed))
                        .queryParamIfPresent("color", Optional.ofNullable(color))
                        .queryParamIfPresent("ownerId", Optional.ofNullable(ownerId))
                        .build())
                .retrieve()
                .bodyToFlux(CatDto.class)
                .collectList()
                .block();
    }
}
