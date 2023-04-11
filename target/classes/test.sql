CREATE TABLE IF NOT EXISTS USERS (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(45) NOT NULL CHECK ( name ! ),
    `last_name` varchar(45) NOT NULL,
    `age` int DEFAULT NULL, PRIMARY KEY (`id`));