
package net.equipment;

import net.equipment.dto.*;
import net.equipment.models.*;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.EquipmentRepository;
import net.equipment.repositories.UserRepository;
import net.equipment.services.*;
import org.junit.experimental.categories.Categories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private CompanyService companyService;
    @Autowired
    private EquipmentCategoryService equipmentCategoryService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    private Long adminId;
    private Long company1Id;
    private Long company2Id;
    private Long category1Id;
    private Long category2Id;

    @BeforeEach
    public void setUp() throws Exception {

        equipmentRepository.deleteAll();
        equipmentCategoryRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Test");
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ROLE_ADMIN);
        User savedAdmin = userService.save(admin);
        adminId = savedAdmin.getId();

        CreateCompanyRequest company1 = new CreateCompanyRequest();
        company1.setName("Test Company 1");
        company1.setDescription("Test Description 1");
        company1.setAdminId(adminId);
        Company savedCompany1 = companyService.createCompany(company1);
        company1Id = savedCompany1.getCompanyId();

        CreateCompanyRequest company2 = new CreateCompanyRequest();
        company2.setName("Test Company 2");
        company2.setDescription("Test Description 2");
        company2.setAdminId(adminId);
        Company savedCompany2 = companyService.createCompany(company2);
        company2Id = savedCompany2.getCompanyId();

        for (int i = 1; i <= 3; i++) {
            CreateUserRequest userRequest = new CreateUserRequest();
            userRequest.setFirstName("User" + i);
            userRequest.setLastName("Company1");
            userRequest.setEmail("userComp1" + i + "@test.com");
            userRequest.setPassword("password" + i);
            userRequest.setCompanyId(company1Id);
            userRequest.setRole(Role.ROLE_USER);
            authenticationService.createUser(userRequest);
        }

        for (int i = 1; i <= 3; i++) {
            CreateUserRequest userRequest = new CreateUserRequest();
            userRequest.setFirstName("User" + i);
            userRequest.setLastName("Company2");
            userRequest.setEmail("userComp2" + i + "@test.com");
            userRequest.setPassword("password" + i);
            userRequest.setCompanyId(company2Id);
            userRequest.setRole(Role.ROLE_USER);
            authenticationService.createUser(userRequest);
        }

        AddEquipmentCategoryRequest category1 = new AddEquipmentCategoryRequest();
        category1.setName("Category 1");
        category1.setAdminId(adminId);
        category1.setExpirationPeriodInMonths(12);
        category1Id = equipmentCategoryService.addEquipmentCategory(category1).getCategoryId();

        AddEquipmentCategoryRequest category2 = new AddEquipmentCategoryRequest();
        category2.setName("Category 2");
        category2.setAdminId(adminId);
        category2.setExpirationPeriodInMonths(24);
        category2Id = equipmentCategoryService.addEquipmentCategory(category2).getCategoryId();

        for (int i = 1; i <= 5; i++) {
            AddEquipmentRequest equipment = new AddEquipmentRequest();
            equipment.setName("Equipment" + i);
            equipment.setDescription("Company1");
            equipment.setCompanyId(company1Id);

            if (i <= 2) {
                equipment.setCategoryId(category1Id);
            } else {
                equipment.setCategoryId(category2Id);
            }
            equipmentService.addEquipment(equipment);
        }

        for (int i = 1; i <= 5; i++) {
            AddEquipmentRequest equipment = new AddEquipmentRequest();
            equipment.setName("Equipment" + i);
            equipment.setDescription("Company2");
            equipment.setCompanyId(company2Id);

            if (i <= 2) {
                equipment.setCategoryId(category1Id);
            } else {
                equipment.setCategoryId(category2Id);
            }
            equipmentService.addEquipment(equipment);
        }
    }

    @Test
    public void getCompaniesForAdminTest() throws Exception {
       List<Company> companies = companyService.getCompanyByAdminId(adminId);
       assertEquals(2,companies.size());

        assertTrue(companies.stream().anyMatch(company -> company.getCompanyId().equals(company1Id)));
        assertTrue(companies.stream().anyMatch(company -> company.getCompanyId().equals(company2Id)));
    }

    @Test
    public void getUsersForAdminTest() throws Exception {
        List<UserDto> users = userService.getUsersByAdminId(adminId);
        assertEquals(6,users.size());
    }

    @Test
    public void getUsersForCompany1Test() throws Exception {
        List<UserDto> users = userService.getUsersByCompanyId(company1Id);
        assertEquals(3,users.size());
    }

    @Test
    public void getUsersForCompany2Test() throws Exception {
        List<UserDto> users = userService.getUsersByCompanyId(company2Id);
        assertEquals(3,users.size());
    }

    @Test
    public void getEquipmentForCompany1Test() throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByCompanyId(company1Id);
        assertEquals(5,equipment.size());
    }

    @Test
    public void getEquipmentForCompany2Test() throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByCompanyId(company2Id);
        assertEquals(5,equipment.size());
    }

    @Test
    public void getEquipmentByAdminIdTest() throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByAdminId(adminId);
        assertEquals(10,equipment.size());
    }
    @Test
    public void getEquipmentByUserIdAndUpdateEquipmentTest() throws Exception {
        List<UserDto> users = userService.getUsersByAdminId(adminId);
        List<EquipmentDto> equipment = equipmentService.getEquipmentByAdminId(adminId);

        UpdateEquipmentRequest updatedEquipment = new UpdateEquipmentRequest();
        updatedEquipment.setName(equipment.get(0).getName());
        updatedEquipment.setDescription(equipment.get(0).getDescription());
        updatedEquipment.setSerialNumber(equipment.get(0).getSerialNumber());
        updatedEquipment.setUserId(users.get(0).getId());
        updatedEquipment.setCategoryId(equipment.get(0).getCategory().getCategoryId());
        updatedEquipment.setLocation(equipment.get(0).getLocation());
        updatedEquipment.setCompanyId(equipment.get(0).getCompany().getCompanyId());

        Equipment testEquipment = equipmentService.updateEquipment(equipment.get(0).getEquipmentId(), updatedEquipment);
        assertEquals(users.get(0).getId(),testEquipment.getUser().getId());

        List<EquipmentDto> equipmentByUser = equipmentService.getEquipmentByUserId(users.get(0).getId());

        assertEquals(1,equipmentByUser.size());
    }

    @Test
    public void getCategoriesTest() throws Exception {
        List<EquipmentCategory> categories = equipmentCategoryService.getEquipmentCategoryByAdminId(adminId);
        assertEquals(2,categories.size());
    }


}