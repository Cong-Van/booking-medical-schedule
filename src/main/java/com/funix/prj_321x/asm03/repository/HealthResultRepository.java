package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.HealthResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HealthResultRepository extends JpaRepository<HealthResult, Integer> {

    @Query(value = "SELECT h.* FROM health_results h JOIN medical_schedules m ON h.medical_schedule_id = m.id " +
            "WHERE user_id = ?1", nativeQuery = true)
    List<HealthResult> findAllByUserId(int id);

    @Query(value = "SELECT h.* FROM health_results h JOIN medical_schedules m ON h.medical_schedule_id = m.id " +
            "WHERE doctor_id = ?1", nativeQuery = true)
    List<HealthResult> findAllByDoctorId(int id);

    HealthResult getByMedicalScheduleId(int id);

    HealthResult getHealthResultById(int id);

}
