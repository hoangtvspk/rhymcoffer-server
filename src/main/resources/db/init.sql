-- Create database if not exists
CREATE DATABASE IF NOT EXISTS rhymcaffer;

-- Use the database
USE rhymcaffer;

-- Create user and grant privileges
CREATE USER IF NOT EXISTS 'rhymcaffer'@'localhost' IDENTIFIED BY 'rhymcaffer123';
GRANT ALL PRIVILEGES ON rhymcaffer.* TO 'rhymcaffer'@'localhost';
FLUSH PRIVILEGES;

-- Set character set and collation
ALTER DATABASE rhymcaffer CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; 