
package net.equipment.mapper;

import net.equipment.dto.CompanyDto;
import net.equipment.models.Company;

public class CompanyMapper {
    public CompanyMapper() {
    }

    public static CompanyDto mapToCompanyDto(Company company) {
        return new CompanyDto(
                company.getCompanyId(),
                company.getAdmin(),
                company.getName(),
                company.getDescription(),
                company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getEmployees());
    }

    public static Company mapToCompany(CompanyDto companyDto) {
        return new Company(
                companyDto.getCompanyId(),
                companyDto.getAdmin(),
                companyDto.getName(),
                companyDto.getDescription(),
                companyDto.getCreatedAt(),
                companyDto.getUpdatedAt(),
                companyDto.getEmployees());
    }
}
