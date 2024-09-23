package com.job.service.service;

import com.job.service.clients.CompanyClient;
import com.job.service.clients.ReviewClient;
import com.job.service.controller.dto.JobDTO;
import com.job.service.external.Company;
import com.job.service.external.Review;
import com.job.service.mapper.MapToJobWithCompanyDto;
import com.job.service.model.Job;
import com.job.service.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyClient companyClient;

    @Mock
    private ReviewClient reviewClient;

    @InjectMocks
    private JobService jobService;

    private Job job;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobService = new JobService(jobRepository, companyClient, reviewClient);

        job = Job.builder()
                .id(1L)
                .title("Software Engineer")
                .description("Develop and maintain software applications")
                .location("Remote")
                .minSalary(50000.0)
                .maxSalary(100000.0)
                .companyId(1L)
                .build();
    }

    @Test
    void findAllJobs_ShouldReturnListOfJobs() {
        // Arrange
        List<Job> jobs = Collections.singletonList(job);
        when(jobRepository.findAll()).thenReturn(jobs);

        // Act
        List<Job> result = jobService.findAllJobs();

        // Assert
        assertEquals(jobs.size(), result.size());
        assertEquals(jobs.get(0).getTitle(), result.get(0).getTitle());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void create_ShouldSaveAndReturnJob() {
        // Arrange
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        // Act
        Job result = jobService.create(job);

        // Assert
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void getJobById_ShouldReturnJobDTO_WhenJobExists() {
        // Arrange
        Company company = Company.builder()
                .id(1L)
                .name("Tech Co.")
                .description("A technology company")
                .rating(4.5)
                .build();

        Review review = Review.builder()
                .companyId(1L)
                .description("")
                .Id(1L)
                .rating(5.0)
                .title("")
                .build();
        List<Review> reviews = Collections.singletonList(review);

        Job job = Job.builder().id(1L)
                .title("QA Engineer")
                .description("Quality Assurance Engineer")
                .minSalary(18800.0)
                .maxSalary(250000.0).location("Johannesburg, SA")
                .companyId(1L)
                .build();

        try (MockedStatic<MapToJobWithCompanyDto> mockedStatic = Mockito.mockStatic(MapToJobWithCompanyDto.class)) {
            mockedStatic.when(() -> MapToJobWithCompanyDto.mapToJobWithCompanyDto(job, company, reviews))
                    .thenReturn(new JobDTO(1L, "QA Engineer", "Quality Assurance Engineer", 18800.0,
                            250000.0, "Johannesburg, SA", company, reviews));

            when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
            when(companyClient.getCompany(1L)).thenReturn(company);
            when(reviewClient.getReviews(1L)).thenReturn(reviews);

            // Act
            JobDTO jobDTO = jobService.getJobById(1L);

            // Assert
            assertNotNull(jobDTO);
            assertEquals(job.getTitle(), jobDTO.getTitle());
            verify(jobRepository, times(1)).findById(1L);
            verify(companyClient, times(1)).getCompany(1L);
            verify(reviewClient, times(1)).getReviews(1L);
        }
    }


    @Test
    void getJobById_ShouldThrowException_WhenJobDoesNotExist() {
        // Arrange
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> jobService.getJobById(1L));
        verify(jobRepository, times(1)).findById(1L);
        verify(companyClient, never()).getCompany(anyLong());
        verify(reviewClient, never()).getReviews(anyLong());
    }

    @Test
    void updateJob_ShouldUpdateAndReturnTrue_WhenJobExists() {
        // Arrange
        Job updatedJob = Job.builder()
                .title("Updated Job Title")
                .description("Updated description")
                .location("New York")
                .minSalary(60000.0)
                .maxSalary(120000.0)
                .companyId(1L)
                .build();

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(updatedJob);

        // Act
        boolean isUpdated = jobService.updateJob(1L, updatedJob);

        // Assert
        assertTrue(isUpdated);
        verify(jobRepository, times(1)).findById(1L);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void updateJob_ShouldReturnFalse_WhenJobDoesNotExist() {
        // Arrange
        when(jobRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean isUpdated = jobService.updateJob(1L, job);

        // Assert
        assertFalse(isUpdated);
        verify(jobRepository, times(1)).findById(1L);
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void deleteJob_ShouldReturnTrue_WhenJobExists() {
        // Act
        boolean isDeleted = jobService.deleteJob(1L);

        // Assert
        assertTrue(isDeleted);
        verify(jobRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteJob_ShouldReturnFalse_WhenJobIdIsNull() {
        // Act
        boolean isDeleted = jobService.deleteJob(null);

        // Assert
        assertFalse(isDeleted);
        verify(jobRepository, never()).deleteById(anyLong());
    }
}
