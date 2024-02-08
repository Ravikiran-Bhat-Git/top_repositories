package com.redcare.domain;

import com.redcare.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.json.JsonObject;

@Data
@AllArgsConstructor
@Builder
public class GitHubRepositoryItem {
    private Long id;
    private String fullName;
    private String url;
    private String stargazersUrl;
    private Long stargazersCount;
    private String createdAt;
    private String language;

    public static GitHubRepositoryItem from(JsonObject repositoryItem) {
        return GitHubRepositoryItem.builder()
                .id(JsonUtil.nullSafeLongValue(repositoryItem.getJsonNumber("id")))
                .fullName(JsonUtil.nullSafeGetString(repositoryItem, "full_name"))
                .url(JsonUtil.nullSafeGetString(repositoryItem, "url"))
                .stargazersUrl(JsonUtil.nullSafeGetString(repositoryItem, "stargazers_url"))
                .stargazersCount(JsonUtil.nullSafeLongValue(repositoryItem.getJsonNumber("stargazers_count")))
                .createdAt(JsonUtil.nullSafeGetString(repositoryItem, "created_at"))
                .language(JsonUtil.nullSafeGetString(repositoryItem, "language"))
                .build();
    }

}
