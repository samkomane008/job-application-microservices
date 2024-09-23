package com.company.service.service;

import com.company.service.model.Company;
import com.company.service.repository.CompanyRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    /**
     * Success test for creating a company.
     */
    @Test
    void createCompany() {
        boolean createCompany = companyService.createCompany(companyRequest());
        Assert.assertTrue(createCompany);
    }

    /**
     * Failure test for creating a company with null request.
     */
    @Test
    void testCreateCompanyWithNullRequest() {
        boolean createCompany = companyService.createCompany(null);
        Assert.assertFalse(createCompany);
    }

    /**
     * Success test for retrieving all companies.
     */
    @Test
    void getCompanies() {
        // Given
        CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
        CompanyService getCompaniesService = new CompanyService(companyRepository);

        Company company1 = new Company(100L, "Samloryx", "Consulting company", 5.0);
        Company company2 = new Company(101L, "Discovery Health", "Insurance", 2.0);

        when(companyRepository.findAll()).thenReturn(Arrays.asList(company1, company2));

        // When
        List<Company> companies = getCompaniesService.getCompanies();

        // Then
        assertNotNull(companies);
        assertEquals(2, companies.size());
        assertEquals("Samloryx", companies.get(0).getName());
        assertEquals("Discovery Health", companies.get(1).getName());

        verify(companyRepository, times(1)).findAll();
    }

    @Test
    void updateCompany() {
    }

    @Test
    void deleteCompany() {
    }

    @Test
    void getCompany() {
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
}