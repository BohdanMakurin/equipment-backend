package net.equipment.services;

import net.equipment.dto.AddEquipmentRequest;
import net.equipment.dto.EquipmentDto;
import net.equipment.dto.UpdateEquipmentRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.*;
import net.equipment.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    private Equipment equipment;
    private EquipmentCategory category;
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

        category = new EquipmentCategory();
        category.setCategoryId(1L);
        category.setName("Test Category");

        equipment = new Equipment();
        equipment.setCompany(company);
        equipment.setCategory(category);
        equipment.setName("Test Equipment");
        equipment.setDescription("Test Description");
        equipment.setSerialNumber("SN12345");
        equipment.setCreatedAt(LocalDateTime.now());
        equipment.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testAddEquipment() throws ResourceNotFoundException {
        AddEquipmentRequest request = new AddEquipmentRequest();
        request.setName("New Equipment"); // Ensure this is set properly
        request.setDescription("Test Description");
        request.setSerialNumber("123456");
        request.setCategoryId(1L);
        request.setCompanyId(1L);

        EquipmentCategory mockCategory = new EquipmentCategory();
        mockCategory.setCategoryId(1L);

        Company mockCompany = new Company();
        mockCompany.setCompanyId(1L);

        when(equipmentCategoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(mockCompany));
        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(invocation -> {
            Equipment equipment = invocation.getArgument(0);
            equipment.setEquipmentId(1L);  // Mock the ID being set after save
            return equipment;
        });

        Equipment addedEquipment = equipmentService.addEquipment(request);

        assertNotNull(addedEquipment);
        assertEquals("New Equipment", addedEquipment.getName());  // Assert that the name is set correctly
    }

    @Test
    public void testAddEquipment_CategoryNotFound() {
        AddEquipmentRequest request = new AddEquipmentRequest();
        request.setName("New Equipment");
        request.setDescription("New Description");
        request.setCategoryId(1L);
        request.setCompanyId(1L);
        request.setSerialNumber("SN54321");

        when(equipmentCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.addEquipment(request));
    }

    @Test
    public void testAddEquipment_CompanyNotFound() {
        AddEquipmentRequest request = new AddEquipmentRequest();
        request.setName("New Equipment");
        request.setDescription("New Description");
        request.setCategoryId(1L);
        request.setCompanyId(1L);
        request.setSerialNumber("SN54321");

        when(equipmentCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.addEquipment(request));
    }

    @Test
    public void testGetEquipmentById() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        Equipment foundEquipment = equipmentService.getEquipmentById(1L);

        assertNotNull(foundEquipment);
        assertEquals("Test Equipment", foundEquipment.getName());
    }

    @Test
    public void testGetEquipmentById_ThrowsException() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.getEquipmentById(1L));
    }

    @Test
    public void testGetAllEquipment() {
        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));

        List<Equipment> equipmentList = equipmentService.getAllEquipment();

        assertNotNull(equipmentList);
        assertEquals(1, equipmentList.size());
        assertEquals("Test Equipment", equipmentList.get(0).getName());
    }

    @Test
    public void testUpdateEquipment() {
        UpdateEquipmentRequest updateRequest = new UpdateEquipmentRequest();
        updateRequest.setName("Updated Equipment");
        updateRequest.setDescription("Updated Description");
        updateRequest.setSerialNumber("SN67890");

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        Equipment updatedEquipment = equipmentService.updateEquipment(1L, updateRequest);

        assertNotNull(updatedEquipment);
        assertEquals("Updated Equipment", updatedEquipment.getName());
        assertEquals("Updated Description", updatedEquipment.getDescription());
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
    }

    @Test
    public void testDeleteEquipment() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        equipmentService.deleteEquipment(1L);

        verify(equipmentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEquipment_ThrowsException() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.deleteEquipment(1L));
    }

    @Test
    public void testGetEquipmentByAdminId() throws Exception {
        when(equipmentRepository.findByAdminId(1L)).thenReturn(List.of(equipment));

        List<EquipmentDto> equipmentList = equipmentService.getEquipmentByAdminId(1L);

        assertNotNull(equipmentList);
        assertEquals(1, equipmentList.size());
        assertEquals("Test Equipment", equipmentList.get(0).getName());
    }

    @Test
    public void testGetEquipmentByAdminId_ThrowsException() {
        when(equipmentRepository.findByAdminId(1L)).thenReturn(List.of());

        assertThrows(Exception.class, () -> equipmentService.getEquipmentByAdminId(1L));
    }
}
