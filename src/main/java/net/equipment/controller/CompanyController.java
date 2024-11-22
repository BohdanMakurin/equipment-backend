
package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.CreateCompanyRequest;
import net.equipment.dto.UpdateCompanyRequest;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller for handling company-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting companies.
 */
@AllArgsConstructor
@RestController
@RequestMapping({"/api/companies"})
public class CompanyController {
    private final CompanyService companyService;

    /**
     * Creates a new company.
     *
     * @param company the request object containing company details
     * @return a ResponseEntity containing the newly created company and HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CreateCompanyRequest company) {
        Company newcompany = companyService.createCompany(company);
        return new ResponseEntity<>(newcompany, HttpStatus.CREATED);
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param companyId the ID of the company to retrieve
     * @return a ResponseEntity containing the company and HTTP status 200 (OK)
     */
    @GetMapping({"{id}"})
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return ResponseEntity.ok(company);
    }

    /**
     * Retrieves a list of all companies.
     *
     * @return a ResponseEntity containing a list of companies and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    /**
     * Updates an existing company by its ID.
     *
     * @param companyId      the ID of the company to update
     * @param updatedCompany the request object containing updated company details
     * @return a ResponseEntity containing the updated company and HTTP status 200 (OK)
     */
    @PutMapping({"{id}"})
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable("id") Long companyId, @RequestBody UpdateCompanyRequest updatedCompany) {
        CompanyDto companyDto = companyService.updateCompany(companyId, updatedCompany);
        return ResponseEntity.ok(companyDto);
    }

    /**
     * Deletes a company by its ID.
     *
     * @param companyId the ID of the company to delete
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteCompany(@PathVariable("id") Long companyId) {
        companyService.deleteCompany(companyId);
        return ResponseEntity.ok("Company deleted successfully");
    }

    /**
     * Retrieves companies by the admin's user ID.
     *
     * @param adminId the ID of the admin user
     * @return a ResponseEntity containing a list of companies managed by the admin and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<Company>> getCompanyByAdminId(@PathVariable("id") Long adminId) throws Exception {
        List<Company> companies = companyService.getCompanyByAdminId(adminId);
        return ResponseEntity.ok(companies);
    }
}
