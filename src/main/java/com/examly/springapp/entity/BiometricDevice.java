package com.examly.springapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "biometric_devices")
@Schema(description = "Represents a biometric device used for attendance tracking in the school system")
public class BiometricDevice {

    @Schema(description = "Type of biometric authentication supported by the device", example = "FINGERPRINT", allowableValues = "FINGERPRINT, RFID, FACE")
    public enum DeviceType { FINGERPRINT, RFID, FACE }

    @Schema(description = "Operational status of the device", example = "ONLINE", allowableValues = "ONLINE, OFFLINE, MAINTENANCE")
    public enum DeviceStatus { ONLINE, OFFLINE, MAINTENANCE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the biometric device", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "device_name", length = 100)
    @Schema(description = "Name of the biometric device", example = "Front Gate Scanner", maxLength = 100)
    private String deviceName;

    @Column(name = "location", length = 100)
    @Schema(description = "Physical location where the device is installed", example = "Main Entrance", maxLength = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type")
    @Schema(description = "Type of biometric authentication supported by the device", example = "FINGERPRINT", allowableValues = "FINGERPRINT, RFID, FACE")
    private DeviceType deviceType;

    @Column(name = "last_sync")
    @Schema(description = "Timestamp of the last successful synchronization with the server", example = "2024-01-15T10:30:00")
    private LocalDateTime lastSync;

    @Column(name = "battery_level")
    @Schema(description = "Current battery level of the device as a percentage", example = "85", minimum = "0", maximum = "100")
    private Integer batteryLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Schema(description = "Operational status of the device", example = "ONLINE", allowableValues = "ONLINE, OFFLINE, MAINTENANCE")
    private DeviceStatus status;

    public BiometricDevice() {}

    public BiometricDevice(String deviceName, String location, DeviceType deviceType,
                           LocalDateTime lastSync, Integer batteryLevel, DeviceStatus status) {
        this.deviceName = deviceName;
        this.location = location;
        this.deviceType = deviceType;
        this.lastSync = lastSync;
        this.batteryLevel = batteryLevel;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public DeviceType getDeviceType() { return deviceType; }
    public void setDeviceType(DeviceType deviceType) { this.deviceType = deviceType; }

    public LocalDateTime getLastSync() { return lastSync; }
    public void setLastSync(LocalDateTime lastSync) { this.lastSync = lastSync; }

    public Integer getBatteryLevel() { return batteryLevel; }
    public void setBatteryLevel(Integer batteryLevel) { this.batteryLevel = batteryLevel; }

    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
}
