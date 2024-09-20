package com.review.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewMessage {

    private Long Id;
    private String title;
    private String description;
    private Double rating;
    private Long companyId;
}
