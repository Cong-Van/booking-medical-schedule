package com.funix.prj_321x.asm03.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "health_results")
public class HealthResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Buổi đặt lịch khám tương ứng với kết quả khám
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medical_schedule_id")
    private MedicalSchedule medicalSchedule;

    @Column(name = "pathology")
    private String pathology;

    @Column(name = "description")
    private String description;

    public HealthResult() {}

    public HealthResult(MedicalSchedule medicalSchedule, String pathology, String description) {
        this.medicalSchedule = medicalSchedule;
        this.pathology = pathology;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MedicalSchedule getMedicalSchedule() {
        return medicalSchedule;
    }

    public void setMedicalSchedule(MedicalSchedule medicalSchedule) {
        this.medicalSchedule = medicalSchedule;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm03.entity.HealthResult{" +
                "id=" + id +
                ", medicalSchedule=" + medicalSchedule +
                ", pathology='" + pathology + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
