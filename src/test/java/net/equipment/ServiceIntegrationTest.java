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
        // Очищаем базы данных перед каждым тестом, чтобы не было старых данных

        equipmentRepository.deleteAll();
        equipmentCategoryRepository.deleteAll();
        companyRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void testCreateAdminAndCompany() {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем компанию
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        List<Company> allCompanies = companyService.getAllCompanies();
        assertEquals(1, allCompanies.size(), "Ожидается 1 компания");
    }

    @Test
    public void testCreateUsersForCompany() {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем компанию
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Создаем пользователей для компании
        for (int i = 1; i <= 3; i++) {
            createUser(i, company1);
        }

        List<UserDto> company1Users = userService.getUsersByCompanyId(company1.getCompanyId());
        assertEquals(3, company1Users.size(), "Ожидается 3 пользователя для компании");
    }

    @Test
    public void testCreateEquipmentCategories() {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем категории оборудования
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);

        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        List<EquipmentCategory> allCategories = equipmentCategoryRepository.findAll();
        assertEquals(2, allCategories.size(), "Ожидается 2 категории оборудования");
    }

    @Test
    public void testCreateAndAssignEquipment() throws Exception {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем компанию
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Создаем категории оборудования
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);
        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        // Добавляем оборудование
        for (int i = 1; i <= 5; i++) {
            createAndAssignEquipment(i, company1, i <= 2 ? category1 : category2);
        }

        List<EquipmentDto> company1Equipment = equipmentService.getEquipmentByCompanyId(company1.getCompanyId());
        assertEquals(5, company1Equipment.size(), "Ожидается 5 единиц оборудования");
    }

    @Test
    public void testRetrieveEquipmentByCategory() throws Exception {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем компанию
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Создаем категории оборудования
        EquipmentCategory category1 = createEquipmentCategory(admin, "Category 1", 12);
        EquipmentCategory category2 = createEquipmentCategory(admin, "Category 2", 24);
        equipmentCategoryRepository.save(category1);
        equipmentCategoryRepository.save(category2);

        // Добавляем оборудование
        for (int i = 1; i <= 5; i++) {
            createAndAssignEquipment(i, company1, i <= 2 ? category1 : category2);
        }

        // Извлечение оборудования по категории
        List<EquipmentDto> company1Equipment = equipmentService.getEquipmentByCompanyId(company1.getCompanyId());
        long countCategory1 = company1Equipment.stream().filter(e -> e.getCategory().getCategoryId().equals(category1.getCategoryId())).count();
        long countCategory2 = company1Equipment.stream().filter(e -> e.getCategory().getCategoryId().equals(category2.getCategoryId())).count();

        assertEquals(2, countCategory1, "Ожидается 2 единицы оборудования в первой категории");
        assertEquals(3, countCategory2, "Ожидается 3 единицы оборудования во второй категории");
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Создаем компанию
        Company company1 = createCompany(admin);
        companyRepository.save(company1);

        // Создаем первого пользователя
        User user1 = createUser(1, company1);

        // Пытаемся создать пользователя с таким же email
        User user2 = new User();
        user2.setFirstName("User2");
        user2.setLastName("Company1");
        user2.setEmail(user1.getEmail()); // Используем тот же email
        user2.setPassword(passwordEncoder.encode("password2"));
        user2.setRole(Role.ROLE_USER);
        user2.setCompany(company1);

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user2), "Ожидается ошибка уникальности email");
    }
    @Test
    public void testCreateCompanyWithNullName() {
        // Создаем объект админа
        User admin = createAdmin();
        userRepository.save(admin);

        // Попытка создать компанию с отсутствующим именем
        Company company = new Company();
        company.setAdmin(admin);
        assertThrows(ConstraintViolationException.class, () -> companyRepository.save(company), "Ожидается ошибка при сохранении компании без имени");
    }



    // Вспомогательные методы для создания объектов

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
