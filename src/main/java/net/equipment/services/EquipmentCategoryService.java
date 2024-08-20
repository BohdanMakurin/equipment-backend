//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.EquipmentCategory;
import net.equipment.repositories.EquipmentCategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EquipmentCategoryService {
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public EquipmentCategory addEquipmentCategory(AddEquipmentCategoryRequest req) {
        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setName(req.getName());
        return equipmentCategoryRepository.save(equipmentCategory);
    }

    public List<EquipmentCategory> getAllEquipmentCategory() {
        return equipmentCategoryRepository.findAll();
    }

    public EquipmentCategory getEquipmentCategoryById(Long equipmentCategoryId) {
        return equipmentCategoryRepository.findById(equipmentCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Category with this id does not exist" + equipmentCategoryId));
    }

    public void deleteEquipment(Long equipmentCategoryId) {
        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Category with this id does not exist" + equipmentCategoryId));
        equipmentCategoryRepository.deleteById(equipmentCategoryId);
    }

}
