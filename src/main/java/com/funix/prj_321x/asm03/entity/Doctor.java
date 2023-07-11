package com.funix.prj_321x.asm03.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // Liên kết với tài khoản và thông tin của bác sĩ
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @Column(name = "examination_price")
    private String examinationPrice;

    @Column(name = "status")
    private boolean status;

    @Column(name = "transfer_at")
    private LocalDateTime transferAt;

    public Doctor() {}

    public Doctor(User user, Clinic clinic, Specialization specialization, String examinationPrice) {
        this.user = user;
        this.clinic = clinic;
        this.specialization = specialization;
        this.examinationPrice = examinationPrice;
        this.setStatus(true);
        this.transferAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getExaminationPrice() {
        return examinationPrice;
    }

    public void setExaminationPrice(String examinationPrice) {
        this.examinationPrice = examinationPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getTransferAt() {
        return transferAt;
    }

    public void setTransferAt(LocalDateTime transferAt) {
        this.transferAt = transferAt;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm03.entity.Doctor{" +
                "id=" + id +
                ", user=" + user +
                ", clinic=" + clinic +
                ", specialization=" + specialization +
                ", examinationPrice='" + examinationPrice + '\'' +
                ", status=" + status +
                ", transferAt=" + transferAt +
                '}';
    }
}
