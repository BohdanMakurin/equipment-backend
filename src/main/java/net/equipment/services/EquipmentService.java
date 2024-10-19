//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

@RequiredArgsConstructor
@Service
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

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

        Equipment savedEquipment = equipmentRepository.save(equipment);

        return equipmentRepository.save(savedEquipment);

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
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));

        equipmentRepository.deleteById(equipmentId);

    }

    public List<EquipmentDto> getEquipmentByAdminId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByAdminId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with admin id " + adminId);
        } else {
            return equipment.stream().map((equip) -> EquipmentMapper.mapToEquipmentDto(equip))
                    .collect(Collectors.toList());
        }
    }

    public List<EquipmentDto> getEquipmentByUserId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByUserId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with user id " + adminId);
        } else {
            return equipment.stream().map((equip) -> EquipmentMapper.mapToEquipmentDto(equip))
                    .collect(Collectors.toList());
        }
    }

    public List<EquipmentDto> getEquipmentByCompanyId(Long companyId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByCompanyId(companyId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with company id " + companyId);
        } else {
            return equipment.stream().map((equip) -> EquipmentMapper.mapToEquipmentDto(equip))
                    .collect(Collectors.toList());
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
