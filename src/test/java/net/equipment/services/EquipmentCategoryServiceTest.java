package net.equipment.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.dto.UpdateEquipmentCategoryRequest;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.User;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.UserRepository;
import net.equipment.services.EquipmentCategoryService;
import net.equipment.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

public class EquipmentCategoryServiceTest {

    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Mock
    private UserRepository userRepository;

    private EquipmentCategoryService equipmentCategoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        equipmentCategoryService = new EquipmentCategoryService(equipmentCategoryRepository, userRepository);
    }

    @Test
    public void testAddEquipmentCategory() {
        // Arrange
        Long adminId = 1L;
        AddEquipmentCategoryRequest request = new AddEquipmentCategoryRequest();
        request.setName("New Category");
        request.setAdminId(adminId);
        request.setExpirationPeriodInMonths(12);

        User mockAdmin = new User();
        mockAdmin.setId(adminId);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(mockAdmin));
        when(equipmentCategoryRepository.save(any(EquipmentCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EquipmentCategory result = equipmentCategoryService.addEquipmentCategory(request);

        // Assert
        assertNotNull(result);
        assertEquals("New Category", result.getName());
        assertEquals(mockAdmin, result.getAdmin());
        assertEquals(12, result.getExpirationPeriodInMonths());
    }

    @Test
    public void testAddEquipmentCategory_AdminNotFound() {
        // Arrange
        Long adminId = 1L;
        AddEquipmentCategoryRequest request = new AddEquipmentCategoryRequest();
        request.setName("New Category");
        request.setAdminId(adminId);
        request.setExpirationPeriodInMonths(12);

        when(userRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            equipmentCategoryService.addEquipmentCategory(request);
        });

        assertEquals("Admin with id 1 not found", thrown.getMessage());
    }

    @Test
    public void testGetAllEquipmentCategory() {
        // Arrange
        EquipmentCategory category1 = new EquipmentCategory();
        category1.setName("Category 1");

        EquipmentCategory category2 = new EquipmentCategory();
        category2.setName("Category 2");

        when(equipmentCategoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // Act
        List<EquipmentCategory> categories = equipmentCategoryService.getAllEquipmentCategory();

        // Assert
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertEquals("Category 1", categories.get(0).getName());
        assertEquals("Category 2", categories.get(1).getName());
    }

    @Test
    public void testGetEquipmentCategoryById() {
        // Arrange
        Long categoryId = 1L;
        EquipmentCategory category = new EquipmentCategory();
        category.setCategoryId(categoryId);
        category.setName("Category 1");

        when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        EquipmentCategory result = equipmentCategoryService.getEquipmentCategoryById(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(categoryId, result.getCategoryId());
        assertEquals("Category 1", result.getName());
    }

    @Test
    public void testGetEquipmentCategoryById_NotFound() {
        // Arrange
        Long categoryId = 1L;

        when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            equipmentCategoryService.getEquipmentCategoryById(categoryId);
        });

        assertEquals("Equipment Category with this id does not exist: 1", thrown.getMessage());
    }

    @Test
    public void testDeleteEquipment() {
        // Arrange
        Long categoryId = 1L;
        EquipmentCategory category = new EquipmentCategory();
        category.setCategoryId(categoryId);

        when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        equipmentCategoryService.deleteEquipment(categoryId);

        // Assert
        verify(equipmentCategoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteEquipment_NotFound() {
        // Arrange
        Long categoryId = 1L;

        when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            equipmentCategoryService.deleteEquipment(categoryId);
        });

        assertEquals("Equipment Category with this id does not exist: 1", thrown.getMessage());
    }

    @Test
    public void testUpdateEquipmentCategory() {
        // Arrange
        Long categoryId = 1L;
        UpdateEquipmentCategoryRequest updateRequest = new UpdateEquipmentCategoryRequest();
        updateRequest.setName("Updated Category");
        updateRequest.setExpirationPeriodInMonths(24);

        EquipmentCategory existingCategory = new EquipmentCategory();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setName("Old Category");
        existingCategory.setExpirationPeriodInMonths(12);

        when(equipmentCategoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(equipmentCategoryRepository.save(any(EquipmentCategory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EquipmentCategory updatedCategory = equipmentCategoryService.updateEquipmentCategory(categoryId, updateRequest);

        // Assert
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
        assertEquals(24, updatedCategory.getExpirationPeriodInMonths());
    }
}
