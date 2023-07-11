package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Doctor getDoctorByUserId(int userId);

    Doctor getDoctorById(int doctorId);

    @Query(value = "SELECT d.* FROM doctors d JOIN " +
            "(SELECT d.id AS doctor_id " +
            "FROM doctors d JOIN users u ON d.user_id = u.id " +
            "JOIN clinics c ON d.clinic_id = c.id " +
            "WHERE CONCAT_WS(' ', u.name, u.description, u.disease_treatment, c.name, c.address, " +
                                "d.examination_price) LIKE %?1%) a " +
            "ON d.id = a.doctor_id", nativeQuery = true)
    List<Doctor> findByKeyword(String keyword);

    @Query(value = "SELECT d.* FROM doctors d JOIN " +
            "(SELECT d.id AS doctor_id " +
            "FROM doctors d JOIN specializations s ON d.specialization_id = s.id " +
            "WHERE CONCAT('Bác sĩ ', s.name, ' ') LIKE %?1%) a " +
            "ON d.id = a.doctor_id", nativeQuery = true)
    List<Doctor> findBySpecialization(String keyword);
}
