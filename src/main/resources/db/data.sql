-- 1. ROLES
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_RECEPTIONIST');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_DOCTOR');

-- 2. ESTADOS
INSERT INTO turn_status (id, name) VALUES (1, 'PENDIENTE');
INSERT INTO turn_status (id, name) VALUES (2, 'EN_ESPERA');
INSERT INTO turn_status (id, name) VALUES (3, 'EN_PROGRESO');
INSERT INTO turn_status (id, name) VALUES (4, 'COMPLETADO');
INSERT INTO turn_status (id, name) VALUES (5, 'CANCELADO');

-- 3. SERVICIOS
INSERT INTO services (id, name, base_price) VALUES (1, 'Consulta General', 50.00);
INSERT INTO services (id, name, base_price) VALUES (2, 'Limpieza Dental', 120.00);
INSERT INTO services (id, name, base_price) VALUES (3, 'Extracción Simple', 150.00);
INSERT INTO services (id, name, base_price) VALUES (4, 'Blanqueamiento', 300.00);
INSERT INTO services (id, name, base_price) VALUES (5, 'Ortodoncia (Mensual)', 250.00);

-- 4. USUARIOS (Pass: 123456)
INSERT INTO users (id, username, password, full_name, email, role_id) VALUES (1, 'admin', '$2a$10$R/h3.e..p/.W.i/z.k/.u.x/y.z.1.2.3.4.5', 'Administrador General', 'admin@clinica.com', 1);
INSERT INTO users (id, username, password, full_name, email, role_id) VALUES (2, 'recepcion', '$2a$10$R/h3.e..p/.W.i/z.k/.u.x/y.z.1.2.3.4.5', 'Laura Recepcionista', 'recepcion@clinica.com', 2);
INSERT INTO users (id, username, password, full_name, email, role_id) VALUES (3, 'dr_house', '$2a$10$R/h3.e..p/.W.i/z.k/.u.x/y.z.1.2.3.4.5', 'Gregory House', 'house@clinica.com', 3);
INSERT INTO users (id, username, password, full_name, email, role_id) VALUES (4, 'dra_grey', '$2a$10$R/h3.e..p/.W.i/z.k/.u.x/y.z.1.2.3.4.5', 'Meredith Grey', 'grey@clinica.com', 3);

-- 5. DOCTORES
INSERT INTO doctors (id, name, last_name, specialty, user_id) VALUES (1, 'Gregory', 'House', 'Diagnóstico Clínico', 3);
INSERT INTO doctors (id, name, last_name, specialty, user_id) VALUES (2, 'Meredith', 'Grey', 'Cirugía General', 4);

-- 6. PACIENTES
INSERT INTO patients (id, dni, name, last_name, phone, email) VALUES (1, '11111111', 'Carlos', 'Pérez', '555-1001', 'carlos@mail.com');
INSERT INTO patients (id, dni, name, last_name, phone, email) VALUES (2, '22222222', 'Maria', 'Gomez', '555-1002', 'maria@mail.com');
INSERT INTO patients (id, dni, name, last_name, phone, email) VALUES (3, '33333333', 'Pepito', 'Juarez', '555-1003', NULL);

-- 7. HISTORIAL
INSERT INTO medical_history_entries (patient_id, description, created_by_user_id) VALUES (1, 'Hipertensión controlada', 1);
INSERT INTO medical_history_entries (patient_id, description, created_by_user_id) VALUES (2, 'Sin antecedentes', 2);

-- 8. TURNOS (Aquí fallaba)
-- Usamos CURRENT_DATE para asegurar compatibilidad total o fechas fijas
INSERT INTO turns (id, patient_id, doctor_id, appointment_datetime, status_id, created_by_user_id) VALUES
(1, 1, 1, '2024-01-01 10:00:00', 4, 2);

INSERT INTO turns (id, patient_id, doctor_id, appointment_datetime, status_id, created_by_user_id) VALUES
(2, 2, 2, CURRENT_TIMESTAMP, 3, 2);

INSERT INTO turns (id, patient_id, doctor_id, appointment_datetime, status_id, created_by_user_id) VALUES
(3, 3, 1, '2025-12-25 09:00:00', 1, 2);

-- 9. SERVICIOS DEL TURNO
INSERT INTO turn_services (turn_id, service_id, price_at_time) VALUES (1, 1, 50.00);
INSERT INTO turn_services (turn_id, service_id, price_at_time) VALUES (1, 2, 120.00);
INSERT INTO turn_services (turn_id, service_id, price_at_time) VALUES (2, 3, 150.00);

-- 10. NOTAS CLÍNICAS
INSERT INTO clinical_entries (turn_id, diagnosis, treatment_notes, attachment_url) VALUES
(1, 'Gingivitis leve', 'Limpieza realizada', NULL);