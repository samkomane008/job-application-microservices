package com.job.service.controller;

import com.job.service.controller.dto.JobDTO;
import com.job.service.model.Job;
import com.job.service.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // Get all the jobs
    @GetMapping()
    public ResponseEntity<List<Job>> findAll() {
        List<Job> jobs = jobService.findAllJobs();
        if (!jobs.isEmpty()) {
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create job
    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return new ResponseEntity<>(jobService.create(job), HttpStatus.CREATED);
    }

    // Get job by id
    @GetMapping("/{jobId}")
    public ResponseEntity<JobDTO> findJob(@PathVariable Long jobId) {
        JobDTO jobDTO = jobService.getJobById(jobId);
        if (jobDTO != null) {
            return new ResponseEntity<>(jobDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Update job
    @PutMapping("/{jobId}")
    public ResponseEntity<String> updateJob(@PathVariable Long jobId, @RequestBody Job job) {
        boolean isJobUpdated = jobService.updateJob(jobId, job);
        if (isJobUpdated) {
            return new ResponseEntity<>("Job Updated Successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Job Not Found!", HttpStatus.NOT_FOUND);
        }
    }

    // Delete job
    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId) {
        boolean isJobDeleted = jobService.deleteJob(jobId);
        if (isJobDeleted) {
            return new ResponseEntity<>("Job Deleted Successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Job Not Found!", HttpStatus.NOT_FOUND);
        }
    }
}
