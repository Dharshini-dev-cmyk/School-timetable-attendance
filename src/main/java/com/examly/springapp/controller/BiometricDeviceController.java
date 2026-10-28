package com.examly.springapp.controller;

import com.examly.springapp.entity.BiometricDevice;
import com.examly.springapp.entity.BiometricDevice.DeviceStatus;
import com.examly.springapp.exception.ResourceNotFoundException;
import com.examly.springapp.repository.BiometricDeviceRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Biometric Devices", description = "Biometric device registration, health monitoring, and management")
@RestController
@RequestMapping("/api/biometric-devices")
public class BiometricDeviceController {

    private final BiometricDeviceRepository deviceRepository;

    public BiometricDeviceController(BiometricDeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    // POST /api/biometric-devices — Register a new device (Admin only)
    @Operation(
        summary = "Register a new biometric device",
        description = "Creates and stores a new biometric device record. Requires ADMIN role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Device registered successfully",
            content = @Content(schema = @Schema(implementation = BiometricDevice.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required")
    })
    @PostMapping
    public ResponseEntity<BiometricDevice> addDevice(
            @Parameter(description = "Biometric device to create", schema = @Schema(implementation = BiometricDevice.class))
            @RequestBody BiometricDevice device) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceRepository.save(device));
    }

    // GET /api/biometric-devices — List all devices
    @Operation(
        summary = "List all biometric devices",
        description = "Returns a list of all registered biometric devices. Requires ADMIN or PRINCIPAL role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
            content = @Content(schema = @Schema(implementation = BiometricDevice.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required")
    })
    @GetMapping
    public ResponseEntity<List<BiometricDevice>> getAllDevices() {
        return ResponseEntity.ok(deviceRepository.findAll());
    }

    // GET /api/biometric-devices/{id} — Get device by ID
    @Operation(
        summary = "Get a biometric device by ID",
        description = "Returns the details of a specific biometric device. Requires ADMIN or PRINCIPAL role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Device found",
            content = @Content(schema = @Schema(implementation = BiometricDevice.class))),
        @ApiResponse(responseCode = "404", description = "Device not found with the given ID"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BiometricDevice> getDeviceById(
            @Parameter(description = "ID of the biometric device to retrieve", example = "1", in = ParameterIn.PATH)
            @PathVariable Long id) {
        return ResponseEntity.ok(deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + id)));
    }

    // GET /api/biometric-devices/status/{status} — Get devices by status (health monitoring — FR5)
    @Operation(
        summary = "Get devices by status",
        description = "Returns all biometric devices matching the given status (ONLINE, OFFLINE, MAINTENANCE). Requires ADMIN or PRINCIPAL role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Devices retrieved successfully",
            content = @Content(schema = @Schema(implementation = BiometricDevice.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status value"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN or PRINCIPAL role required")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BiometricDevice>> getDevicesByStatus(
            @Parameter(description = "Device status to filter by", example = "ONLINE", in = ParameterIn.PATH)
            @PathVariable String status) {
        return ResponseEntity.ok(deviceRepository.findByStatus(DeviceStatus.valueOf(status.toUpperCase())));
    }

    // PUT /api/biometric-devices/{id} — Update device info or status
    @Operation(
        summary = "Update a biometric device",
        description = "Updates the details or status of an existing biometric device. Requires ADMIN role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Device updated successfully",
            content = @Content(schema = @Schema(implementation = BiometricDevice.class))),
        @ApiResponse(responseCode = "404", description = "Device not found with the given ID"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BiometricDevice> updateDevice(
            @Parameter(description = "ID of the biometric device to update", example = "1", in = ParameterIn.PATH)
            @PathVariable Long id,
            @Parameter(description = "Updated biometric device fields", schema = @Schema(implementation = BiometricDevice.class))
            @RequestBody BiometricDevice updated) {
        BiometricDevice device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + id));
        device.setDeviceName(updated.getDeviceName());
        device.setLocation(updated.getLocation());
        device.setDeviceType(updated.getDeviceType());
        device.setLastSync(updated.getLastSync());
        device.setBatteryLevel(updated.getBatteryLevel());
        device.setStatus(updated.getStatus());
        return ResponseEntity.ok(deviceRepository.save(device));
    }

    // DELETE /api/biometric-devices/{id} — Remove device (Admin only)
    @Operation(
        summary = "Delete a biometric device",
        description = "Removes a biometric device from the system. Requires ADMIN role."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Device not found with the given ID"),
        @ApiResponse(responseCode = "403", description = "Forbidden — ADMIN role required")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID of the biometric device to delete", example = "1", in = ParameterIn.PATH)
            @PathVariable Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Device not found with ID: " + id);
        }
        deviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
