package com.funix.prj_321x.asm03.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_not_blocked")
    private boolean isNotBlocked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "lock_reason")
    private String lockReason;

    @Column(name = "description")
    private String description;

    @Column(name = "study_process")
    private String studyProcess;

    @Column(name = "achievements")
    private String achievements;

    @Column(name = "disease_treatment")
    private String diseaseTreatment;

    public User() {}

    // Tạo tài khoản người dùng
    public User(String name, String gender, String email, String phone, String address, String password) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.role = new Role();
        this.role.setId(3);
        this.isNotBlocked = true;
        this.createdAt = LocalDateTime.now();
    }

    // Tạo tài khoản bác sĩ
    public User(String name, String gender, String email, String phone, String address, String password,
                String description, String studyProcess, String achievements, String diseaseTreatment) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.role = new Role();
        this.role.setId(2);
        this.isNotBlocked = true;
        this.createdAt = LocalDateTime.now();
        this.description = description;
        this.studyProcess = studyProcess;
        this.achievements = achievements;
        this.diseaseTreatment = diseaseTreatment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isNotBlocked() {
        return isNotBlocked;
    }

    public void setNotBlocked(boolean notBlocked) {
        isNotBlocked = notBlocked;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStudyProcess() {
        return studyProcess;
    }

    public void setStudyProcess(String studyProcess) {
        this.studyProcess = studyProcess;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getDiseaseTreatment() {
        return diseaseTreatment;
    }

    public void setDiseaseTreatment(String diseaseTreatment) {
        this.diseaseTreatment = diseaseTreatment;
    }

    @Override
    public String toString() {
        return "com.funix.prj_321x.asm03.entity.User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", isActive=" + isNotBlocked +
                ", createdAt=" + createdAt +
                ", deleteReason='" + lockReason + '\'' +
                ", description='" + description + '\'' +
                ", studyProcess='" + studyProcess + '\'' +
                ", achievements='" + achievements + '\'' +
                ", diseaseTreatment='" + diseaseTreatment + '\'' +
                '}';
    }
}
