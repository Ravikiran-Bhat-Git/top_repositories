package com.redcare.controller;

import com.redcare.domain.GitHubQueryParam;
import com.redcare.domain.GitHubRepositoryItem;
import com.redcare.domain.GitHubResponse;
import com.redcare.enums.Order;
import com.redcare.enums.SortField;
import com.redcare.service.GitHubService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GitHubController.class)
class GitHubControllerTest {
    private static final String API_URL = "/api/v1/github";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitHubService gitHubService;

    @Nested
    class SuccessfulResponse {
        @Test
        @DisplayName("should fetch top repositories when only date param provided")
        @SneakyThrows
        void shouldFetchTopRepositoriesWhenOnlyDateQueryParamProvided() {
            String date = "2022-02-07";

            GitHubQueryParam param = new GitHubQueryParam(
                    date,
                    null,
                    10,
                    SortField.STARS.getName(),
                    Order.DESC.getName()
            );
            Flux<GitHubResponse> responseFlux = prepareMock();
            when(gitHubService.getTopRepositoriesByParams(param)).thenReturn(responseFlux);

            mockMvc
                    .perform(
                            get(API_URL)
                                    .param("date", "2022-02-07")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk());

            verify(gitHubService, times(1)).getTopRepositoriesByParams(param);
        }

        @Test
        @DisplayName("should fetch top repositories when all params provided")
        @SneakyThrows
        void shouldFetchTopRepositoriesWithAllParamsProvided() {
            String date = "2022-02-07";
            String language = "Java";
            int count = 10;

            GitHubQueryParam param = new GitHubQueryParam(
                    date,
                    language,
                    count,
                    SortField.STARS.getName(),
                    Order.DESC.getName()
            );
            Flux<GitHubResponse> responseFlux = prepareMock();
            when(gitHubService.getTopRepositoriesByParams(param)).thenReturn(responseFlux);

            mockMvc
                    .perform(
                            get(API_URL)
                                    .param("date", "2022-02-07")
                                    .param("language", "Java")
                                    .param("count", "10")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk());

            verify(gitHubService, times(1)).getTopRepositoriesByParams(param);
        }
    }

    @Nested
    class FailureResponse {
        @Test
        @DisplayName("should check the mandatory params")
        @SneakyThrows
        void shouldCheckMandatoryParams() {
            when(gitHubService.getTopRepositoriesByParams(any())).thenReturn(Flux.empty());

            mockMvc
                    .perform(
                            get(API_URL)
                                    .param("date", "2022-02-07")
                                    .param("count", "150")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(gitHubService, never()).getTopRepositoriesByParams(any());
        }

        @Test
        @DisplayName("should not fetch when count param is greater than 100")
        @SneakyThrows
        void shouldNotFetchWhenCountRequestIsGreaterThan100() {
            when(gitHubService.getTopRepositoriesByParams(any())).thenReturn(Flux.empty());

            mockMvc
                    .perform(get(API_URL).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(gitHubService, never()).getTopRepositoriesByParams(any());
        }
    }


    @NotNull
    private static Flux<GitHubResponse> prepareMock() {
        return Flux.just(
                new GitHubResponse(10L, false, List.of(
                        new GitHubRepositoryItem(
                                1L,
                                "testRepo",
                                "https://github.com/testRepo",
                                "https://github.com/testRepo/stargazers",
                                100L,
                                "2022-02-07",
                                "Java")
                ))
        );
    }
}
