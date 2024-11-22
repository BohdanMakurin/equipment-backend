
package net.equipment.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.EquipmentDto;
import net.equipment.dto.UpdateEquipmentRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.mapper.CompanyMapper;
import net.equipment.mapper.EquipmentMapper;
import net.equipment.mapper.UserMapper;
import net.equipment.models.*;
import net.equipment.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing equipment, including adding, retrieving, updating,
 * and deleting equipment, as well as handling equipment data related to users,
 * categories, and companies.
 */
@RequiredArgsConstructor
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    /**
     * Adds a new equipment item to the system, associates it with a category, company, and generates a QR code.
     *
     * @param req the request object containing equipment details
     * @return the newly added Equipment object
     * @throws ResourceNotFoundException if the category or company with the given ID is not found
     */
    @SneakyThrows
    public Equipment addEquipment(AddEquipmentRequest req) {
        EquipmentCategory existingCategory = equipmentCategoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + req.getCategoryId() + " not found"));
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

        String qrCodeKey = UUID.randomUUID().toString();
        equipment.setQrCode(qrCodeKey);

        return equipmentRepository.save(equipment);
    }

    /**
     * Retrieves a list of all equipment items in the system.
     *
     * @return a list of all Equipment objects
     */
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    /**
     * Retrieves an equipment item by its ID.
     *
     * @param equipmentId the ID of the equipment to be retrieved
     * @return the Equipment object
     * @throws ResourceNotFoundException if the equipment with the given ID is not found
     */
    public Equipment getEquipmentById(Long equipmentId) {
        return equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));
    }

    /**
     * Deletes an equipment item by its ID.
     *
     * @param equipmentId the ID of the equipment to be deleted
     * @throws ResourceNotFoundException if the equipment with the given ID is not found
     */
    public void deleteEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));
        equipmentRepository.deleteById(equipmentId);
    }

    /**
     * Retrieves a list of equipment items associated with a given admin ID.
     *
     * @param adminId the ID of the admin whose equipment is to be retrieved
     * @return a list of EquipmentDto objects
     * @throws Exception if no equipment is found for the given admin ID
     */
    public List<EquipmentDto> getEquipmentByAdminId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByAdminId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("Equipment not found with admin id " + adminId);
        } else {
            return equipment.stream()
                    .map(EquipmentMapper::mapToEquipmentDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves a list of equipment items associated with a given user ID.
     *
     * @param adminId the ID of the user whose equipment is to be retrieved
     * @return a list of EquipmentDto objects
     * @throws Exception if no equipment is found for the given user ID
     */
    public List<EquipmentDto> getEquipmentByUserId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByUserId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("Equipment not found with user id " + adminId);
        } else {
            return equipment.stream()
                    .map(EquipmentMapper::mapToEquipmentDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves a list of equipment items associated with a given company ID.
     *
     * @param companyId the ID of the company whose equipment is to be retrieved
     * @return a list of EquipmentDto objects
     * @throws Exception if no equipment is found for the given company ID
     */
    public List<EquipmentDto> getEquipmentByCompanyId(Long companyId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByCompanyId(companyId);
        if (equipment.isEmpty()) {
            throw new Exception("Equipment not found with company id " + companyId);
        } else {
            return equipment.stream()
                    .map(EquipmentMapper::mapToEquipmentDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Updates the details of an existing equipment item, including its name, description,
     * serial number, location, user, category, and company.
     *
     * @param equipmentId the ID of the equipment to be updated
     * @param updatedEquipment the request object containing updated equipment details
     * @return the updated Equipment object
     * @throws ResourceNotFoundException if the equipment, user, category, or company is not found
     */
    public Equipment updateEquipment(Long equipmentId, UpdateEquipmentRequest updatedEquipment) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));

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

        if (updatedEquipment.getUserId() != null) {
            User newUser = userRepository.findById(updatedEquipment.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist: " + updatedEquipment.getUserId()));
            equipment.setUser(newUser);
        }
        if (updatedEquipment.getCategoryId() != null) {
            EquipmentCategory newCategory = equipmentCategoryRepository.findById(updatedEquipment.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("EquipmentCategory with this id does not exist: " + updatedEquipment.getCategoryId()));
            equipment.setCategory(newCategory);
        }
        if (updatedEquipment.getCompanyId() != null) {
            Company newCompany = companyRepository.findById(updatedEquipment.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + updatedEquipment.getCompanyId()));
            equipment.setCompany(newCompany);
        }

        equipment.setUpdatedAt(LocalDateTime.now());

        return equipmentRepository.save(equipment);
    }
}
