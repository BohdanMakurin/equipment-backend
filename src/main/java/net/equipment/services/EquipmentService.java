//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.services;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.UpdateEquipmentRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.mapper.CompanyMapper;
import net.equipment.models.*;
import net.equipment.repositories.*;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final LocationRepository locationRepository;

    public Equipment addEquipment(AddEquipmentRequest req) {
        EquipmentCategory existingCategory = equipmentCategoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id " + req.getCategoryId() + " not found"));
        Company existingCompany = companyRepository.findById(req.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + req.getCompanyId() + " not found"));

        Equipment equipment = new Equipment();
        equipment.setName(req.getName());
        equipment.setDescription(req.getDescription());
        equipment.setCategory(existingCategory);
        equipment.setSerialNumber(req.getSerialNumber());
        equipment.setCompany(existingCompany);
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

    public Equipment addLocationToEquipment(Long equipmentId, String locationName) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));

//        Location location = locationRepository.findById(locationId)
//                .orElseThrow(() -> new ResourceNotFoundException("location with this id does not exist" + locationId));

        equipment.setLocation(locationName);
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

//    public Location getLocationAssignedToEquipment(Long equipmentId) {
//        Equipment equipment = equipmentRepository.findById(equipmentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
//        return equipment.getLocation();
//    }

    public List<Equipment> getEquipmentByAdminId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByAdminId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with admin id " + adminId);
        } else {
            return equipment;
        }
    }

    public Equipment updateEquipment(Long equipmentId, UpdateEquipmentRequest updatedEquipment) {
        // Найти существующее оборудование
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));

        // Обновление полей оборудования, если они не равны null
        if (updatedEquipment.getName() != null) {
            equipment.setName(updatedEquipment.getName());
        }
        if (updatedEquipment.getSerialNumber() != null) {
            equipment.setSerialNumber(updatedEquipment.getSerialNumber());
        }
        if (updatedEquipment.getDescription() != null) {
            equipment.setDescription(updatedEquipment.getDescription());
        }
        if (updatedEquipment.getLocation() != null) {
            equipment.setLocation(updatedEquipment.getLocation());
        }

        // Обновление связанных сущностей
        if (updatedEquipment.getUserId() != null) {
            User newUser = userRepository.findById(updatedEquipment.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist: " + updatedEquipment.getUserId()));
            equipment.setUser(newUser);
        }
        if (updatedEquipment.getCategoryId() != null) {
            EquipmentCategory newEquipmentCategory = equipmentCategoryRepository.findById(updatedEquipment.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("EquipmentCategory with this id does not exist: " + updatedEquipment.getCategoryId()));
            equipment.setCategory(newEquipmentCategory);
        }
        if (updatedEquipment.getCompanyId() != null) {
            Company newCompany = companyRepository.findById(updatedEquipment.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + updatedEquipment.getCompanyId()));
            equipment.setCompany(newCompany);
        }

        // Обновление времени
        equipment.setUpdatedAt(LocalDateTime.now());

        // Сохранение обновленного оборудования
        return equipmentRepository.save(equipment);
    }

}
