package com.redcare.service;

import com.redcare.client.GitHubClient;
import com.redcare.config.GitHubConfig;
import com.redcare.domain.GitHubQueryParam;
import com.redcare.domain.GitHubResponse;
import com.redcare.enums.Order;
import com.redcare.enums.SortField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubServiceTest {
    @Mock
    private GitHubClient gitHubClient;

    @Mock
    private GitHubConfig gitHubConfig;

    @InjectMocks
    private GitHubServiceImpl gitHubService;


    @Test
    void testGetPopularRepositoriesByParams() {
        when(gitHubConfig.getGithubApi()).thenReturn("https://api.github.com");
        GitHubQueryParam param = new GitHubQueryParam(
                "2022-02-07",
                "Java",
                10,
                SortField.STARS.getName(),
                Order.DESC.getName()
        );
        Flux<GitHubResponse> responseFlux = Flux.just(
                new GitHubResponse(10L, false, Collections.emptyList())
        );
        when(gitHubClient.fetchRepositories(anyString())).thenReturn(responseFlux);

        Flux<GitHubResponse> result = gitHubService.getTopRepositoriesByParams(param);
        StepVerifier.create(responseFlux)
                .expectNextCount(1)
                .verifyComplete();

        verify(gitHubClient, times(1)).fetchRepositories(anyString());
        assertSame(responseFlux, result);
    }

    @Test
    void shouldCreateQueryStringWhenValidDateAndLanguageProvided() {
        String date = "2022-02-07";
        String language = "Java";

        String result = ReflectionTestUtils.invokeMethod(gitHubService, "createQueryString", date, language);

        assertEquals("created:2022-02-07+language:Java", result);
    }

    @Test
    void shouldCreateQueryStringWhenOnlyValidDateProvided() {
        String date = "2022-02-07";

        String result = ReflectionTestUtils.invokeMethod(gitHubService, "createQueryString", date, null);
        assertEquals("created:2022-02-07", result);
    }

    @Test
    void shouldNotCreateQueryStringWithInvalidDate() {
        String invalidDate = "2022-24-77";
        assertThrows(IllegalArgumentException.class, () ->
                ReflectionTestUtils.invokeMethod(gitHubService, "createQueryString", invalidDate, null));
    }

}
