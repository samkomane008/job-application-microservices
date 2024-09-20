package com.company.service.service;

import com.company.service.model.Company;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class CompanyServiceTest {

    private final CompanyService companyService;

    /**
     * Test for success scenario
     */
    @Test
    void createCompany() {
        boolean companyCreated = companyService.createCompany(companyRequest());
        Assert.assertTrue(companyCreated);
    }

    @Test
    void getCompanies() {
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