package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.AddEquipmentCategoryRequest;
import net.equipment.dto.AddLocationRequest;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.Location;
import net.equipment.services.EquipmentCategoryService;
import net.equipment.services.LocationService;
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
@RequestMapping({"/api/location"})
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestBody AddLocationRequest location) {
        Location newLocation = locationService.addLocation(location);
        return new ResponseEntity(newLocation, HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Location> getLocationById(@PathVariable("id") Long locationId) {
        Location location = locationService.getLocationById(locationId);
        return ResponseEntity.ok(location);
    }

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<String> deleteEquipmentCategory(@PathVariable("id") Long locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.ok("Location deleted successfully");
    }
}
