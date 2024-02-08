package com.redcare.client;

import com.redcare.domain.GitHubResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.json.Json;
import java.io.StringReader;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GitHubClientImpl implements GitHubClient {
    private final WebClient webClient;
    @Override
    public Flux<GitHubResponse> fetchRepositories(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonString -> Objects.requireNonNull(Json.createReader(new StringReader(jsonString)).readObject()))
                .map(GitHubResponse::from)
                .flux();
    }

}
