
package net.equipment.services;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.CreateCompanyRequest;
import net.equipment.dto.UpdateCompanyRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.mapper.CompanyMapper;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing company-related operations, including creating,
 * updating, retrieving, and deleting companies.
 */
@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new company with the provided details and assigns it to an admin.
     *
     * @param req the request object containing company creation details
     * @return the newly created Company object
     * @throws ResourceNotFoundException if the admin with the given ID is not found
     */
    public Company createCompany(CreateCompanyRequest req) {
        User existingAdmin = userRepository.findById(req.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + req.getAdminId() + " not found"));

        Company company = new Company();
        company.setName(req.getName());
        company.setDescription(req.getDescription());
        company.setUpdatedAt(LocalDateTime.now());
        company.setCreatedAt(LocalDateTime.now());
        company.setAdmin(existingAdmin);
        return companyRepository.save(company);
    }

    /**
     * Retrieves a list of all companies.
     *
     * @return a list of Company objects
     */
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    /**
     * Retrieves a company by its ID.
     *
     * @param companyId the ID of the company to be retrieved
     * @return the Company object
     * @throws ResourceNotFoundException if the company with the given ID is not found
     */
    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + companyId));
    }

    /**
     * Updates the details of an existing company based on the provided request.
     *
     * @param companyId       the ID of the company to be updated
     * @param updatedCompany  the request object containing updated company details
     * @return a CompanyDto object representing the updated company
     * @throws ResourceNotFoundException if the company with the given ID is not found
     */
    public CompanyDto updateCompany(Long companyId, UpdateCompanyRequest updatedCompany) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + companyId));

        if (updatedCompany.getName() != null) {
            company.setName(updatedCompany.getName());
        }
        if (updatedCompany.getDescription() != null) {
            company.setDescription(updatedCompany.getDescription());
        }

        company.setUpdatedAt(LocalDateTime.now());
        Company savedCompany = companyRepository.save(company);
        return CompanyMapper.mapToCompanyDto(savedCompany);
    }

    /**
     * Deletes a company by its ID.
     *
     * @param companyId the ID of the company to be deleted
     * @throws ResourceNotFoundException if the company with the given ID is not found
     */
    public void deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + companyId));
        companyRepository.deleteById(companyId);
    }

    /**
     * Retrieves companies by the admin's ID.
     *
     * @param adminId the ID of the admin
     * @return a list of Company objects associated with the admin
     * @throws Exception if no companies are found for the given admin ID
     */
    public List<Company> getCompanyByAdminId(Long adminId) throws Exception {
        List<Company> companies = companyRepository.findByAdminId(adminId);
        if (companies.isEmpty()) {
            throw new Exception("Company not found with admin id " + adminId);
        } else {
            return companies;
        }
    }
}
