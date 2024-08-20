//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.models.Equipment;
import net.equipment.models.Location;
import net.equipment.models.User;
import net.equipment.services.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        List<Equipment> companies = equipmentService.getAllEquipment();
        return ResponseEntity.ok(companies);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipment(@PathVariable("id") Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok("equipment deleted successfully");
    }

    @PostMapping({"user/{equipmentId}/{userId}"})
    public ResponseEntity<Equipment> addUserToEquipment(@PathVariable("equipmentId") Long equipmentId, @PathVariable("userId") Long userId) {
        Equipment equipment = equipmentService.addUserToEquipment(equipmentId, userId);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping({"user/{equipmentId}"})
    public ResponseEntity<String> removeUserFromEquipment(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.removeUserFromEquipment(equipmentId);
        return ResponseEntity.ok("User removed From Equipment successfully");
    }

    @GetMapping({"user/{userId}"})
    public ResponseEntity<User> getUserAssignedToEquipment(@PathVariable("userId") Long userId) {
        User assignedUser = equipmentService.getUserAssignedToEquipment(userId);
        return ResponseEntity.ok(assignedUser);
    }

    @PostMapping({"location/{equipmentId}/{locationId}"})
    public ResponseEntity<Equipment> addLocationToEquipment(
            @PathVariable("equipmentId") Long equipmentId,
            @PathVariable("locationId") Long locationId) {
        Equipment equipment = equipmentService.addLocationToEquipment(equipmentId, locationId);
        return ResponseEntity.ok(equipment);
    }

    @DeleteMapping({"location/{equipmentId}"})
    public ResponseEntity<String> removeLocationFromEquipment(
            @PathVariable("equipmentId") Long equipmentId) {
       equipmentService.removeLocationFromEquipment(equipmentId);
        return ResponseEntity.ok("Location removed From Equipment successfully");
    }

    @GetMapping({"location/{locationId}"})
    public ResponseEntity<Location> getLocationAssignedToEquipment(
            @PathVariable("locationId") Long locationId) {
        Location assignedLocation = equipmentService.getLocationAssignedToEquipment(locationId);
        return ResponseEntity.ok(assignedLocation);
    }
}
