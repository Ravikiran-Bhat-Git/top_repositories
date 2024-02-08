package com.redcare.controller;

import com.redcare.domain.GitHubQueryParam;
import com.redcare.domain.GitHubResponse;
import com.redcare.enums.Order;
import com.redcare.enums.SortField;
import com.redcare.service.GitHubService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping
    public ResponseEntity<Flux<GitHubResponse>> listTopRepositories(
            @RequestParam(name = "date") @NotNull(message = "Date cannot be null") String date,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(name = "count", required = false, defaultValue = "10") @Max(value = 100) Integer count
    ) {
        return new ResponseEntity<>(gitHubService.getTopRepositoriesByParams(
                new GitHubQueryParam(date, language, count, SortField.STARS.getName(), Order.DESC.getName())
        ), HttpStatus.OK);
    }

}
