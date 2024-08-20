package net.equipment.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.AddLocationRequest;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.models.Location;
import net.equipment.repositories.LocationRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public Location addLocation(AddLocationRequest req) {
        Location location = new Location();
        location.setName(req.getName());
        return locationRepository.save(location);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long locationId) {
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location with this id does not exist" + locationId));
    }

    public void deleteLocation(Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("Location with this id does not exist" + locationId));
        locationRepository.deleteById(locationId);
    }

}
