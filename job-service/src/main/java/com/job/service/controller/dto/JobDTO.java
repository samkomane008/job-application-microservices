package com.job.service.controller.dto;

import com.job.service.external.Company;
import com.job.service.external.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class JobDTO {

    private final Long id;
    private final String title;
    private final String description;
    private final Double minSalary;
    private final Double maxSalary;
    private final String location;
    private final Company company;
    private final List<Review> reviews;
}
