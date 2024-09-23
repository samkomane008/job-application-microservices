package com.company.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class ReviewMessage {

    private final Long Id;
    private final String title;
    private final String description;
    private final String rating;
    private final Long companyId;
}
