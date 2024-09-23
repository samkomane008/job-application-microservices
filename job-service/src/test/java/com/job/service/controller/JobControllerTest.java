package com.job.service.controller;

import com.job.service.controller.dto.JobDTO;
import com.job.service.model.Job;
import com.job.service.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void findAll_ShouldReturnListOfJobs_WhenJobsExist() throws Exception {
        // Arrange
        Job job1 = new Job(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0, 250000.0, "Johannesburg, SA", 1L);
        Job job2 = new Job(2L, "DevOps Engineer", "DevOps Engineer", 20000.0, 300000.0, "Cape Town, SA", 2L);
        when(jobService.findAllJobs()).thenReturn(Arrays.asList(job1, job2));

        // Act & Assert
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("QA Engineer"))
                .andExpect(jsonPath("$[1].title").value("DevOps Engineer"));

        verify(jobService, times(1)).findAllJobs();
    }

    @Test
    void findAll_ShouldReturnNotFound_WhenNoJobsExist() throws Exception {
        // Arrange
        when(jobService.findAllJobs()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/jobs"))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).findAllJobs();
    }

    @Test
    void createJob_ShouldReturnCreatedJob_WhenJobIsCreated() throws Exception {
        // Arrange
        Job job = new Job(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0, 250000.0, "Johannesburg, SA", 1L);
        when(jobService.create(any(Job.class))).thenReturn(job);

        // Act & Assert
        mockMvc.perform(post("/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("QA Engineer"));

        verify(jobService, times(1)).create(any(Job.class));
    }

    @Test
    void findJob_ShouldReturnJobDTO_WhenJobExists() throws Exception {
        // Arrange
        JobDTO jobDTO = new JobDTO(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0, 250000.0, "Johannesburg, SA", null, null);
        when(jobService.getJobById(1L)).thenReturn(jobDTO);

        // Act & Assert
        mockMvc.perform(get("/jobs/{jobId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("QA Engineer"));

        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    void findJob_ShouldReturnNotFound_WhenJobDoesNotExist() throws Exception {
        // Arrange
        when(jobService.getJobById(1L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/jobs/{jobId}", 1L))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).getJobById(1L);
    }

    @Test
    void updateJob_ShouldReturnSuccessMessage_WhenJobIsUpdated() throws Exception {
        // Arrange
        Job job = new Job(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0, 250000.0, "Johannesburg, SA", 1L);
        when(jobService.updateJob(eq(1L), any(Job.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/jobs/{jobId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isOk())
                .andExpect(content().string("Job Updated Successfully!"));

        verify(jobService, times(1)).updateJob(eq(1L), any(Job.class));
    }

    @Test
    void updateJob_ShouldReturnNotFound_WhenJobDoesNotExist() throws Exception {
        // Arrange
        Job job = new Job(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0, 250000.0, "Johannesburg, SA", 1L);
        when(jobService.updateJob(eq(1L), any(Job.class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/jobs/{jobId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job Not Found!"));

        verify(jobService, times(1)).updateJob(eq(1L), any(Job.class));
    }

    @Test
    void deleteJob_ShouldReturnSuccessMessage_WhenJobIsDeleted() throws Exception {
        // Arrange
        when(jobService.deleteJob(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/jobs/{jobId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Job Deleted Successfully!"));

        verify(jobService, times(1)).deleteJob(1L);
    }

    @Test
    void deleteJob_ShouldReturnNotFound_WhenJobDoesNotExist() throws Exception {
        // Arrange
        when(jobService.deleteJob(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/jobs/{jobId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job Not Found!"));

        verify(jobService, times(1)).deleteJob(1L);
    }
}
