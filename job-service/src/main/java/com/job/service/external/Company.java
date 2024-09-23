package com.job.service.external;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Company {

    private Long id;
    private String name;
    private String description;
    private Double rating;
}
