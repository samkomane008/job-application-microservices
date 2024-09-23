package com.job.service.controller.dto;

import com.job.service.external.Company;
import com.job.service.external.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JobDTO {

    private Long id;
    private String title;
    private String description;
    private Double minSalary;
    private Double maxSalary;
    private String location;
    private Company company;
    private List<Review> reviews;
}
