package com.funix.prj_321x.asm03.service;

import com.funix.prj_321x.asm03.entity.*;
import com.funix.prj_321x.asm03.exception.EmailExistException;
import com.funix.prj_321x.asm03.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService extends UserDetailsService {

    User register(String name, String gender, String email, String phone, String address, String password) throws EmailExistException;

    User getUserByEmail(String email);

    String resetPassword(String email) throws EmailExistException;

    String sendEmail(String email, String resetPasswordUrl) throws MessagingException, UnsupportedEncodingException;

    void validatePasswordResetToken(String token);

    String updatePassword(String token, String newPassword);

    List<Specialization> getTopSpecializations();

    List<Clinic> getTopClinics();

    List<Clinic> getClinicByKeyword(String keyword);

    List<Doctor> getDoctorByKeyword(String keyword);

    List<Doctor> getDoctorBySpecialization(String keyword);

    MedicalSchedule bookSchedule(String email, int doctorId, String name, String gender, String address, String date, String time, String examinationPrice);

    List<HealthResult> getResultList(String email);
}
