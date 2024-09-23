package com.company.service.controller;

import com.company.service.model.Company;
import com.company.service.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.boot.test.mock.mockito.MockBean;

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

@ExtendWith(SpringExtension.class)
@WebMvcTest(CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    // Utility method to convert an object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createCompany_ShouldReturnCreatedStatus_WhenCompanyIsSuccessfullyCreated() throws Exception {
        // Arrange
        Company company = new Company();
        when(companyService.createCompany(any(Company.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(company)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Company Added Successfully!"));

        verify(companyService, times(1)).createCompany(any(Company.class));
    }

    @Test
    void createCompany_ShouldReturnBadRequest_WhenCompanyCreationFails() throws Exception {
        // Arrange
        Company company = new Company();
        when(companyService.createCompany(any(Company.class))).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(company)))
                .andExpect(status().isBadRequest());

        verify(companyService, times(1)).createCompany(any(Company.class));
    }

    @Test
    void getCompanies_ShouldReturnOkStatusAndListOfCompanies() throws Exception {
        // Arrange
        List<Company> companies = Arrays.asList(new Company(), new Company());
        when(companyService.getCompanies()).thenReturn(companies);

        // Act & Assert
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(companies.size()));

        verify(companyService, times(1)).getCompanies();
    }

    @Test
    void updateCompany_ShouldReturnOkStatusAndUpdatedCompany_WhenCompanyIsSuccessfullyUpdated() throws Exception {
        // Arrange
        Long companyId = 1L;
        Company company = new Company();
        Company updatedCompany = new Company();
        when(companyService.updateCompany(eq(companyId), any(Company.class))).thenReturn(updatedCompany);

        // Act & Assert
        mockMvc.perform(put("/companies/{companyId}", companyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(company)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCompany.getId()));

        verify(companyService, times(1)).updateCompany(eq(companyId), any(Company.class));
    }

    @Test
    void deleteCompany_ShouldReturnOkStatus_WhenCompanyIsSuccessfullyDeleted() throws Exception {
        // Arrange
        Long companyId = 1L;
        when(companyService.deleteCompany(companyId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/companies/{companyId}", companyId))
                .andExpect(status().isOk())
                .andExpect(content().string("Company Deleted Successfully!"));

        verify(companyService, times(1)).deleteCompany(companyId);
    }

    @Test
    void deleteCompany_ShouldReturnNotFoundStatus_WhenCompanyIsNotFound() throws Exception {
        // Arrange
        Long companyId = 1L;
        when(companyService.deleteCompany(companyId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/companies/{companyId}", companyId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company with companyId " + companyId + " not found!"));

        verify(companyService, times(1)).deleteCompany(companyId);
    }

    @Test
    void getCompany_ShouldReturnOkStatusAndCompany_WhenCompanyIsFound() throws Exception {
        // Arrange
        Long companyId = 1L;
        Company company = new Company();
        when(companyService.getCompany(companyId)).thenReturn(company);

        // Act & Assert
        mockMvc.perform(get("/companies/{companyId}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getId()));

        verify(companyService, times(1)).getCompany(companyId);
    }
}
