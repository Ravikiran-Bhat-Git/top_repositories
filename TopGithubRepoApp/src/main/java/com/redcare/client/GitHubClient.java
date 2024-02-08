package com.redcare.client;

import com.redcare.domain.GitHubResponse;
import reactor.core.publisher.Flux;

public interface GitHubClient {
    public Flux<GitHubResponse> fetchRepositories(String url);
}
