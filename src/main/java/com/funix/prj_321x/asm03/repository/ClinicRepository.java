package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {

    Clinic getClinicById(int id);

    @Query(value = "SELECT c.* FROM clinics c JOIN " +
            "(SELECT count(clinic_id) AS count, clinic_id " +
            "FROM medical_schedules ms JOIN doctors d ON ms.doctor_id = d.id GROUP BY clinic_id) a " +
            "ON c.id = a.clinic_id ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Clinic> findTopClinics();

    @Query(value = "SELECT c.* FROM clinics c JOIN " +
            "(SELECT c.id AS cli_id " +
            "FROM specializations s JOIN clinic_specialties sc ON s.id = sc.specialization_id " +
            "JOIN clinics c ON c.id = sc.clinic_id " +
            "WHERE CONCAT_WS(' ', c.name, c.address, s.name, s.description) LIKE %?1% GROUP BY cli_id) a " +
            "ON c.id = a.cli_id", nativeQuery = true)
    List<Clinic> findByKeyword(String keyword);

}
