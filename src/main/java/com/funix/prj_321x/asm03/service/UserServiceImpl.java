package com.funix.prj_321x.asm03.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.funix.prj_321x.asm03.common.Gender;
import com.funix.prj_321x.asm03.entity.*;
import com.funix.prj_321x.asm03.exception.EmailNotFoundException;
import com.funix.prj_321x.asm03.exception.UserNotFoundException;
import com.funix.prj_321x.asm03.repository.*;
import com.funix.prj_321x.asm03.exception.EmailExistException;
import com.funix.prj_321x.asm03.security.UserPrincipal;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.*;
import java.util.List;
import java.util.UUID;

import static com.funix.prj_321x.asm03.constant.SecurityConstant.ADMINISTRATION;
import static com.funix.prj_321x.asm03.constant.UserImplConstant.*;

@Service
public class UserServiceImpl implements UserService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private DoctorRepository doctorRepository;
    private ClinicRepository clinicRepository;
    private SpecializationRepository specializationRepository;
    private MedicalScheduleRepository medicalScheduleRepository;
    private HealthResultRepository healthResultRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private JavaMailSender javaMailSender;

    public UserServiceImpl(UserRepository userRepository,
                           DoctorRepository doctorRepository,
                           ClinicRepository clinicRepository,
                           SpecializationRepository specializationRepository,
                           MedicalScheduleRepository medicalScheduleRepository,
                           HealthResultRepository healthResultRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.specializationRepository = specializationRepository;
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.healthResultRepository = healthResultRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            LOGGER.error(String.format(EMAIL_IS_NOT_REGISTERED, email));
            throw new UsernameNotFoundException(String.format(EMAIL_IS_NOT_REGISTERED, email));
        }
        UserPrincipal userPrincipal = new UserPrincipal(user);
        LOGGER.info(FOUND_USER_BY_EMAIL + email);
        return userPrincipal;
    }

    @Override
    public User register(String name, String gender, String email, String phone, String address, String password) throws EmailExistException {
        if (getUserByEmail(email) != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXIST);
        }
        User newUser =  new User(name, getGenderEnumName(gender).name(), email, phone, address, encodePassword(password));
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public String resetPassword(String email) throws EmailExistException {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            LOGGER.error(String.format(EMAIL_IS_NOT_REGISTERED, email));
            throw new EmailExistException(String.format(EMAIL_IS_NOT_REGISTERED, email));
        }
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        return token;
    }

    public String sendEmail(String email, String resetPasswordUrl) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String subject = "Link to reset password";
        String content = "<p>Hello,</p>" +
                "<p>You have requested to reset your password. Click the link below to change: </p>" +
                "<p style=\"color: blue;\"><a href=\"" + resetPasswordUrl + "\">Change password</a></p>" +
                "<p>Ignore this email if you do remember your password, or you have not made the request.</p>";

        helper.setFrom(EMAIL_TO_SEND, ADMINISTRATION);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);

        javaMailSender.send(message);
        return HAVE_SENT_RESET_PASSWORD_LINK_TO_EMAIL;
    }

    @Override
    public void validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            throw new NoResultException(INVALID_VERIFICATION_TOKEN);
        }
        Instant expiryTime = passwordResetToken.getExpiryTime().toInstant(ZoneOffset.UTC);
        if (expiryTime.isBefore(Instant.now())) {
            throw new TokenExpiredException(LINK_ALREADY_EXPIRED, expiryTime);
        }
    }

    @Override
    public String updatePassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(passwordResetToken);
        return PASSWORD_HAS_BEEN_CHANGED;
    }

    @Override
    public List<Specialization> getTopSpecializations() {
        return specializationRepository.findTopSpecializations();
    }

    @Override
    public List<Clinic> getTopClinics() {
        return clinicRepository.findTopClinics();
    }

    @Override
    public List<Clinic> getClinicByKeyword(String keyword) {
        return clinicRepository.findByKeyword(keyword);
    }

    @Override
    public List<Doctor> getDoctorByKeyword(String keyword) {
        return doctorRepository.findByKeyword(keyword);
    }

    @Override
    public List<Doctor> getDoctorBySpecialization(String keyword) {
        return doctorRepository.findBySpecialization(keyword);
    }

    @Override
    public MedicalSchedule bookSchedule(String email, int doctorId, String name, String gender, String address, String date, String time, String examinationPrice) {
        User user = userRepository.getUserByEmail(email);
        Doctor doctor = doctorRepository.getDoctorById(doctorId);
        if (doctor == null) {
            throw new NoResultException(DOCTOR_HAS_NOT_BEEN_TRANSFERRED);
        }
        if (!doctor.isStatus()) {
            throw new NoResultException(DOCTOR_NO_LONGER_WORKING);
        }
        MedicalSchedule newSchedule =  new MedicalSchedule(doctor, user, name, getGenderEnumName(gender).name(), address,
                LocalDate.parse(date), LocalTime.parse(time), examinationPrice);

        medicalScheduleRepository.save(newSchedule);
        return newSchedule;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public List<HealthResult> getResultList(String email) {
        User user = userRepository.getUserByEmail(email);
        List<HealthResult> resultList = healthResultRepository.findAllByUserId(user.getId());
        return resultList;
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId());
        if (passwordResetToken != null) {
            passwordResetTokenRepository.delete(passwordResetToken);
        }
        PasswordResetToken newToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(newToken);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Gender getGenderEnumName(String gender) {
        return Gender.valueOf(gender);
    }
}
