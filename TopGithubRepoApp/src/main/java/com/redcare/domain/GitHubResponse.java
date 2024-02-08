package com.redcare.domain;

import com.redcare.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.json.JsonObject;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GitHubResponse {
    private Long totalCount;
    private boolean incompleteResults;
    private List<GitHubRepositoryItem> items;

    public static GitHubResponse from(JsonObject response) {
        List<GitHubRepositoryItem> items = response.getJsonArray("items")
                .getValuesAs(JsonObject.class)
                .stream()
                .map(GitHubRepositoryItem::from)
                .toList();

        return GitHubResponse.builder()
                .totalCount(JsonUtil.nullSafeLongValue(response.getJsonNumber("total_count")))
                .incompleteResults(response.getBoolean("incomplete_results"))
                .items(items)
                .build();
    }
}
