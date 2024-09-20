package com.company.service.controller;

import com.company.service.model.Company;
import com.company.service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // Create company
    @PostMapping
    public ResponseEntity<String> createCompany(@RequestBody Company company) {
        boolean isCompanyCreated = companyService.createCompany(company);
        if (isCompanyCreated) {
            return new ResponseEntity<>("Company Added Successfully!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Get all companies
    @GetMapping()
    public ResponseEntity<List<Company>> getCompanies() {
        return new ResponseEntity<>(companyService.getCompanies(), HttpStatus.OK);
    }


    // Update company
    @PutMapping("/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId, @RequestBody Company company) {
        return new ResponseEntity<>(companyService.updateCompany(companyId, company), HttpStatus.OK);
    }

    // Delete company
    @DeleteMapping("/{companyId}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long companyId) {
        boolean isCompanyDeleted = companyService.deleteCompany(companyId);
        if (isCompanyDeleted) {
            return new ResponseEntity<>("Company Deleted Successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Company with companyId "+companyId+" not found!",HttpStatus.NOT_FOUND);
        }
    }

    // Get company by id
    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompany(@PathVariable Long companyId) {
        return new ResponseEntity<>(companyService.getCompany(companyId), HttpStatus.OK);
    }
}
