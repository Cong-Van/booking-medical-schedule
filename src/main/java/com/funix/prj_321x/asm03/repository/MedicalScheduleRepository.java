package com.funix.prj_321x.asm03.repository;

import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalScheduleRepository extends JpaRepository<MedicalSchedule, Integer> {

    List<MedicalSchedule> findAllByDoctorId(int id);

    List<MedicalSchedule> getByUserId(int id);

    List<MedicalSchedule> getByDoctorId(int id);
}
