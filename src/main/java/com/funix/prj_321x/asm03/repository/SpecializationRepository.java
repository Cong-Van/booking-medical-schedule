package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {

    Specialization getSpecializationById(int id);

    @Query(value = "SELECT s.* FROM specializations s JOIN " +
            "(SELECT count(specialization_id) AS count, specialization_id " +
            "FROM medical_schedules ms JOIN doctors d ON ms.doctor_id = d.id GROUP BY specialization_id) a " +
            "ON s.id = a.specialization_id ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Specialization> findTopSpecializations();
}
