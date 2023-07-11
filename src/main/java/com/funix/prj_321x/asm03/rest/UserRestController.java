package com.funix.prj_321x.asm03.rest;

import com.funix.prj_321x.asm03.DTO.UserDTO;
import com.funix.prj_321x.asm03.entity.*;
import com.funix.prj_321x.asm03.exception.EmailExistException;
import com.funix.prj_321x.asm03.exception.PasswordMismatchException;
import com.funix.prj_321x.asm03.security.JwtTokenProvider;
import com.funix.prj_321x.asm03.security.UserPrincipal;
import com.funix.prj_321x.asm03.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.funix.prj_321x.asm03.constant.ExceptionConstant.PASSWORD_MISMATCH;
import static com.funix.prj_321x.asm03.constant.SecurityConstant.JWT_TOKEN_HEADER;

@RestController
@RequestMapping("/user")
public class UserRestController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    public UserRestController(UserService userService,
                              JwtTokenProvider jwtTokenProvider,
                              AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        authenticate(email, password);

        User loginUser = userService.getUserByEmail(email);
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders headers = getJwtHeader(userPrincipal);

        return new ResponseEntity<>(loginUser, headers, HttpStatus.OK);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) throws EmailExistException, PasswordMismatchException {

        if (!userDTO.getPassword().equals(userDTO.getConfirmPass())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
        return userService.register(userDTO.getName(), userDTO.getGender(), userDTO.getEmail(),
                userDTO.getPhone(), userDTO.getAddress(), userDTO.getPassword());
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email) throws EmailExistException, MessagingException, UnsupportedEncodingException {
        String token = userService.resetPassword(email);
        String resetPasswordUrl = getCurrentUrl() + "/update?token=" + token;
        return userService.sendEmail(email, resetPasswordUrl);
    }

    @PostMapping("/reset-password/update")
    public String changePassword(@RequestParam("token") String token,
                                 @RequestBody UserDTO userDTO) throws PasswordMismatchException {

        userService.validatePasswordResetToken(token);

        if (!userDTO.getPassword().equals(userDTO.getConfirmPass())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
        return userService.updatePassword(token, userDTO.getPassword());
    }

    @GetMapping("/info")
    public User showInformation() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByEmail(email);
    }

    @GetMapping("/results")
    public List<HealthResult> resultList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getResultList(email);
    }

    @GetMapping("/specializations/top")
    public List<Specialization> topSpecializations() {
        return userService.getTopSpecializations();
    }

    @GetMapping("/clinics/top")
    public List<Clinic> topClinics() {
        return userService.getTopClinics();
    }

    // Tìm kiếm chung (tìm kiếm bệnh viện)
    @GetMapping("/clinics/find")
    public List<Clinic> findClinics(@RequestParam("keyword") String keyword) {
        return userService.getClinicByKeyword(keyword);
    }

    // Tìm kiếm chung (tìm kiếm bác sĩ)
    @GetMapping("/doctors/find")
    public List<Doctor> findDoctors(@RequestParam("keyword") String keyword) {
        return userService.getDoctorByKeyword(keyword);
    }

    // Tìm kiếm bác sĩ theo chuyên khoa
    @GetMapping("/doctors/specialization")
    public List<Doctor> findDoctorBySpecialization(@RequestParam("keyword") String keyword) {
        return userService.getDoctorBySpecialization(keyword);
    }

    @PostMapping("/schedules")
    public MedicalSchedule bookSchedule(@RequestParam("doctorId") int doctorId,
                                        @RequestParam("name") String name,
                                        @RequestParam("gender") String gender,
                                        @RequestParam("address") String address,
                                        @RequestParam("date") String date,
                                        @RequestParam("time") String time,
                                        @RequestParam("examinationPrice") String examinationPrice) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.bookSchedule(email, doctorId, name, gender, address, date, time, examinationPrice);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private String getCurrentUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRequestURL().toString();
    }
}
