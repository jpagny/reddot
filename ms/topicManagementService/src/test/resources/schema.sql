CREATE TABLE IF NOT EXISTS topic
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    label       VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);
