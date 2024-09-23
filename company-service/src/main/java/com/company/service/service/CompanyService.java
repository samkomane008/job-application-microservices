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

    private CompanyRepository companyRepository;
    private ReviewClients reviewClients;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

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
        Company updatedCompany = null;
        if (companyId != null && company != null) {
           updatedCompany = companyRepository.findById(companyId).orElseThrow(null);
           if (updatedCompany != null) {
               updatedCompany.setDescription(company.getDescription());
               updatedCompany.setName(company.getName());
               companyRepository.save(updatedCompany);
           }
       }
        return updatedCompany;
    }

    // delete company
    public boolean deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(null);
        if (company != null) {
            companyRepository.delete(company);
            return true;
        }
        return false;
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