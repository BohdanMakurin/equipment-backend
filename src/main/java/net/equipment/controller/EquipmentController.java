//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.dto.CompanyDto;
import net.equipment.dto.EquipmentDto;
import net.equipment.dto.UpdateEquipmentRequest;
import net.equipment.models.Company;
import net.equipment.models.Equipment;
import net.equipment.models.Location;
import net.equipment.models.User;
import net.equipment.services.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping({"/api/equipment"})
public class EquipmentController {
    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody AddEquipmentRequest equipment) {
        System.out.println("CONTROLLER" + String.valueOf(equipment));
        Equipment newEquipment = equipmentService.addEquipment(equipment);
        return new ResponseEntity(newEquipment, HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") Long equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipment = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipment(@PathVariable("id") Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok("equipment deleted successfully");
    }
    
    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<EquipmentDto>> getEquipmentByAdminId(@PathVariable("id") Long adminId) throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByAdminId(adminId);
        return ResponseEntity.ok(equipment);
    }
    @GetMapping({"byUser/{id}"})
    public ResponseEntity<List<Equipment>> getEquipmentByUserId(@PathVariable("id") Long userId) throws Exception {
        List<Equipment> equipment = equipmentService.getEquipmentByUserId(userId);
        return ResponseEntity.ok(equipment);
    }
    @GetMapping({"byCompany/{id}"})
    public ResponseEntity<List<Equipment>> getEquipmentByCompanyId(@PathVariable("id") Long companyId) throws Exception {
        List<Equipment> equipment = equipmentService.getEquipmentByCompanyId(companyId);
        return ResponseEntity.ok(equipment);
    }
    @PutMapping({"{id}"})
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long equipmentId, @RequestBody UpdateEquipmentRequest updatedEquipment) {
        Equipment equipment = equipmentService.updateEquipment(equipmentId, updatedEquipment);
        return ResponseEntity.ok(equipment);
    }
}
