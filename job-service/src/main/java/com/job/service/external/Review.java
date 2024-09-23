package com.job.service.external;


import lombok.*;

@Builder
@Getter
@Setter
public class Review {

    private Long Id;
    private String title;
    private String description;
    private String rating;
    private Long companyId;
}
