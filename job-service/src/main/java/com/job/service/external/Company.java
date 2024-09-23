package com.job.service.external;

import lombok.*;

@Builder
@Getter
@Setter
public class Company {

    private Long id;
    private String name;
    private String description;
}
