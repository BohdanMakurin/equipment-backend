package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.dto.UpdateEquipmentCategoryRequest;
import net.equipment.models.EquipmentCategory;
import net.equipment.services.EquipmentCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling equipment category-related operations.
 * Provides endpoints for creating, retrieving, and deleting equipment categories.
 */
@AllArgsConstructor
@RestController
@RequestMapping({"/api/EquipmentCategory"})
public class EquipmentCategoryController {
    private final EquipmentCategoryService equipmentCategoryService;

    /**
     * Creates a new equipment category.
     *
     * @param equipmentCategory the request object containing equipment category details
     * @return a ResponseEntity containing the newly created equipment category and HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<EquipmentCategory> createEquipmentCategory(@RequestBody AddEquipmentCategoryRequest equipmentCategory) {
        EquipmentCategory newEquipmentCategory = equipmentCategoryService.addEquipmentCategory(equipmentCategory);
        return new ResponseEntity<>(newEquipmentCategory, HttpStatus.CREATED);
    }

    /**
     * Retrieves an equipment category by its ID.
     *
     * @param equipmentCategoryId the ID of the equipment category to retrieve
     * @return a ResponseEntity containing the equipment category and HTTP status 200 (OK)
     */
    @GetMapping({"{id}"})
    public ResponseEntity<EquipmentCategory> getEquipmentCategoryById(@PathVariable("id") Long equipmentCategoryId) {
        EquipmentCategory equipmentCategory = equipmentCategoryService.getEquipmentCategoryById(equipmentCategoryId);
        return ResponseEntity.ok(equipmentCategory);
    }

    /**
     * Retrieves a list of all equipment categories.
     *
     * @return a ResponseEntity containing a list of equipment categories and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<EquipmentCategory>> getAllEquipmentCategory() {
        List<EquipmentCategory> equipmentCategories = equipmentCategoryService.getAllEquipmentCategory();
        return ResponseEntity.ok(equipmentCategories);
    }

    /**
     * Deletes an equipment category by its ID.
     *
     * @param equipmentCategoryId the ID of the equipment category to delete
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipmentCategory(@PathVariable("id") Long equipmentCategoryId) {
        equipmentCategoryService.deleteEquipment(equipmentCategoryId);
        return ResponseEntity.ok("Equipment category deleted successfully");
    }

    /**
     * Retrieves equipment categories by the admin's user ID.
     *
     * @param adminId the ID of the admin user
     * @return a ResponseEntity containing a list of equipment categories managed by the admin and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<EquipmentCategory>> getEquipmentCategoryByAdminId(@PathVariable("id") Long adminId) throws Exception {
        List<EquipmentCategory> equipmentCategories = equipmentCategoryService.getEquipmentCategoryByAdminId(adminId);
        return ResponseEntity.ok(equipmentCategories);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<EquipmentCategory> updateEquipmentCategory(@PathVariable("id") Long categoryId, @RequestBody UpdateEquipmentCategoryRequest updatedEquipmentCategory) {
        EquipmentCategory equipmentCategory = equipmentCategoryService.updateEquipmentCategory(categoryId, updatedEquipmentCategory);
        return ResponseEntity.ok(equipmentCategory);
    }
}


