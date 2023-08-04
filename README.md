# booking-medical-schedule

- Introduction

The project of booking medical schedule examination provides medical examination and treatment solutions for 
patients, accompanied by utilities for examination through: searching, scheduling, looking for records, ... 
There for, creating a trust as well as peace of mind about the medical team as well as the corresponding system.


- Prerequisites

Before you continue, ensure you meet the following requirements:
You have installed JDK 17 or over.
You are using an IDE (Eg: IntelliJ IDEA) and a DBMS (mysql or postgresql). Currently, using postgresql.
You can check API on a support tool like Postman.
Or you can view API list on: https://booking-medical-schedule.onrender.com

- Use

There are 3 roles: 
Admin, account is granted by default. 
Doctor, account created and granted by admin. 
Patient, self-created new account to use.
Use the account to login using the Login API.
Receive JWT, use JWT to authenticate with APIs that require authentication. 
(Copy value of Jwt-Token and Paste to Authorization: "Bearer " + value). 
Eg: Jwt-Token: 123456. Authorization: Bearer 123456.

You can try admin function with the default account. 
TK: admin@admin.com 
MK: 123.

- List of features - LoF

Admin: 
Create doctor account. 
Update information for doctor account. 
Lock, unlock patient account or doctor account. 
View list of scheduling medical of patient, doctor. 
View details of scheduling medical. 

Doctor:
View list of medical appointments. 
Confirm, cancel medical appointment. 
Create result after medical examination. 
Update medical examination results. 
View list of medical examination results.

Patient:
Reset password by registered email when forgotten. 
View your individual information. 
View list of your medical examination results.
Looking for doctor or medical clinic.
Booking medical schedules.

- Technologies Used

Spring Boot 3, Spring Security, JPA.
Swagger, Docker and Render website to publish openAPI.

- Acknowledgments

Security by JWT. Authenticate users through login. Authorization by role. 
This is only REST API and many features are missing. Look forward to your comments.

Thank you!

