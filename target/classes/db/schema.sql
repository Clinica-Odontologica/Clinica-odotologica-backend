-- -----------------------------------------------------
-- Schema clinica_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table roles
-- -----------------------------------------------------
CREATE TABLE roles (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- -----------------------------------------------------
-- Table users
-- -----------------------------------------------------
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(100),
  role_id INT NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE (username),
  FOREIGN KEY (role_id) REFERENCES roles(id) ON delete RESTRICT ON update CASCADE
);

-- -----------------------------------------------------
-- Table doctors
-- -----------------------------------------------------
CREATE TABLE doctors (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  specialty VARCHAR(50) NOT NULL,
  user_id BIGINT UNIQUE,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON delete RESTRICT ON update CASCADE
);

-- -----------------------------------------------------
-- Table patients
-- -----------------------------------------------------
CREATE TABLE patients (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dni VARCHAR(20) NOT NULL,
  name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE (dni)
);

-- -----------------------------------------------------
-- Table services
-- -----------------------------------------------------
CREATE TABLE services (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  base_price DECIMAL(10,2) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table turn_status
-- -----------------------------------------------------
CREATE TABLE turn_status (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (id),
  UNIQUE (name)
);

-- -----------------------------------------------------
-- Table turns
-- -----------------------------------------------------
CREATE TABLE turns (
  id BIGINT NOT NULL AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  doctor_id BIGINT NOT NULL,
  appointment_datetime DATETIME NOT NULL,
  status_id INT NOT NULL,
  created_by_user_id BIGINT NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (patient_id) REFERENCES patients(id) ON delete RESTRICT ON update CASCADE,
  FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON delete RESTRICT ON update CASCADE,
  FOREIGN KEY (status_id) REFERENCES turn_status(id) ON delete RESTRICT ON update CASCADE,
  FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON delete RESTRICT ON update CASCADE
);

-- -----------------------------------------------------
-- Table turn_services (N:M)
-- -----------------------------------------------------
CREATE TABLE turn_services (
  id BIGINT NOT NULL AUTO_INCREMENT,
  turn_id BIGINT NOT NULL,
  service_id BIGINT NOT NULL,
  price_at_time DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (turn_id) REFERENCES turns(id) ON delete RESTRICT ON update CASCADE,
  FOREIGN KEY (service_id) REFERENCES services(id) ON delete RESTRICT ON update CASCADE
);

-- -----------------------------------------------------
-- Table clinical_entries
-- -----------------------------------------------------
CREATE TABLE clinical_entries (
  id BIGINT NOT NULL AUTO_INCREMENT,
  turn_id BIGINT NOT NULL,
  diagnosis VARCHAR(255) NOT NULL,
  treatment_notes TEXT NOT NULL,
  attachment_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE (turn_id),
  FOREIGN KEY (turn_id) REFERENCES turns(id) ON delete RESTRICT ON update CASCADE
);

-- -----------------------------------------------------
-- Table medical_history_entries
-- -----------------------------------------------------
CREATE TABLE medical_history_entries (
  id BIGINT NOT NULL AUTO_INCREMENT,
  patient_id BIGINT NOT NULL,
  description TEXT NOT NULL,
  created_by_user_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (patient_id) REFERENCES patients(id) ON delete RESTRICT ON update CASCADE,
  FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON delete RESTRICT ON update CASCADE
);
