package com.funix.prj_321x.asm03.rest;

import com.funix.prj_321x.asm03.DTO.UserDTO;
import com.funix.prj_321x.asm03.common.HttpResponse;
import com.funix.prj_321x.asm03.entity.Doctor;
import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import com.funix.prj_321x.asm03.entity.User;
import com.funix.prj_321x.asm03.exception.*;
import com.funix.prj_321x.asm03.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.funix.prj_321x.asm03.constant.ExceptionConstant.PASSWORD_MISMATCH;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/add-doctor-account")
    public User addDoctorAccount(@RequestBody UserDTO userDTO) throws PasswordMismatchException, EmailExistException {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPass())) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
        return adminService.addDoctorAccount(userDTO.getName(), userDTO.getGender(), userDTO.getEmail(),
                userDTO.getPhone(), userDTO.getAddress(), userDTO.getPassword(),
                userDTO.getDescription(), userDTO.getStudyProcess(), userDTO.getAchievements(), userDTO.getDiseaseTreatment());
    }

    // Điều chuyển bác sĩ vào cơ sở y tế
    @PostMapping("/doctors")
    public Doctor transferDoctor(@RequestParam("userId") int userId,
                                 @RequestParam("clinicId") int clinicId,
                                 @RequestParam("specializationId") int specializationId,
                                 @RequestParam("examinationPrice") String examinationPrice) throws EmailExistException, ClinicNotFoundException, SpecializationOfClinicNotFoundException {
        return adminService.transferDoctor(userId, clinicId, specializationId, examinationPrice);
    }

    // Cập nhật thông tin điều chuyển công tác cho bác sĩ
    @PutMapping("/doctors/{doctorId}")
    public Doctor updateDoctor(@PathVariable("doctorId") int doctorId,
                               @RequestParam("clinicId") int clinicId,
                               @RequestParam("specializationId") int specializationId,
                               @RequestParam("examinationPrice") String examinationPrice) throws UserNotFoundException, ClinicNotFoundException, SpecializationOfClinicNotFoundException {
        return adminService.updateDoctor(doctorId, clinicId, specializationId, examinationPrice);
    }

    @PutMapping("/lock-account/{email}")
    public ResponseEntity<HttpResponse> lockAccount(@PathVariable("email") String email,
                                                    @RequestParam String reason) throws EmailNotFoundException {
        String message = adminService.lockAccount(email, reason);
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK, OK.getReasonPhrase(), message), OK);
    }

    @PutMapping("unlock-account/{email}")
    public ResponseEntity<HttpResponse> unlockAccount(@PathVariable("email") String email) throws EmailNotFoundException {
        String message = adminService.unlockAccount(email);
        return new ResponseEntity<>(new HttpResponse(OK.value(), OK, OK.getReasonPhrase(), message), OK);
    }

    // Danh sách lịch khám của bệnh nhân
    @GetMapping("/schedules/user")
    public List<MedicalSchedule> scheduleListOfUser(@RequestParam("userId") int userId) {
        return adminService.getScheduleListOfUser(userId);
    }

    // Danh sách lịch khám của bác sĩ
    @GetMapping("/schedules/doctor")
    public List<MedicalSchedule> scheduleListOfDoctor(@RequestParam("doctorId") int doctorId) {
        return adminService.getScheduleListOfDoctor(doctorId);
    }

    // Chi tiết thông tin buổi đặt lịch
    @GetMapping("/schedules/{scheduleId}")
    public MedicalSchedule scheduleDetail(@PathVariable("scheduleId") int scheduleId) {
        return adminService.getScheduleDetail(scheduleId);
    }
}
