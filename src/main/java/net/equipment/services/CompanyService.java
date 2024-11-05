//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

@RequiredArgsConstructor
@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

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

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("company with this id does not exist" + companyId));
        return company;
    }

    public CompanyDto updateCompany(Long companyId, UpdateCompanyRequest updatedCompany) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist" + companyId));
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

    public void deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + companyId));
        companyRepository.deleteById(companyId);
    }

    public List<Company> getCompanyByAdminId(Long adminId) throws Exception {
        List<Company> companies = companyRepository.findByAdminId(adminId);
        if (companies.isEmpty()) {
            throw new Exception("company not found with admin id " + adminId);
        } else {
            return companies;
        }
    }

}
