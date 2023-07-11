SELECT s.* FROM specializations s JOIN 
	(SELECT count(specialization_id) AS count, specialization_id 
	FROM medical_schedules ms JOIN doctors d ON ms.doctor_id = d.id 
	GROUP BY specialization_id) a 
ON s.id = a.specialization_id ORDER BY count DESC LIMIT 5;

SELECT c.* FROM clinics c JOIN 
	(SELECT count(clinic_id) AS count, clinic_id 
	FROM medical_schedules ms JOIN doctors d ON ms.doctor_id = d.id 
	GROUP BY clinic_id) a 
ON c.id = a.clinic_id ORDER BY count DESC LIMIT 5;

SELECT c.* FROM clinics c JOIN 
	(SELECT c.id AS cli_id 
	FROM specializations s JOIN clinic_specialties sc ON s.id = sc.specialization_id
	JOIN clinics c ON c.id = sc.clinic_id
	WHERE CONCAT_WS(' ', c.name, c.address, s.name, s.description) LIKE '%hà nội%' GROUP BY cli_id) a
ON c.id = a.cli_id;

SELECT d.* FROM doctors d JOIN 
	(SELECT d.id AS doctor_id
	FROM doctors d JOIN users u ON d.user_id = u.id
    JOIN clinics c ON d.clinic_id = c.id
	WHERE CONCAT_WS(' ', u.name, u.description, u.disease_treatment, c.name, c.address, d.examination_price) LIKE '%400%') a
ON d.id = a.doctor_id;

SELECT d.* FROM doctors d JOIN 
	(SELECT d.id AS doctor_id
	FROM doctors d JOIN specializations s ON d.specialization_id = s.id
	WHERE CONCAT('Bác sĩ ', s.name, ' ') LIKE '%bác sĩ cơ xương%') a
ON d.id = a.doctor_id;



