package com.job.service.service;

import com.job.service.clients.CompanyClient;
import com.job.service.clients.ReviewClient;
import com.job.service.controller.dto.JobDTO;
import com.job.service.external.Company;
import com.job.service.external.Review;
import com.job.service.mapper.MapToJobWithCompanyDto;
import com.job.service.repository.JobRepository;
import com.job.service.model.Job;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyClient companyClient;
    private final ReviewClient reviewClient;

    // Find all jobs
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    // create job
    public Job create(Job job) {
        return jobRepository.save(job);
    }

    // Find job by id
    @CircuitBreaker(name = "companyBreaker")
    public JobDTO getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(RuntimeException::new);
        assert job != null;
        return convertToDto(job);
    }

    // Update job
    public boolean updateJob(Long jobId, Job updateJob) {
        if (jobId != null && updateJob != null) {
            Job job = jobRepository.findById(jobId).orElse(null);
            if (job != null) {
                job.setDescription(updateJob.getDescription());
                job.setLocation(updateJob.getLocation());
                job.setTitle(updateJob.getTitle());
                job.setMinSalary(updateJob.getMinSalary());
                job.setMaxSalary(updateJob.getMaxSalary());
                jobRepository.save(job);
                return true;
            }
        }
        return false;
    }

    // Delete job
    public boolean deleteJob(Long jobId) {
        if (jobId != null) {
            jobRepository.deleteById(jobId);
            return true;
        } else {
            return false;
        }
    }

    private JobDTO convertToDto(Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());
        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());
        return MapToJobWithCompanyDto.mapToJobWithCompanyDto(job, company, reviews);
    }
}
