CREATE DATABASE IF NOT EXISTS agency;
USE agency;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id CHAR(36) PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       phone VARCHAR(50),
                       is_verified BOOLEAN DEFAULT FALSE,
                       verification_token VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);