package com.funix.prj_321x.asm03.service;

import com.funix.prj_321x.asm03.entity.Doctor;
import com.funix.prj_321x.asm03.entity.HealthResult;
import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import com.funix.prj_321x.asm03.entity.User;
import com.funix.prj_321x.asm03.exception.NotInChargeException;
import com.funix.prj_321x.asm03.exception.ResultExistException;
import com.funix.prj_321x.asm03.exception.ScheduleHasCancelException;
import com.funix.prj_321x.asm03.exception.ScheduleNotFoundException;
import com.funix.prj_321x.asm03.repository.DoctorRepository;
import com.funix.prj_321x.asm03.repository.HealthResultRepository;
import com.funix.prj_321x.asm03.repository.MedicalScheduleRepository;
import com.funix.prj_321x.asm03.repository.UserRepository;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.funix.prj_321x.asm03.constant.UserImplConstant.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    private UserRepository userRepository;
    private DoctorRepository doctorRepository;
    private MedicalScheduleRepository medicalScheduleRepository;
    private HealthResultRepository healthResultRepository;

    public DoctorServiceImpl(UserRepository userRepository,
                             DoctorRepository doctorRepository, 
                             MedicalScheduleRepository medicalScheduleRepository, 
                             HealthResultRepository healthResultRepository) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository; 
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.healthResultRepository = healthResultRepository;
    }

    @Override
    public List<MedicalSchedule> getScheduleList(String email) {
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        List<MedicalSchedule> scheduleList = medicalScheduleRepository.findAllByDoctorId(doctor.getId());
        return scheduleList;
    }

    @Override
    public MedicalSchedule confirmSchedule(String email, int scheduleId) throws ScheduleNotFoundException, ScheduleHasCancelException, NotInChargeException {
        /*
        Lấy thông tin và tài khoản của bác sĩ
        Nếu bác sĩ phụ trách thì được phép confirm
         */
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        Optional<MedicalSchedule> schedule = medicalScheduleRepository.findById(scheduleId);
        if (!schedule.isPresent()) {
            throw new ScheduleNotFoundException(NOT_FOUND_SCHEDULE);
        }
        MedicalSchedule medicalSchedule = schedule.get();
        if (doctor.getId() != medicalSchedule.getDoctor().getId()) {
            throw new NotInChargeException(NO_PERMISSION_TO_OPERATE);
        }
        if (medicalSchedule.getReason() != null) {
            throw new ScheduleHasCancelException(SCHEDULE_HAS_CANCEL);
        }
        medicalSchedule.setDoctorConfirm(true);
        medicalScheduleRepository.save(medicalSchedule);
        return medicalSchedule;
    }

    @Override
    public MedicalSchedule cancelSchedule(int scheduleId, String email, String reason) throws ScheduleNotFoundException, NotInChargeException {
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        Optional<MedicalSchedule> schedule = medicalScheduleRepository.findById(scheduleId);
        if (!schedule.isPresent()) {
            throw new ScheduleNotFoundException(NOT_FOUND_SCHEDULE);
        }
        MedicalSchedule medicalSchedule = schedule.get();
        if (doctor.getId() != medicalSchedule.getDoctor().getId()) {
            throw new NotInChargeException(NO_PERMISSION_TO_OPERATE);
        }
        medicalSchedule.setDoctorConfirm(false);
        medicalSchedule.setReason(reason);
        medicalScheduleRepository.save(medicalSchedule);
        return medicalSchedule;
    }

    @Override
    public HealthResult createResult(String email, int scheduleId, String pathology, String description) throws ScheduleNotFoundException, NotInChargeException, ScheduleHasCancelException, ResultExistException {
        /*
        Lấy thông tin và tài khoản của bác sĩ
        Lấy thông tin buổi khám (lịch khám)
        Nếu bác sĩ phụ trách thì tạo kết quả sau khám
        */
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        Optional<MedicalSchedule> schedule = medicalScheduleRepository.findById(scheduleId);
        if (!schedule.isPresent()) {
            throw new ScheduleNotFoundException(NOT_FOUND_SCHEDULE);
        }
        MedicalSchedule medicalSchedule = schedule.get();
        if (doctor.getId() != medicalSchedule.getDoctor().getId()) {
            throw new NotInChargeException(NO_PERMISSION_TO_OPERATE);
        }
        if (!medicalSchedule.isDoctorConfirm()) {
            if (medicalSchedule.getReason() != null) {
                throw new ScheduleHasCancelException(SCHEDULE_HAS_CANCEL);
            }
            throw new NoResultException(NOT_YET_CONFIRM);
        }

        HealthResult result = healthResultRepository.getByMedicalScheduleId(medicalSchedule.getId());
        if (result != null) {
            throw new ResultExistException(ALREADY_CONFIRM);
        }
        HealthResult healthResult = new HealthResult(medicalSchedule, pathology, description);
        healthResultRepository.save(healthResult);
        return healthResult;
    }

    @Override
    public HealthResult updateResult(String email, int resultId, String pathology, String description) throws NotInChargeException {
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        HealthResult healthResult = healthResultRepository.getHealthResultById(resultId);
        if (healthResult == null) {
            throw new NoResultException(NOT_FOUND_RESULT);
        }
        if (doctor.getId() != healthResult.getMedicalSchedule().getDoctor().getId()) {
            throw new NotInChargeException(NO_PERMISSION_TO_OPERATE);
        }
        healthResult.setPathology(pathology);
        healthResult.setDescription(description);
        healthResultRepository.save(healthResult);
        return healthResult;
    }

    @Override
    public List<HealthResult> getResultList(String email) {
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
        List<HealthResult> resultList = healthResultRepository.findAllByDoctorId(doctor.getId());
        return resultList;
    }

}
