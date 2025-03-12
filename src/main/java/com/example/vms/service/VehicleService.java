package com.example.vms.service;

import com.example.vms.model.Vehicle;
import com.example.vms.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public ResponseEntity<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Vehicle> createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            return ResponseEntity.badRequest().build();
        }
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return ResponseEntity.ok(savedVehicle);
    }

    public ResponseEntity<Vehicle> updateVehicle(Long id, Vehicle vehicleDetails) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(id);
        
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Vehicle existingVehicle = vehicleOptional.get();
        
        // Check if new registration number already exists
        if (!existingVehicle.getRegistrationNumber().equals(vehicleDetails.getRegistrationNumber()) &&
                vehicleRepository.existsByRegistrationNumber(vehicleDetails.getRegistrationNumber())) {
            return ResponseEntity.badRequest().build();
        }
        
        // Update vehicle details
        existingVehicle.setMake(vehicleDetails.getMake());
        existingVehicle.setModel(vehicleDetails.getModel());
        existingVehicle.setYear(vehicleDetails.getYear());
        existingVehicle.setColor(vehicleDetails.getColor());
        existingVehicle.setStatus(vehicleDetails.getStatus());
        existingVehicle.setRegistrationNumber(vehicleDetails.getRegistrationNumber());
        
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return ResponseEntity.ok(updatedVehicle);
    }

    public ResponseEntity<Void> deleteVehicle(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicle -> {
                    vehicleRepository.delete(vehicle);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 