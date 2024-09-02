package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.models.Company;
import net.equipment.models.EquipmentCategory;
import net.equipment.services.EquipmentCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping({"/api/EquipmentCategory"})
public class EquipmentCategoryController {
    private final EquipmentCategoryService equipmentCategoryService;


    @PostMapping
    public ResponseEntity<EquipmentCategory> createEquipmentCategory(@RequestBody AddEquipmentCategoryRequest equipmentCategory) {
        EquipmentCategory newEquipmentCategory = equipmentCategoryService.addEquipmentCategory(equipmentCategory);
        return new ResponseEntity(newEquipmentCategory, HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<EquipmentCategory> getEquipmentCategoryById(@PathVariable("id") Long equipmentCategoryId) {
        EquipmentCategory equipmentCategory = equipmentCategoryService.getEquipmentCategoryById(equipmentCategoryId);
        return ResponseEntity.ok(equipmentCategory);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentCategory>> getAllEquipmentCategory() {
        List<EquipmentCategory> equipmentCategories = equipmentCategoryService.getAllEquipmentCategory();
        return ResponseEntity.ok(equipmentCategories);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipmentCategory(@PathVariable("id") Long equipmentCategoryId) {
        equipmentCategoryService.deleteEquipment(equipmentCategoryId);
        return ResponseEntity.ok("equipmentCategory deleted successfully");
    }

    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<EquipmentCategory>> getCompanyByAdminId(@PathVariable("id") Long adminId) throws Exception {
        List<EquipmentCategory> equipmentCategories = this.equipmentCategoryService.getEquipmentCategoryByAdminId(adminId);
        return ResponseEntity.ok(equipmentCategories);
    }
}
