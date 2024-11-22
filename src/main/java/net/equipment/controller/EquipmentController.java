
package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentRequest;
import net.equipment.dto.EquipmentDto;
import net.equipment.dto.UpdateEquipmentRequest;
import net.equipment.models.Equipment;
import net.equipment.services.EquipmentService;
import net.equipment.services.ExpiredEquipmentReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing equipment-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting equipment,
 * as well as filtering equipment by admin, user, and company.
 */
@AllArgsConstructor
@RestController
@RequestMapping({"/api/equipment"})
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final ExpiredEquipmentReportService expiredEquipmentReportService;
    /**
     * Creates a new equipment entry.
     *
     * @param equipment the request object containing the equipment details
     * @return a ResponseEntity containing the newly created equipment and HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody AddEquipmentRequest equipment) {
        System.out.println("CONTROLLER" + String.valueOf(equipment));
        Equipment newEquipment = equipmentService.addEquipment(equipment);
        return new ResponseEntity<>(newEquipment, HttpStatus.CREATED);
    }

    /**
     * Retrieves an equipment entry by its ID.
     *
     * @param equipmentId the ID of the equipment to retrieve
     * @return a ResponseEntity containing the equipment and HTTP status 200 (OK)
     */
    @GetMapping({"{id}"})
    public ResponseEntity<Equipment> getEquipmentById(@PathVariable("id") Long equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(equipment);
    }

    /**
     * Retrieves a list of all equipment entries.
     *
     * @return a ResponseEntity containing a list of equipment and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<Equipment>> getAllEquipment() {
        List<Equipment> equipment = equipmentService.getAllEquipment();
        return ResponseEntity.ok(equipment);
    }

    /**
     * Deletes an equipment entry by its ID.
     *
     * @param equipmentId the ID of the equipment to delete
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipment(@PathVariable("id") Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.ok("Equipment deleted successfully");
    }

    /**
     * Retrieves a list of equipment entries associated with a specific admin by their ID.
     *
     * @param adminId the ID of the admin user
     * @return a ResponseEntity containing a list of equipment and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<EquipmentDto>> getEquipmentByAdminId(@PathVariable("id") Long adminId) throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByAdminId(adminId);
        return ResponseEntity.ok(equipment);
    }

    /**
     * Retrieves a list of equipment entries associated with a specific user by their ID.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity containing a list of equipment and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byUser/{id}"})
    public ResponseEntity<List<EquipmentDto>> getEquipmentByUserId(@PathVariable("id") Long userId) throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByUserId(userId);
        return ResponseEntity.ok(equipment);
    }

    /**
     * Retrieves a list of equipment entries associated with a specific company by its ID.
     *
     * @param companyId the ID of the company
     * @return a ResponseEntity containing a list of equipment and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byCompany/{id}"})
    public ResponseEntity<List<EquipmentDto>> getEquipmentByCompanyId(@PathVariable("id") Long companyId) throws Exception {
        List<EquipmentDto> equipment = equipmentService.getEquipmentByCompanyId(companyId);
        return ResponseEntity.ok(equipment);
    }

    /**
     * Updates an existing equipment entry by its ID.
     *
     * @param equipmentId      the ID of the equipment to update
     * @param updatedEquipment the request object containing the updated equipment details
     * @return a ResponseEntity containing the updated equipment and HTTP status 200 (OK)
     */
    @PutMapping({"{id}"})
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long equipmentId, @RequestBody UpdateEquipmentRequest updatedEquipment) {
        Equipment equipment = equipmentService.updateEquipment(equipmentId, updatedEquipment);
        return ResponseEntity.ok(equipment);
    }
    /**
     * Retrieves a report of expired equipment for a given company.
     *
     * @param companyId the ID of the company for which to generate the expired equipment report
     * @return a ResponseEntity containing a list of {@link Equipment} objects that have expired for the specified company
     */
    @GetMapping({"expiredEquipment/{companyId}"})
    public ResponseEntity<List<Equipment>> expiredEquipmentReport(@PathVariable("companyId") Long companyId) {
        List<Equipment> expiredEquipment = expiredEquipmentReportService.expiredEquipmentReport(companyId);
        return ResponseEntity.ok(expiredEquipment);
    }
}
