//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.services;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.Equipment;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.Location;
import net.equipment.models.User;
import net.equipment.repositories.EquipmentCategoryRepository;
import net.equipment.repositories.EquipmentRepository;
import net.equipment.repositories.LocationRepository;
import net.equipment.repositories.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final LocationRepository locationRepository;

    public Equipment addEquipment(AddEquipmentRequest req) {
        EquipmentCategory existingCategory = equipmentCategoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id " + req.getCategoryId() + " not found"));
        Equipment equipment = new Equipment();
        equipment.setName(req.getName());
        equipment.setDescription(req.getDescription());
        equipment.setCategory(existingCategory);
        equipment.setSerialNumber(req.getSerialNumber());
        equipment.setUpdatedAt(LocalDateTime.now());
        equipment.setCreatedAt(LocalDateTime.now());
        return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
        return equipment;
    }

    public void deleteEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
        equipmentRepository.deleteById(equipmentId);
    }

    public Equipment addUserToEquipment(Long equipmentId, Long userId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + userId));

        equipment.setUser(user);
        equipment.setUpdatedAt(LocalDateTime.now());

        return equipmentRepository.save(equipment);
    }

    public void removeUserFromEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));

        equipment.setUser(null);
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentRepository.save(equipment);
    }

    public User getUserAssignedToEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
        return equipment.getUser();
    }

    public Equipment addLocationToEquipment(Long equipmentId, Long locationId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("location with this id does not exist" + locationId));

        equipment.setLocation(location);
        equipment.setUpdatedAt(LocalDateTime.now());

        return equipmentRepository.save(equipment);
    }

    public void removeLocationFromEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));

        equipment.setLocation(null);
        equipment.setUpdatedAt(LocalDateTime.now());
        equipmentRepository.save(equipment);
    }

    public Location getLocationAssignedToEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
        return equipment.getLocation();
    }

}
