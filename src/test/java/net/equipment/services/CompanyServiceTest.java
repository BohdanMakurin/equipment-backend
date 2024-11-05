package net.equipment.services;

import net.equipment.dto.CompanyDto;
import net.equipment.dto.CreateCompanyRequest;
import net.equipment.dto.UpdateCompanyRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.UserRepository;
import net.equipment.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CompanyService companyService;

    private Company company;
    private User admin;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new User();
        admin.setId(1L);

        company = new Company();
        company.setCompanyId(1L);
        company.setName("Test Company");
        company.setDescription("Test Description");
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        company.setAdmin(admin);
    }

    @Test
    public void testCreateCompany() {
        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setAdminId(1L);
        request.setName("New Company");
        request.setDescription("New Description");


        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));


        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            company.setCompanyId(1L);
            return company;
        });


        Company createdCompany = companyService.createCompany(request);


        assertNotNull(createdCompany);
        assertEquals("New Company", createdCompany.getName());
        assertEquals("New Description", createdCompany.getDescription());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    public void testCreateCompany_AdminNotFound() {

        CreateCompanyRequest request = new CreateCompanyRequest();
        request.setAdminId(1L);
        request.setName("New Company");
        request.setDescription("New Description");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> companyService.createCompany(request));
    }

    @Test
    public void testGetAllCompanies() {

        when(companyRepository.findAll()).thenReturn(List.of(company));


        List<Company> companies = companyService.getAllCompanies();


        assertNotNull(companies);
        assertEquals(1, companies.size());
        assertEquals("Test Company", companies.get(0).getName());
    }

    @Test
    public void testGetCompanyById() {

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));


        Company foundCompany = companyService.getCompanyById(1L);


        assertNotNull(foundCompany);
        assertEquals("Test Company", foundCompany.getName());
    }

    @Test
    public void testGetCompanyById_ThrowsException() {

        when(companyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> companyService.getCompanyById(2L));
    }

    @Test
    public void testUpdateCompany() {

        UpdateCompanyRequest updateRequest = new UpdateCompanyRequest();
        updateRequest.setName("Updated Company");
        updateRequest.setDescription("Updated Description");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);


        CompanyDto updatedCompany = companyService.updateCompany(1L, updateRequest);


        assertNotNull(updatedCompany);
        assertEquals("Updated Company", updatedCompany.getName());
        assertEquals("Updated Description", updatedCompany.getDescription());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    public void testDeleteCompany() {

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company)).thenReturn(Optional.empty());

        companyService.deleteCompany(1L);

        assertThrows(ResourceNotFoundException.class, () -> companyService.getCompanyById(1L));

        verify(companyRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetCompanyByAdminId() throws Exception {

        when(companyRepository.findByAdminId(1L)).thenReturn(List.of(company));

        List<Company> companies = companyService.getCompanyByAdminId(1L);

        assertNotNull(companies);
        assertEquals(1, companies.size());
        assertEquals("Test Company", companies.get(0).getName());
    }

    @Test
    public void testGetCompanyByAdminId_ThrowsException() {

        when(companyRepository.findByAdminId(2L)).thenReturn(Collections.emptyList());
        
        assertThrows(Exception.class, () -> companyService.getCompanyByAdminId(2L));
    }
}
