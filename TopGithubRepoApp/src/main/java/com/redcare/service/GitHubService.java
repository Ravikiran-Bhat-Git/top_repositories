package com.redcare.service;

import com.redcare.domain.GitHubQueryParam;
import com.redcare.domain.GitHubResponse;
import reactor.core.publisher.Flux;

public interface GitHubService {
    public Flux<GitHubResponse> getTopRepositoriesByParams(GitHubQueryParam param);
}
