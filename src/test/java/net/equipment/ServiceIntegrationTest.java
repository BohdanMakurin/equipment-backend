package net.equipment;

import jakarta.validation.ConstraintViolationException;
import net.equipment.dto.*;
import net.equipment.models.*;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.EquipmentRepository;
import net.equipment.repositories.UserRepository;
import net.equipment.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ServiceIntegrationTest {

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Clear databases before each test to avoid old data

        equipmentRepository.deleteAll();
        equipmentCategoryRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testCreateAdminAndCompany() {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create company
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        List<Company> allCompanies = companyService.getAllCompanies();
        assertEquals(1, allCompanies.size(), "Expected 1 company");
    }

    @Test
    public void testCreateUsersForCompany() {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create company
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Create users for the company
        for (int i = 1; i <= 3; i++) {
            createUser(i, company1);
        }

        List<UserDto> company1Users = userService.getUsersByCompanyId(company1.getCompanyId());
        assertEquals(3, company1Users.size(), "Expected 3 users for the company");
    }

    @Test
    public void testCreateEquipmentCategories() {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create equipment categories
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);

        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        List<EquipmentCategory> allCategories = equipmentCategoryRepository.findAll();
        assertEquals(2, allCategories.size(), "Expected 2 equipment categories");
    }

    @Test
    public void testCreateAndAssignEquipment() throws Exception {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create company
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Create equipment categories
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);
        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        // Add equipment
        for (int i = 1; i <= 5; i++) {
            createAndAssignEquipment(i, company1, i <= 2 ? category1 : category2);
        }

        List<EquipmentDto> company1Equipment = equipmentService.getEquipmentByCompanyId(company1.getCompanyId());
        assertEquals(5, company1Equipment.size(), "Expected 5 pieces of equipment");
    }

    @Test
    public void testRetrieveEquipmentByCategory() throws Exception {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create company
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Create equipment categories
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);
        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        // Add equipment
        for (int i = 1; i <= 5; i++) {
            createAndAssignEquipment(i, company1, i <= 2 ? category1 : category2);
        }

        // Retrieve equipment by category
        List<EquipmentDto> company1Equipment = equipmentService.getEquipmentByCompanyId(company1.getCompanyId());
        long countCategory1 = company1Equipment.stream().filter(e -> e.getCategory().getCategoryId().equals(category1.getCategoryId())).count();
        long countCategory2 = company1Equipment.stream().filter(e -> e.getCategory().getCategoryId().equals(category2.getCategoryId())).count();

        assertEquals(2, countCategory1, "Expected 2 pieces of equipment in the first category");
        assertEquals(3, countCategory2, "Expected 3 pieces of equipment in the second category");
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Create company
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Create first user
        User user1 = createUser(1, company1);

        // Try to create a user with the same email
        User user2 = new User();
        user2.setFirstName("User2");
        user2.setLastName("Company1");
        user2.setEmail(user1.getEmail()); // Use the same email
        user2.setPassword(passwordEncoder.encode("password2"));
        user2.setRole(Role.ROLE_USER);
        user2.setCompany(company1);

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2), "Expected email uniqueness violation");
    }

    @Test
    public void testCreateCompanyWithNullName() {
        // Create admin user
        User admin = createAdmin();
        userRepository.save(admin);

        // Try to create a company with no name
        Company company = new Company();
        company.setAdmin(admin);
        assertThrows(ConstraintViolationException.class, () -> companyRepository.save(company), "Expected error when saving company with no name");
    }

    // Helper methods for creating objects

    private User createAdmin() {
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Test");
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ROLE_ADMIN);
        admin.setPassword(passwordEncoder.encode("adminpassword"));
        return admin;
    }

    private Company createCompany(User admin) {
        Company company = new Company();
        company.setName("Test Company 1");
        company.setAdmin(admin);
        return company;
    }

    private User createUser(int index, Company company) {
        User user = new User();
        user.setFirstName("User" + index);
        user.setLastName("Company1");
        user.setEmail("user" + index + "@test.com");
        user.setPassword(passwordEncoder.encode("password" + index));
        user.setRole(Role.ROLE_USER);
        user.setCompany(company);
        userRepository.save(user);
        return user;
    }

    private EquipmentCategory createEquipmentCategory(User admin, String name, int expirationPeriodInMonths) {
        EquipmentCategory category = new EquipmentCategory();
        category.setName(name);
        category.setExpirationPeriodInMonths(expirationPeriodInMonths);
        category.setAdmin(admin);
        return category;
    }

    private Equipment createAndAssignEquipment(int index, Company company, EquipmentCategory category) {
        Equipment equipment = new Equipment();
        equipment.setName("Equipment" + index);
        equipment.setDescription("Test Equipment");
        equipment.setCompany(company);
        equipment.setCategory(category);
        equipmentRepository.save(equipment);
        return equipment;
    }
}
