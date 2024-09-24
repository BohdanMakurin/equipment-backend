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
    private final LocationRepository locationRepository;
    private final QRCodeRepository qrCodeRepository;

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

        Equipment savedEquipment = equipmentRepository.save(equipment);

        // Генерация QR-кода и сохранение его в файл
        String qrCodeText = "Product ID: " + savedEquipment.getEquipmentId();
        String qrCodeFilePath = "qr-codes/" + savedEquipment.getEquipmentId() + ".png";
        net.equipment.services.QRCodeGenerator.generateQRCode(qrCodeText, qrCodeFilePath);

        // Привязка пути к QR-коду к продукту (при необходимости)
        savedEquipment.setQrCode(qrCodeFilePath);
        equipmentRepository.save(savedEquipment);


        return equipmentRepository.save(savedEquipment);

    }

    public List<Equipment> getAllEquipment() {

//        List<Equipment> equipment = equipmentRepository.findAll();
//        return equipment.stream().map((equip) -> EquipmentMapper.mapToEquipmentDto(equip))
//                .collect(Collectors.toList());
        return equipmentRepository.findAll();
    }

    public Equipment getEquipmentById(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist" + equipmentId));
        return equipment;
    }

    public void deleteEquipment(Long equipmentId) {
        // Найти оборудование по ID
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with this id does not exist: " + equipmentId));

        // Удалить запись оборудования из базы данных
        equipmentRepository.deleteById(equipmentId);

        // Путь к файлу QR-кода
        String fileName = equipmentId + ".png"; // Имя файла QR-кода основывается на ID оборудования
        Path qrCodePath = Paths.get("src/main/resources/static/qr-codes/" + fileName);

        try {
            // Проверка существования файла и его удаление
            if (Files.exists(qrCodePath)) {
                Files.delete(qrCodePath);
                System.out.println("QR code deleted: " + fileName);
            } else {
                System.out.println("QR code not found: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Error deleting QR code: " + e.getMessage());
            // Можно добавить обработку исключений, если необходимо
        }
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

    public List<Equipment> getEquipmentByUserId(Long adminId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByUserId(adminId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with user id " + adminId);
        } else {
            return equipment;
        }
    }

    public List<Equipment> getEquipmentByCompanyId(Long companyId) throws Exception {
        List<Equipment> equipment = equipmentRepository.findByCompanyId(companyId);
        if (equipment.isEmpty()) {
            throw new Exception("equipment not found with company id " + companyId);
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
