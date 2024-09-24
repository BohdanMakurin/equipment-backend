package net.equipment.mapper;

import net.equipment.dto.EquipmentDto;
import net.equipment.models.Company;
import net.equipment.models.Equipment;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.User;

public class EquipmentMapper {

    public EquipmentMapper() {
    }

    // Преобразование Equipment в EquipmentDto
    public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
        return new EquipmentDto(
                equipment.getEquipmentId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getSerialNumber(),
                equipment.getCreatedAt(),
                equipment.getUpdatedAt(),
                equipment.getCategory(),
                equipment.getUser(),
                equipment.getLocation(),
                equipment.getCompany()
        );
    }

    // Преобразование EquipmentDto в Equipment
    public static Equipment mapToEquipment(EquipmentDto equipmentDto) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(equipmentDto.getEquipmentId());
        equipment.setName(equipmentDto.getName());
        equipment.setDescription(equipmentDto.getDescription());
        equipment.setSerialNumber(equipmentDto.getSerialNumber());
        equipment.setCreatedAt(equipmentDto.getCreatedAt());
        equipment.setUpdatedAt(equipmentDto.getUpdatedAt());
        equipment.setCategory(equipmentDto.getCategory());
        equipment.setUser(equipmentDto.getUser());
        equipment.setLocation(equipmentDto.getLocation());
        equipment.setCompany(equipmentDto.getCompany());
        return equipment;
    }
}