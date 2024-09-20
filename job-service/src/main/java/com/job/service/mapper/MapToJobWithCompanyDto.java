package com.job.service.mapper;

import com.job.service.external.Company;
import com.job.service.controller.dto.JobDTO;
import com.job.service.external.Review;
import com.job.service.model.Job;

import java.util.List;

public class MapToJobWithCompanyDto {

    public static JobDTO mapToJobWithCompanyDto(Job job, Company company, List<Review> reviews) {
        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .company(company)
                .reviews(reviews)
                .build();
    }
}
