package com.examly.springapp.repository;

import com.examly.springapp.entity.BiometricDevice;
import com.examly.springapp.entity.BiometricDevice.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BiometricDeviceRepository extends JpaRepository<BiometricDevice, Long> {

    List<BiometricDevice> findByStatus(DeviceStatus status);

    List<BiometricDevice> findByLocation(String location);
}
