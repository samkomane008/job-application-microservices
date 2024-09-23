package com.company.service.service;

import com.company.service.model.Company;
import com.company.service.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Success test for creating a company.
     */
    @Test
    void createCompany() {
        assertTrue(companyService.createCompany(companyRequest()));
    }

    /**
     * Failure test for creating a company with null request.
     */
    @Test
    void testCreateCompanyWithNullRequest() {
        assertFalse(companyService.createCompany(null));
    }

    /**
     * Success test for retrieving all companies.
     */
    @Test
    void getCompanies() {
        when(companyRepository.findAll()).thenReturn(Arrays.asList(companyObject(), companyObject()));

        // When
        List<Company> companies = companyService.getCompanies();

        // Then
        assertNotNull(companies);
        assertEquals(2, companies.size());
        assertEquals("Test company name", companies.get(0).getName());

        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void updateCompany_shouldUpdateCompany_whenCompanyExists() {
        // Arrange
        Company existingCompany = companyObject();
        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));

        Company updatedDetails = Company.builder()
                .name("Updated name company")
                .description("Updated company description")
                .rating(4.8)
                .build();

        // Act
        Company updatedCompany = companyService.updateCompany(1L, updatedDetails);

        // Assert
        assertNotNull(updatedCompany);
        assertEquals("Updated company description", updatedCompany.getDescription());
        assertEquals("Updated name company", updatedCompany.getName());

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(existingCompany);
    }

    @Test
    void updateCompany_shouldReturnNull_whenCompanyDoesNotExist() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        Company companyDetails = Company.builder()
                .name("Updated name company")
                .description("Updated company description")
                .rating(4.8)
                .build();

        // Act
        Company result = companyService.updateCompany(1L, companyDetails);

        // Assert
        assertNull(result);

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void updateCompany_shouldThrowException_whenCompanyIdIsNull() {
        // Arrange
        Company companyDetails = Company.builder()
                .name("Updated name company")
                .description("Updated company description")
                .rating(4.8)
                .build();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> companyService.updateCompany(null, companyDetails));

        verify(companyRepository, never()).findById(any());
        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    void updateCompany_shouldThrowException_whenCompanyObjectIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> companyService.updateCompany(1L, null));

        verify(companyRepository, never()).findById(any());
        verify(companyRepository, never()).save(any(Company.class));
    }




    /**
     * Success delete test for deleting user by id when user exists.
     */
    @Test
    void deleteCompanyById_shouldReturnTrue_WhenCompanyExists() {
        // Arrange
        Long companyId = 1L;
        when(companyRepository.existsById(companyId)).thenReturn(true);
        doNothing().when(companyRepository).deleteById(companyId);

        // Act
        boolean isCompanyDeleted = companyService.deleteCompany(companyId);

        // Assert
        assertTrue(isCompanyDeleted);
        verify(companyRepository, times(1)).deleteById(companyId);
    }

    @Test
    void deleteCompanyById_shouldThrowException_whenCompanyDoesNotExits() {
        // Act and Assert
        assertThrows(NullPointerException.class, () -> companyService.deleteCompany(200L));
        verify(companyRepository, never()).deleteById(anyLong());
    }

    @Test
    void getCompany() {
        Long companyId = 1L;
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyObject()));

        Company company = companyService.getCompany(companyId);
        assertNotNull(company);
        assertEquals("Test company name", company.getName());
        verify(companyRepository, times(1)).findById(companyId);
    }

    @Test
    void updateCompanyRating() {
    }

    // create company request object
    private Company companyRequest() {
        return Company.builder()
                .Id(1000L)
                .description("Sample company description")
                .name("Sample company name")
                .rating(5.0)
                .build();
    }

    // create company object
    private Company companyObject() {
        return Company.builder()
                .rating(5.8)
                .name("Test company name")
                .description("Insurance company")
                .Id(1L)
                .build();
    }
}