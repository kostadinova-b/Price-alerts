USE `price-alerts-browser`;

CREATE TABLE IF NOT EXISTS users (
    `id` INT PRIMARY KEY,
    `fcm_token` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS notifications (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `sub_id` BIGINT,
    `status` ENUM('SENT', 'DELIVERED', 'REJECTED', 'RETRY') DEFAULT 'SENT',
    `rate` INT DEFAULT 0
);