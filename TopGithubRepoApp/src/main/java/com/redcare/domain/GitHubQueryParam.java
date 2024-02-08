package com.redcare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GitHubQueryParam {
    private String date;
    private String language;
    private Integer count;
    private String sortFieldName;
    private String sortOrder;
}
