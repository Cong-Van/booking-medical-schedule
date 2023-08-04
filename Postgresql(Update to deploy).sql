CREATE TABLE roles (
    id serial PRIMARY KEY,
    name varchar(50)
);

CREATE TABLE users (
    id serial PRIMARY KEY,
    name varchar(100) NOT NULL,
    gender varchar(20) CHECK (gender IN ('Nam', 'Nữ', 'Other')) NOT NULL,
    email varchar(200) NOT NULL UNIQUE,
    phone varchar(20) NOT NULL,
    address varchar(255) NOT NULL,
    password varchar(68) NOT NULL,
    role_id int NOT NULL,
    is_not_blocked boolean DEFAULT true NOT NULL,
    created_at timestamp DEFAULT current_timestamp,
    lock_reason varchar(255),
    description text,
    study_process text,
    achievements text,
    disease_treatment text,
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE password_reset_tokens (
    id serial PRIMARY KEY,
    token varchar(45) NOT NULL,
    expiry_time timestamp NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE specializations (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    description text NOT NULL
);

CREATE TABLE clinics (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    address varchar(255) NOT NULL,
    phone varchar(20) NOT NULL,
    work_time varchar(255) NOT NULL,
    status boolean DEFAULT true NOT NULL,
    created_at timestamp DEFAULT current_timestamp
);

CREATE TABLE clinic_specialties (
    id serial PRIMARY KEY,
    clinic_id int NOT NULL,
    specialization_id int NOT NULL,
    FOREIGN KEY (clinic_id) REFERENCES clinics (id),
    FOREIGN KEY (specialization_id) REFERENCES specializations (id)
);

CREATE TABLE doctors (
    id serial PRIMARY KEY,
    user_id int NOT NULL,
    clinic_id int NOT NULL,
    specialization_id int NOT NULL,
    examination_price varchar(100) NOT NULL,
    status boolean DEFAULT true NOT NULL,
    reason varchar(255),
    transfer_at timestamp DEFAULT current_timestamp,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (clinic_id) REFERENCES clinics (id),
    FOREIGN KEY (specialization_id) REFERENCES specializations (id)
);

CREATE TABLE medical_schedules (
    id serial PRIMARY KEY,
    doctor_id int NOT NULL,
    user_id int NOT NULL,
    name varchar(100) NOT NULL,
    gender varchar(20) NOT NULL,
    address varchar(255) NOT NULL,
    date date NOT NULL,
    time time NOT NULL,
    examination_price varchar(100) NOT NULL,
    doctor_confirm boolean NOT NULL,
    created_at timestamp DEFAULT current_timestamp,
    FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE health_results (
    id serial PRIMARY KEY,
    medical_schedule_id int NOT NULL,
    pathology varchar(255) NOT NULL,
    description text NOT NULL,
    FOREIGN KEY (medical_schedule_id) REFERENCES medical_schedules (id)
);

INSERT INTO roles (name) 
VALUES ('ROLE_ADMIN'), 
       ('ROLE_DOCTOR'), 
       ('ROLE_USER');

INSERT INTO users (name, gender, email, phone, address, password, role_id) 
VALUES ('Admin', 'Other', 'admin@admin.com', '0123456789', 
        'Hà Nội', '$2a$10$FjzdO5LYxjeyCP2C65lbVeRPus9x3JDerC4lNjnmQlf3yWSE3ui6G', 1);

INSERT INTO specializations (name, description)
VALUES ('Cơ xương khớp', 'Chuyên chữa cơ xương khớp'),
       ('Tiêu hóa', 'Chuyên chữa tiêu hóa'),
       ('Tim mạch', 'Chuyên chữa tim mạch'),
       ('Cột sống', 'Chuyên chữa cột sống'),
       ('Y học cổ truyền', 'Chuyên chữa với thảo dược'),
       ('Châm cứu', 'Xua tan mệt mỏi với châm cứu'),
       ('Sản phụ khoa', 'Chăm sóc sức khỏe sản phụ'),
       ('Siêu âm thai', 'Chăm sóc sớm sức khỏe cho trẻ'),
       ('Nhi khoa', 'Để trẻ luôn khỏe'),
       ('Da liễu', 'Chuyên chữa da liễu'),
       ('Hô hấp', 'Chuyên chữa hô hấp'),
       ('Chuyên khoa mắt', 'Chuyên chữa mắt'),
       ('Nha khoa', 'Chuyên chữa răng'),
       ('Phục hồi chức năng', 'Vật lý trị liệu và phục hồi chức năng');

INSERT INTO clinics (name, address, phone, work_time) 
VALUES ('Phòng khám Cơ xương khớp Bảo Ngọc', 'Số 73 ngõ 109 Hoàng Ngân - Thanh Xuân - Hà Nội', 
        '0321645798', '17:30-19:00'),
       ('Hệ thống Y tế Thu Cúc cơ sở Thụy Khuê', '286 Thụy Khuê, quận Tây Hồ, Hà Nội', 
        '0213245651', '8:00-11:00, 13:30-16:00 thứ 2 tới thứ 7'),
       ('Phòng khám Đa khoa Mediplus', '99 phố Tân Mai, Tân Mai, Hoàng Mai, Hà Nội', 
        '0321354552', '13:30-17:00'),
       ('Phòng khám Đa khoa SIM Medical Center', '241đường Hòa Bình, P. Hiệp Tân, Q.Tân Phú, TP. HCM', 
        '0324512457', '13:30-17:00 thứ 2, 4, 6'),
       ('Phòng khám Đa khoa Việt Mỹ', 'Hoàng Mai - Hà Nội', 
        '0354512478', '18:30-21:00'),
       ('Phòng Khám Đa Khoa MSC Clinic', '204 Nguyễn Tuân, phường Nhân Chính, quận Thanh Xuân, TP Hà Nội', 
        '0324521451', '17:30-19:00'),
       ('Phòng khám chuyên khoa phụ sản - Siêu âm Dr Chường', '16 Hoàng Ngân, Trung Hoà, Cầu Giấy, Hà Nội', 
        '0231514512', '8:00-17:30');
            
INSERT INTO clinic_specialties (clinic_id, specialization_id)
VALUES  (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 10),
        (2, 1), (2, 3), (2, 5), (2, 6), (2, 8), (2, 10),
        (3, 1), (3, 5), (3, 7), (3, 9), (3, 11), (3, 12),
        (4, 3), (4, 5), (4, 8), (4, 12), (4, 13), (4, 14),
        (5, 1), (5, 6), (5, 11), (5, 12), (5, 13), (5, 14),
        (6, 4), (6, 5), (6, 8), (6, 10), (6, 11), (6, 13),
        (5, 2), (5, 6), (5, 7), (5, 9), (5, 13), (5, 14);