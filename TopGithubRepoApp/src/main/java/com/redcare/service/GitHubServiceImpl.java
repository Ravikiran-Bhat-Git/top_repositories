package com.redcare.service;

import com.redcare.client.GitHubClient;
import com.redcare.config.GitHubConfig;
import com.redcare.constants.ExternalUrl;
import com.redcare.domain.GitHubQueryParam;
import com.redcare.domain.GitHubResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {
    private final GitHubConfig gitHubConfig;
    private final GitHubClient gitHubClient;

    @Override
    @Cacheable("topRepositories")
    public Flux<GitHubResponse> getTopRepositoriesByParams(GitHubQueryParam param) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(gitHubConfig.getGithubApi().concat(ExternalUrl.SEARCH_REPOSITORIES))
                .queryParam("q", createQueryString(param.getDate(), param.getLanguage()))
                .queryParam("sort", param.getSortFieldName())
                .queryParam("order", param.getSortOrder())
                .queryParam("per_page", param.getCount());

        return gitHubClient.fetchRepositories(uriBuilder.toUriString());
    }

    private String createQueryString(String date, String language) {
        StringBuilder queryString = new StringBuilder();

        LocalDate formattedDate = validateDate(date);
        queryString.append("created:").append(formattedDate);

        if (language != null && !language.isEmpty()) {
            queryString.append("+language:").append(language);
        }

        return queryString.toString();
    }

    private LocalDate validateDate(String date) {
        LocalDate formattedDate;
        try {
            formattedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date format. Please provide a valid ISO date format (yyyy-MM-dd).", e);
        }
        return formattedDate;
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(cacheNames = "topRepositories", allEntries = true)
    public void evictAllCaches() {
        log.info("Evicting cache at {}", Instant.now());
    }
}
