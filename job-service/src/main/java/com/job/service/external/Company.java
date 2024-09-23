package com.job.service.external;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Company {

    private final Long id;
    private final String name;
    private final String description;
    private final Double rating;
}
