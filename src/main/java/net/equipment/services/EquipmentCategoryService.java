

package net.equipment.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.*;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.User;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing equipment categories, including adding, retrieving,
 * updating, and deleting equipment categories.
 */
@RequiredArgsConstructor
@Service
public class EquipmentCategoryService {

    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final UserRepository userRepository;

    /**
     * Adds a new equipment category and associates it with an admin.
     *
     * @param req the request object containing equipment category details
     * @return the newly added EquipmentCategory object
     * @throws ResourceNotFoundException if the admin with the given ID is not found
     */
    public EquipmentCategory addEquipmentCategory(AddEquipmentCategoryRequest req) {
        User existingAdmin = userRepository.findById(req.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin with id " + req.getAdminId() + " not found"));

        EquipmentCategory equipmentCategory = new EquipmentCategory();
        equipmentCategory.setName(req.getName());
        equipmentCategory.setAdmin(existingAdmin);
        equipmentCategory.setExpirationPeriodInMonths(req.getExpirationPeriodInMonths());
        System.out.println(req.getExpirationPeriodInMonths());
        return equipmentCategoryRepository.save(equipmentCategory);
    }

    /**
     * Retrieves a list of all equipment categories.
     *
     * @return a list of EquipmentCategory objects
     */
    public List<EquipmentCategory> getAllEquipmentCategory() {
        return equipmentCategoryRepository.findAll();
    }

    /**
     * Retrieves an equipment category by its ID.
     *
     * @param equipmentCategoryId the ID of the equipment category to be retrieved
     * @return the EquipmentCategory object
     * @throws ResourceNotFoundException if the equipment category with the given ID is not found
     */
    public EquipmentCategory getEquipmentCategoryById(Long equipmentCategoryId) {
        return equipmentCategoryRepository.findById(equipmentCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Category with this id does not exist: " + equipmentCategoryId));
    }

    /**
     * Deletes an equipment category by its ID.
     *
     * @param equipmentCategoryId the ID of the equipment category to be deleted
     * @throws ResourceNotFoundException if the equipment category with the given ID is not found
     */
    public void deleteEquipment(Long equipmentCategoryId) {
        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Category with this id does not exist: " + equipmentCategoryId));
        equipmentCategoryRepository.deleteById(equipmentCategoryId);
    }

    /**
     * Retrieves a list of equipment categories associated with a given admin ID.
     *
     * @param adminId the ID of the admin whose equipment categories are to be retrieved
     * @return a list of EquipmentCategory objects
     * @throws Exception if no equipment categories are found for the given admin ID
     */
    public List<EquipmentCategory> getEquipmentCategoryByAdminId(Long adminId) throws Exception {
        List<EquipmentCategory> equipmentCategories = equipmentCategoryRepository.findByAdminId(adminId);
        if (equipmentCategories.isEmpty()) {
            throw new Exception("Equipment categories not found with admin id " + adminId);
        } else {
            return equipmentCategories;
        }
    }

    /**
     * Updates the details of an existing equipment category by its ID.
     * The fields that can be updated include the category name and expiration period.
     * If a field is not provided in the request, it will remain unchanged.
     *
     * @param equipmentCategoryId the ID of the equipment category to update
     * @param updatedEquipment the request object containing updated category details (name and expiration period)
     * @return the updated {@link EquipmentCategory} object
     * @throws ResourceNotFoundException if the equipment category with the provided ID is not found
     */
    public EquipmentCategory updateEquipmentCategory(Long equipmentCategoryId, UpdateEquipmentCategoryRequest updatedEquipment) {
        EquipmentCategory equipmentCategory = equipmentCategoryRepository.findById(equipmentCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment Category with this id does not exist: " + equipmentCategoryId));

        if (updatedEquipment.getName() != null) {
            equipmentCategory.setName(updatedEquipment.getName());
        }
        if (updatedEquipment.getExpirationPeriodInMonths() != null) {
            equipmentCategory.setExpirationPeriodInMonths(updatedEquipment.getExpirationPeriodInMonths());
        }


        EquipmentCategory savedEquipmentCategory = equipmentCategoryRepository.save(equipmentCategory);
        return savedEquipmentCategory;
    }

}

