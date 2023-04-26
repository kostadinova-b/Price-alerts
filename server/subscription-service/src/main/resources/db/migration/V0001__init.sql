USE `price-alerts-subs`;

CREATE TABLE IF NOT EXISTS users (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `device_token` VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS stocks (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `buy` DOUBLE,
    `sell` DOUBLE
);

CREATE TABLE IF NOT EXISTS custom_subscriptions (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `stock_id` INT,
    `type` ENUM('BUY', 'SELL'),
    `on_price` DOUBLE,
    CONSTRAINT `user_custom` UNIQUE(user_id, stock_id, type),
    CONSTRAINT `fk_user_custom` FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT `fk_stock_custom` FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

CREATE TABLE IF NOT EXISTS threshold_subscriptions (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `user_id` INT,
    `stock_id` INT,
    `type` ENUM('BUY', 'SELL'),
    `price` DOUBLE,
    `threshold` INT,
    `rates_per_day` INT,
    CONSTRAINT `user_threshold` UNIQUE(user_id, stock_id, type),
    CONSTRAINT `fk_user_threshold` FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT `fk_stock_threshold` FOREIGN KEY (stock_id) REFERENCES stocks(id)
);