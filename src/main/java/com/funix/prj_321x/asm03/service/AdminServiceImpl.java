package com.funix.prj_321x.asm03.service;

import com.funix.prj_321x.asm03.common.Gender;
import com.funix.prj_321x.asm03.entity.*;
import com.funix.prj_321x.asm03.exception.*;
import com.funix.prj_321x.asm03.repository.*;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.funix.prj_321x.asm03.constant.UserImplConstant.*;

@Service
public class AdminServiceImpl implements AdminService{

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;
    private ClinicRepository clinicRepository;
    private SpecializationRepository specializationRepository;
    private MedicalScheduleRepository medicalScheduleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository,
                            DoctorRepository doctorRepository,
                            ClinicRepository clinicRepository,
                            SpecializationRepository specializationRepository,
                            MedicalScheduleRepository medicalScheduleRepository,
                            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.specializationRepository = specializationRepository;
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addDoctorAccount(String name, String gender, String email, String phone, String address, String password,
                                 String description, String studyProcess, String achievements, String diseaseTreatment) throws EmailExistException {
        if (getUserByEmail(email) != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        }
        User newUser =  new User(name, getGenderEnumName(gender).name(), email, phone, address, encodePassword(password),
                description, studyProcess, achievements, diseaseTreatment);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public Doctor transferDoctor(int userId, int clinicId, int specializationId, String examinationPrice) throws EmailExistException, ClinicNotFoundException, SpecializationOfClinicNotFoundException {
        /*
        Lấy ra thông tin của tài khoản
        Nếu là bác sĩ thì điều chuyển công tác vào 1 chuyên khoa trong 1 cơ sở y tế
         */
        User user = userRepository.getUserById(userId);
        Doctor doctor = doctorRepository.getDoctorByUserId(userId);
        Clinic clinic = clinicRepository.getClinicById(clinicId);
        Specialization specialization = specializationRepository.getSpecializationById(specializationId);
        if (user == null || user.getRole().getId() != 2) {
            throw new EmailExistException(HAVE_NOT_ADD_DOCTOR_ACCOUNT_YET);
        }
        if (doctor != null) {
            throw new EmailExistException(DOCTOR_ALREADY_WORKING_AT_A_CLINIC);
        }
        if (clinic == null) {
            throw new ClinicNotFoundException(NOT_FOUND_CLINIC);
        }
        if (!clinic.getSpecializations().contains(specialization)) {
            throw new SpecializationOfClinicNotFoundException(CLINIC_NOT_HAVE_THIS_SPECIALIZATION);
        }
        Doctor newDoctor = new Doctor(user, clinic, specialization, examinationPrice);
        doctorRepository.save(newDoctor);
        return newDoctor;
    }

    @Override
    public Doctor updateDoctor(int doctorId, int clinicId, int specializationId, String examinationPrice) throws UserNotFoundException, ClinicNotFoundException, SpecializationOfClinicNotFoundException {
        Doctor doctor = doctorRepository.getDoctorById(doctorId);
        Clinic clinic = clinicRepository.getClinicById(clinicId);
        Specialization specialization = specializationRepository.getSpecializationById(specializationId);
        if (doctor == null) {
            throw new UserNotFoundException(DOCTOR_HAS_NOT_BEEN_TRANSFERRED);
        }
        if (clinic == null) {
            throw new ClinicNotFoundException(NOT_FOUND_CLINIC);
        }
        if (!clinic.getSpecializations().contains(specialization)) {
            throw new SpecializationOfClinicNotFoundException(CLINIC_NOT_HAVE_THIS_SPECIALIZATION);
        }
        doctor.setClinic(clinic);
        doctor.setSpecialization(specialization);
        doctor.setExaminationPrice(examinationPrice);
        doctor.setTransferAt(LocalDateTime.now());
        doctorRepository.save(doctor);
        return doctor;
    }

    @Override
    public String lockAccount(String email, String reason) throws EmailNotFoundException {
        if (getUserByEmail(email) == null) {
            throw new EmailNotFoundException(EMAIL_IS_NOT_REGISTERED);
        }
        User user = userRepository.getUserByEmail(email);
        user.setNotBlocked(false);
        user.setLockReason(reason);
        if (user.getRole().getId() == 2) {
            Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
            if (doctor != null) {
                doctor.setStatus(false);
                doctorRepository.save(doctor);
            }
        }
        userRepository.save(user);
        return String.format(HAVE_LOCKED_ACCOUNT, email, reason);
    }

    @Override
    public String unlockAccount(String email) throws EmailNotFoundException {
        if (getUserByEmail(email) == null) {
            throw new EmailNotFoundException(EMAIL_IS_NOT_REGISTERED);
        }
        User user = userRepository.getUserByEmail(email);
        user.setNotBlocked(true);
        user.setLockReason(null);
        if (user.getRole().getId() == 2) {
            Doctor doctor = doctorRepository.getDoctorByUserId(user.getId());
            if (doctor != null) {
                doctor.setStatus(true);
                doctorRepository.save(doctor);
            }
        }
        userRepository.save(user);
        return String.format(UNLOCK_ACCOUNT, email);
    }

    @Override
    public List<MedicalSchedule> getScheduleListOfUser(int userId) {
        User user = userRepository.getUserById(userId);
        List<MedicalSchedule> scheduleList = medicalScheduleRepository.getByUserId(user.getId());
        return scheduleList;
    }

    @Override
    public List<MedicalSchedule> getScheduleListOfDoctor(int doctorId) {
        Doctor doctor = doctorRepository.getDoctorById(doctorId);
        List<MedicalSchedule> scheduleList = medicalScheduleRepository.getByDoctorId(doctor.getId());
        return scheduleList;
    }

    @Override
    public MedicalSchedule getScheduleDetail(int scheduleId) {
        Optional<MedicalSchedule> medicalSchedule = medicalScheduleRepository.findById(scheduleId);
        if (!medicalSchedule.isPresent()) {
            throw new NoResultException(NOT_FOUND_SCHEDULE);
        }
        return medicalSchedule.get();
    }

    private User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Gender getGenderEnumName(String gender) {
        return Gender.valueOf(gender);
    }
}
