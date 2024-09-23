package com.job.service.external;


import lombok.*;

@Builder
@Getter
public class Review {

    private final Long Id;
    private final String title;
    private final String description;
    private final Double rating;
    private final Long companyId;
}
