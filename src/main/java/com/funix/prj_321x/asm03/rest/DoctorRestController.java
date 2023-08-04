package com.funix.prj_321x.asm03.rest;

import com.funix.prj_321x.asm03.entity.HealthResult;
import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import com.funix.prj_321x.asm03.exception.NotInChargeException;
import com.funix.prj_321x.asm03.exception.ResultExistException;
import com.funix.prj_321x.asm03.exception.ScheduleHasCancelException;
import com.funix.prj_321x.asm03.exception.ScheduleNotFoundException;
import com.funix.prj_321x.asm03.repository.HealthResultRepository;
import com.funix.prj_321x.asm03.repository.MedicalScheduleRepository;
import com.funix.prj_321x.asm03.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorRestController {

    private DoctorService doctorService;
    private MedicalScheduleRepository medicalScheduleRepository;
    private HealthResultRepository healthResultRepository;

    public DoctorRestController(DoctorService doctorService,
                                MedicalScheduleRepository medicalScheduleRepository,
                                HealthResultRepository healthResultRepository) {
        this.doctorService = doctorService;
        this.medicalScheduleRepository = medicalScheduleRepository;
        this.healthResultRepository = healthResultRepository;
    }

    @GetMapping("/schedules")
    @Operation(summary = "Show list of scheduling medical")
    public List<MedicalSchedule> scheduleList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.getScheduleList(email);
    }

    // Nhận lịch khám
    @PutMapping("/schedules/{scheduleId}")
    @Operation(summary = "Confirm medical appointment")
    public MedicalSchedule confirmSchedule(@PathVariable("scheduleId") int scheduleId) throws ScheduleNotFoundException, ScheduleHasCancelException, NotInChargeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.confirmSchedule(email, scheduleId);
    }

    // Hủy lịch khám
    @DeleteMapping("/schedules/{scheduleId}")
    @Operation(summary = "Cancel medical appointment")
    public MedicalSchedule cancelSchedule(@PathVariable("scheduleId") int scheduleId,
                                          @RequestParam("reason") String reason) throws ScheduleNotFoundException, NotInChargeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.cancelSchedule(scheduleId, email, reason);
    }

    // Tạo kết quả khám sau buổi khám
    @PostMapping("/results")
    @Operation(summary = "Create result after medical examination")
    public HealthResult createResult(@RequestParam("scheduleId") int scheduleId,
                                     @RequestParam("pathology") String pathology,
                                     @RequestParam("description") String description) throws ScheduleNotFoundException, ScheduleHasCancelException, NotInChargeException, ResultExistException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.createResult(email, scheduleId, pathology, description);
    }

    // Cập nhật lại kết quả
    @PutMapping("/results/{resultId}")
    @Operation(summary = "Update result after medical examination")
    public HealthResult updateResult(@PathVariable("resultId") int resultId,
                                     @RequestParam("pathology") String pathology,
                                     @RequestParam("description") String description) throws NotInChargeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.updateResult(email, resultId, pathology, description);
    }

    // Danh sách đã thăm khám
    @GetMapping("/results")
    @Operation(summary = "Show list of medical examination results")
    public List<HealthResult> resultList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.getResultList(email);
    }
}
