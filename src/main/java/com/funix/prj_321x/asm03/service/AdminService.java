package com.funix.prj_321x.asm03.service;

import com.funix.prj_321x.asm03.entity.Doctor;
import com.funix.prj_321x.asm03.entity.HealthResult;
import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import com.funix.prj_321x.asm03.entity.User;
import com.funix.prj_321x.asm03.exception.*;

import java.util.List;

public interface AdminService {

    User addDoctorAccount(String name, String gender, String email, String phone, String address, String password,
                          String description, String studyProcess, String achievements, String diseaseTreatment) throws EmailExistException;

    Doctor transferDoctor(int userId, int clinicId, int specializationId, String examinationPrice) throws EmailExistException, ClinicNotFoundException, SpecializationOfClinicNotFoundException;

    Doctor updateDoctor(int doctorId, int clinicId, int specializationId, String examinationPrice) throws UserNotFoundException, ClinicNotFoundException, SpecializationOfClinicNotFoundException;

    String lockAccount(String email, String reason) throws EmailNotFoundException;

    String unlockAccount(String email) throws EmailNotFoundException;

    List<MedicalSchedule> getScheduleListOfUser(int userId);

    List<MedicalSchedule> getScheduleListOfDoctor(int doctorId);

    MedicalSchedule getScheduleDetail(int scheduleId);
}
