package com.funix.prj_321x.asm03.service;

import com.funix.prj_321x.asm03.entity.HealthResult;
import com.funix.prj_321x.asm03.entity.MedicalSchedule;
import com.funix.prj_321x.asm03.exception.NotInChargeException;
import com.funix.prj_321x.asm03.exception.ResultExistException;
import com.funix.prj_321x.asm03.exception.ScheduleHasCancelException;
import com.funix.prj_321x.asm03.exception.ScheduleNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface DoctorService {
    MedicalSchedule confirmSchedule(String email, int scheduleId) throws ScheduleNotFoundException, ScheduleHasCancelException, NotInChargeException;

    MedicalSchedule cancelSchedule(int scheduleId, String email, String reason) throws ScheduleNotFoundException, NotInChargeException;

    HealthResult createResult(String email, int scheduleId, String pathology, String description) throws ScheduleNotFoundException, NotInChargeException, ScheduleHasCancelException, ResultExistException;

    List<HealthResult> getResultList(String email);

    List<MedicalSchedule> getScheduleList(String email);

    HealthResult updateResult(String email, int resultId, String pathology, String description) throws NotInChargeException;
}
