package com.teste.PowerCrm.repository;

import com.teste.PowerCrm.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {

    boolean existsByPlate(String plate);
}
