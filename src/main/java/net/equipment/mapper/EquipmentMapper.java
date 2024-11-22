package net.equipment.mapper;


import net.equipment.dto.EquipmentDto;
import net.equipment.models.Equipment;

public class EquipmentMapper {

    public EquipmentMapper() {
    }

    // Преобразование Equipment в EquipmentDto
    public static EquipmentDto mapToEquipmentDto(Equipment equipment) {
        Long userId = (equipment.getUser() != null) ? equipment.getUser().getId() : null;

        return new EquipmentDto(
                equipment.getEquipmentId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getSerialNumber(),
                equipment.getCreatedAt(),
                equipment.getUpdatedAt(),
                equipment.getCategory(),
                userId,  // Устанавливаем userId, если user не равен null
                equipment.getQrCode(),
                equipment.getLocation(),
                equipment.getCompany()
        );
    }

//    public static Equipment mapToEquipment(EquipmentDto equipmentDto) {
//        return new Equipment(
//            equipmentDto.getEquipmentId(),
//            equipmentDto.getName(),
//            equipmentDto.getDescription(),
//            equipmentDto.getSerialNumber(),
//            equipmentDto.getCreatedAt(),
//            equipmentDto.getUpdatedAt(),
//            equipmentDto.getCategory(),
//            equipmentDto.getUser(),
//            equipmentDto.getQrCode(),
//            equipmentDto.getLocation(),
//            equipmentDto.getCompany()
//        );
//    }
}