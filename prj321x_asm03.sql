drop database if exists prj321x_asm03;

create database prj321x_asm03;

use prj321x_asm03;

create table roles(
	id int not null auto_increment primary key,
    name varchar(50)
);

create table users(
	id int not null auto_increment primary key,
    name varchar(100) not null,
    gender enum("Nam", "Nữ", "Other") not null,
    email varchar(200) not null unique,
    phone varchar(20) not null,
    address varchar(255) not null,
    password char(68) not null,
    role_id int not null,
    is_not_blocked tinyint not null default 1,
    created_at datetime default now(),
    lock_reason varchar(255),
    description text,
    study_process text,
    achievements text,
    disease_treatment text,
    foreign key(role_id) references roles(id)
);

create table password_reset_tokens(
	id int not null auto_increment primary key,
    token varchar(45) not null,
    expiry_time datetime not null,
    user_id int not null,
    foreign key(user_id) references users(id)
);

create table specializations(
	id int not null auto_increment primary key,
    name varchar(255) not null,
    description text not null
);

create table clinics(
	id int not null auto_increment primary key,
    name varchar(255) not null,
    address varchar(255) not null,
    phone varchar(20) not null,
    work_time varchar(255) not null,
    status tinyint not null default 1,
    created_at datetime default now()
);

create table clinic_specialties(
	id int not null auto_increment primary key,
    clinic_id int not null,
    specialization_id int not null,
    foreign key(clinic_id) references clinics(id),
    foreign key(specialization_id) references specializations(id)
);

create table doctors(
	id int not null auto_increment primary key,
    user_id int not null,
    clinic_id int not null,
    specialization_id int not null,
    examination_price varchar(100) not null,
    status tinyint not null default 1,
    reason varchar(255),
    transfer_at datetime default now(),
    foreign key(user_id) references users(id),
    foreign key(clinic_id) references clinics(id),
    foreign key(specialization_id) references specializations(id)
);

create table medical_schedules(
	id int not null auto_increment primary key,
    doctor_id int not null,
    user_id int not null,
    name varchar(100) not null,
    gender varchar(20) not null,
    address varchar(255) not null,
    date date not null,
    time time not null,
    examination_price varchar(100) not null,
    doctor_confirm tinyint not null,
    created_at datetime default now(),
    foreign key(doctor_id) references doctors(id),
    foreign key(user_id) references users(id)
);

create table health_results(
	id int not null auto_increment primary key,
    medical_schedule_id int not null,
    pathology varchar(255) not null,
    description text not null,
    foreign key(medical_schedule_id) references medical_schedules(id)
);

insert into roles 
values  (1, "ROLE_ADMIN"), 
		(2, "ROLE_DOCTOR"), 
        (3, "ROLE_USER");
        
insert into users (
		name, gender, email, phone, 
		address, password, role_id) 
values ("Admin", "Other", "admin@admin.com", "0123456789", 
		"Hà Nội", "$2a$10$FjzdO5LYxjeyCP2C65lbVeRPus9x3JDerC4lNjnmQlf3yWSE3ui6G", 1);
        
insert into specializations (name, description)
	values ("Cơ xương khớp", "Chuyên chữa cơ xương khớp"),
           ("Tiêu hóa", "Chuyên chữa tiêu hóa"),
           ("Tim mạch", "Chuyên chữa tim mạch"),
           ("Cột sống", "Chuyên chữa cột sống"),
           ("Y học cổ truyền", "Chuyên chữa với thảo dược"),
           ("Châm cứu", "Xua tan mệt mỏi với châm cứu"),
           ("Sản phụ khoa", "Chăm sóc sức khỏe sản phụ"),
           ("Siêu âm thai", "Chăm sóc sớm sức khỏe cho trẻ"),
           ("Nhi khoa", "Để trẻ luôn khỏe"),
           ("Da liễu", "Chuyên chữa da liễu"),
           ("Hô hấp", "Chuyên chữa hô hấp"),
           ("Chuyên khoa mắt", "Chuyên chữa mắt"),
           ("Nha khoa", "Chuyên chữa răng"),
           ("Phục hồi chức năng", "Vật lý trị liệu và phục hồi chức năng");

insert into clinics (name, address, phone, work_time) 
	values ("Phòng khám Cơ xương khớp Bảo Ngọc", "Số 73 ngõ 109 Hoàng Ngân - Thanh Xuân - Hà Nội", 
			"0321645798", "17:30-19:00"),
            ("Hệ thống Y tế Thu Cúc cơ sở Thụy Khuê", "286 Thụy Khuê, quận Tây Hồ, Hà Nội", 
			"0213245651", "8:00-11:00, 13:30-16:00 thứ 2 tới thứ 7"),
            ("Phòng khám Đa khoa Mediplus", "99 phố Tân Mai, Tân Mai, Hoàng Mai, Hà Nội", 
			"0321354552", "13:30-17:00"),
            ("Phòng khám Đa khoa SIM Medical Center", "241đường Hòa Bình, P. Hiệp Tân, Q.Tân Phú, TP. HCM", 
			"0324512457", "13:30-17:00 thứ 2, 4, 6"),
            ("Phòng khám Đa khoa Việt Mỹ", "Hoàng Mai - Hà Nội", 
			"0354512478", "18:30-21:00"),
            ("Phòng Khám Đa Khoa MSC Clinic", "204 Nguyễn Tuân, phường Nhân Chính, quận Thanh Xuân, TP Hà Nội", 
			"0324521451", "17:30-19:00"),
            ("Phòng khám chuyên khoa phụ sản - Siêu âm Dr Chường", "16 Hoàng Ngân, Trung Hoà, Cầu Giấy, Hà Nội", 
			"0231514512", "8:00-17:30");
            
insert into clinic_specialties(clinic_id, specialization_id)
	values  (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 10),
			(2, 1), (2, 3), (2, 5), (2, 6), (2, 8), (2, 10),
            (3, 1), (3, 5), (3, 7), (3, 9), (3, 11), (3, 12),
            (4, 3), (4, 5), (4, 8), (4, 12), (4, 13), (4, 14),
            (5, 1), (5, 6), (5, 11), (5, 12), (5, 13), (5, 14),
            (6, 4), (6, 5), (6, 8), (6, 10), (6, 11), (6, 13),
            (5, 2), (5, 6), (5, 7), (5, 9), (5, 13), (5, 14);

