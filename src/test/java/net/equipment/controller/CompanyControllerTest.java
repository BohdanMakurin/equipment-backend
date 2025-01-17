package net.equipment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.CreateCompanyRequest;
import net.equipment.dto.UpdateCompanyRequest;
import net.equipment.models.Company;
import net.equipment.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    }

    @Test
    @WithMockUser
    void testCreateCompany() throws Exception {
        CreateCompanyRequest request = new CreateCompanyRequest();

        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("New Company");
        company.setDescription("Description");

        when(companyService.createCompany(any(CreateCompanyRequest.class))).thenReturn(company);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Company"))
                .andExpect(jsonPath("$.description").value("Description"));

        verify(companyService, times(1)).createCompany(any(CreateCompanyRequest.class));
    }

    @Test
    @WithMockUser
    void testGetCompanyById() throws Exception {
        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("Test Company");
        company.setDescription("Test Description");

        when(companyService.getCompanyById(1L)).thenReturn(company);

        mockMvc.perform(get("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Company"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(companyService, times(1)).getCompanyById(1L);
    }

    @Test
    @WithMockUser
    void testGetAllCompanies() throws Exception {
        Company company1 = new Company();
        company1.setCompanyId(1L);
        company1.setName("Company 1");
        company1.setDescription("Description 1");

        Company company2 = new Company();
        company2.setCompanyId(2L);
        company2.setName("Company 2");
        company2.setDescription("Description 2");

        List<Company> companies = Arrays.asList(company1, company2);

        when(companyService.getAllCompanies()).thenReturn(companies);

        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Company 1"))
                .andExpect(jsonPath("$[1].name").value("Company 2"));

        verify(companyService, times(1)).getAllCompanies();
    }

    @Test
    @WithMockUser
    void testUpdateCompany() throws Exception {
        UpdateCompanyRequest updateRequest = new UpdateCompanyRequest("Updated Company", "Updated Description");

        CompanyDto company = new CompanyDto();
        company.setCompanyId(1L);
        company.setName("Updated Company");
        company.setDescription("Updated Description");

        when(companyService.updateCompany(1L, updateRequest)).thenReturn(company);

        mockMvc.perform(put("/api/companies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(companyService, times(1)).updateCompany(1L, updateRequest);
    }

    @Test
    @WithMockUser
    void testDeleteCompany() throws Exception {
        doNothing().when(companyService).deleteCompany(1L);

        mockMvc.perform(delete("/api/companies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Company deleted successfully"));

        verify(companyService, times(1)).deleteCompany(1L);
    }

    @Test
    @WithMockUser
    void testGetCompanyByAdminId() throws Exception {
        Company company1 = new Company();
        company1.setCompanyId(1L);
        company1.setName("Company 1");
        company1.setDescription("Description 1");

        Company company2 = new Company();
        company2.setCompanyId(2L);
        company2.setName("Company 2");
        company2.setDescription("Description 2");

        List<Company> companies = Arrays.asList(company1, company2);

        when(companyService.getCompanyByAdminId(1L)).thenReturn(companies);

        mockMvc.perform(get("/api/companies/byAdmin/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Company 1"))
                .andExpect(jsonPath("$[1].name").value("Company 2"));

        verify(companyService, times(1)).getCompanyByAdminId(1L);
    }
}
