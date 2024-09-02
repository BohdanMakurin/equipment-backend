//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.services;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.dto.CreateCompanyRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.Company;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.User;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EquipmentCategoryService {
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final UserRepository userRepository;
    public EquipmentCategory addEquipmentCategory(AddEquipmentCategoryRequest req) {
        User existingAdmin = userRepository.findById(req.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + req.getAdminId() + " not found"));

        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setName(req.getName());
        equipmentCategory.setAdmin(existingAdmin);
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

    public List<EquipmentCategory> getEquipmentCategoryByAdminId(Long adminId) throws Exception {
        List<EquipmentCategory> equipmentCategories = equipmentCategoryRepository.findByAdminId(adminId);
        if (equipmentCategories.isEmpty()) {
            throw new Exception("company not found with admin id " + adminId);
        } else {
            return equipmentCategories;
        }
    }

}
