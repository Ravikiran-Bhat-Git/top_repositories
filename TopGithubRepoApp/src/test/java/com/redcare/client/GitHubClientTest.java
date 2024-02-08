package com.redcare.client;

import com.redcare.domain.GitHubResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class GitHubClientTest {
    @Spy
    @InjectMocks
    private GitHubClient underTest = new GitHubClientImpl(WebClient.builder().baseUrl("http://localhost:8080").build());

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() {
        mockWebServer = new MockWebServer();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    class FetchGitHubRepositories {

        @SneakyThrows
        @Test
        void shouldReturnEmptyListForInvalidRequest() {
            String url = "/search/repositories?q=created:2044-02-07&sort=stars&order=desc&per_page=10";
            String responseBody = """
                        {
                            "total_count": 0,
                            "incomplete_results": false,
                            "items": []
                        }
                    """;

            mockWebServer.enqueue(
                    new MockResponse()
                            .setResponseCode(200)
                            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .setBody(responseBody)
            );

            Flux<GitHubResponse> responseFlux = underTest.fetchRepositories(mockWebServer.url(url).toString());

            StepVerifier.create(responseFlux)
                    .expectNextMatches(response ->
                            response.getTotalCount() == 0 &&
                                    !response.isIncompleteResults() &&
                                    response.getItems().isEmpty()
                    )
                    .expectComplete()
                    .verify();

        }

        @SneakyThrows
        @Test
        void shouldGetGithubReposWhenExists() {
            String url = "/search/repositories?q=created:2023-02-07&sort=stars&order=desc&per_page=1";

            String responseBody = """
                        {
                            "total_count": 1,
                            "incomplete_results": false,
                            "items": [{
                                "id": 123,
                                "full_name": "mock-repo/test",
                                "language": "Java",
                                "url": "https://api.github.com/repos/mock-repo/test",
                                "stargazers_url": "https://api.github.com/repos/mock-repo/test/stargazers",
                                "stargazers_count": 2,
                                "created_at": "2022-02-07T14:52:36Z",
                                "language": "Java"
                            }]
                        }
                    """;

            mockWebServer.enqueue(
                    new MockResponse()
                            .setResponseCode(200)
                            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .setBody(responseBody)
            );

            Flux<GitHubResponse> responseFlux = underTest.fetchRepositories(mockWebServer.url(url).toString());

            StepVerifier.create(responseFlux)
                    .expectNextMatches(response ->
                            response.getTotalCount() == 1 &&
                                    !response.isIncompleteResults() &&
                                    response.getItems().size() == 1  &&
                                    response.getItems().get(0).getId().equals(123L)
                    )
                    .expectComplete()
                    .verify();
        }
    }
}
