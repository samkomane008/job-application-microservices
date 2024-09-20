package com.company.service.service;

import com.company.service.clients.ReviewClients;
import com.company.service.dto.ReviewMessage;
import com.company.service.model.Company;
import com.company.service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ReviewClients reviewClients;

    // create company
    public boolean createCompany(Company company) {
         if (company != null) {
             companyRepository.save(company);
             return true;
         } else {
             return false;
         }
    }

    // get companies
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    // update company
    public Company updateCompany(Long companyId, Company company) {
        Company updatedCompany = companyRepository.findById(companyId).orElse(null);
        if (updatedCompany != null) {
            updatedCompany.setDescription(company.getDescription());
            updatedCompany.setName(company.getName());
        }
        return companyRepository.save(updatedCompany);
    }

    // delete company
    public boolean deleteCompany(Long companyId) {
        if (companyId != null) {
            companyRepository.deleteById(companyId);
            return true;
        } else {
            return false;
        }
    }

    // get company by id
    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    public void updateCompanyRating(ReviewMessage reviewMessage) {
        System.out.println("Testing..." + reviewMessage.getDescription());
        Company company = companyRepository.findById(reviewMessage.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found  "+ reviewMessage.getCompanyId()));

        double averageRating = reviewClients.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepository.save(company);
    }
}
