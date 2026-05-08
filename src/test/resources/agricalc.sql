create database `agricalc`;
USE `agricalc`;

CREATE TABLE `cities`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `code`          VARCHAR(25)  NOT NULL,
    `name`          VARCHAR(255) NOT NULL,
    `neighbors_ids` VARCHAR(45)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`name`, `code`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_groups`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL DEFAULT 0 COMMENT '-1:deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crops`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`         DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '-1:deleted, 0:passive, 1:active',
    `crop_group_id` BIGINT       NOT NULL,
    `name`          VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crops_crop_groups` FOREIGN KEY (`crop_group_id`) REFERENCES `crop_groups` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crops`
(
    `id`      BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT  NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_id` BIGINT   NOT NULL,
    `crop_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crops_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    CONSTRAINT `FK_city_crops_crops` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`id`),
    INDEX idx_city_crops (`status`, `city_id`, `crop_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `questions`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL DEFAULT 1 COMMENT '-1:deleted, 0:passive, 1:active',
    `type`   VARCHAR(25)  NOT NULL,
    `value`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_questions`
(
    `id`           BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT  NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT   NOT NULL,
    `question_id`  BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_questions_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    CONSTRAINT `FK_city_crop_questions_questions` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`),
    INDEX idx_city_crop_questions (`status`, `city_crop_id`, `question_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_question_answers`
(
    `id`                    BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_question_id` BIGINT       NOT NULL,
    `value`                 VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_question_answers_city_crop_questions` FOREIGN KEY (`city_crop_question_id`) REFERENCES `city_crop_questions` (`id`),
    INDEX idx_city_crop_question_answers (`city_crop_question_id`, `idate`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `coefficients`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`    DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`   TINYINT      NOT NULL DEFAULT 1 COMMENT '-1:deleted, 0:passive, 1:active',
    `type`     VARCHAR(25)  NOT NULL,
    `value`    VARCHAR(255) NOT NULL,
    `value_tr` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_coefficients (`status`, `type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_coefficients`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`          DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`         TINYINT  NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `crop_id`        BIGINT   NOT NULL,
    `coefficient_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_coefficients_crops` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`id`),
    CONSTRAINT `FK_crop_coefficients_coefficients` FOREIGN KEY (`coefficient_id`) REFERENCES `coefficients` (`id`),
    INDEX idx_crop_coefficients (`status`, `crop_id`, `coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_coefficient_answers`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `crop_coefficient_id` BIGINT       NOT NULL,
    `value`               VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_coefficient_answers_crop_coefficient` FOREIGN KEY (`crop_coefficient_id`) REFERENCES `crop_coefficients` (`id`),
    INDEX idx_crop_coefficient_answers (`crop_coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_diesel_distances`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT      NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_id` BIGINT       NOT NULL,
    `type`    VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_diesel_distances_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    INDEX idx_city_diesel_distances (`city_id`, `type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_diesel_distance_values`
(
    `id`                      BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_diesel_distance_id` BIGINT         NOT NULL,
    `value`                   DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_diesel_distance_values_city_diesel_distances` FOREIGN KEY (`city_diesel_distance_id`) REFERENCES `city_diesel_distances` (`id`),
    INDEX idx_city_diesel_distance_values (`city_diesel_distance_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `general_coefficients`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_general_coefficients (`status`, `name`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `general_coefficient_values`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `general_coefficient_id` BIGINT         NOT NULL,
    `value`                  DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_general_coefficient_values_city_diesel_distances` FOREIGN KEY (`general_coefficient_id`) REFERENCES `general_coefficients` (`id`),
    INDEX idx_city_diesel_distance_values (`idate`, `general_coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_seed_and_seedling_numbers`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT       NOT NULL,
    `type`         VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_numbers_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_seed_and_seedling_numbers (`status`, `city_crop_id`, `type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_seed_and_seedling_number_values`
(
    `id`                                    BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                                 DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_seed_and_seedling_number_id` BIGINT         NOT NULL,
    `value`                                 DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_number_values_1` FOREIGN KEY (`city_crop_seed_and_seedling_number_id`) REFERENCES `city_crop_seed_and_seedling_numbers` (`id`),
    INDEX idx_city_crop_seed_and_seedling_number_values (`idate`, `city_crop_seed_and_seedling_number_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_seed_and_seedling_prices`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT       NOT NULL,
    `type`         VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_prices_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_seed_and_seedling_prices (`status`, `city_crop_id`, `type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_seed_and_seedling_price_values`
(
    `id`                                   BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_seed_and_seedling_price_id` BIGINT         NOT NULL,
    `value`                                DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_price_values_1` FOREIGN KEY (`city_crop_seed_and_seedling_price_id`) REFERENCES `city_crop_seed_and_seedling_prices` (`id`),
    INDEX idx_city_crop_seed_and_seedling_price_values (`idate`, `city_crop_seed_and_seedling_price_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_field_harvest_average_values`
(
    `id`                   BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_id`         BIGINT         NOT NULL,
    `collected_crop_count` DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_field_harvest_average_values_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_field_harvest_average_values (`city_crop_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `fertilizers`
(
    `id`                BIGINT        NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME               DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT       NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `type`              VARCHAR(255)  NOT NULL,
    `name`              VARCHAR(255)  NOT NULL,
    `nitrogen_percent`  DECIMAL(6, 3) NOT NULL DEFAULT 0,
    `phosphor_percent`  DECIMAL(6, 3) NOT NULL DEFAULT 0,
    `potassium_percent` DECIMAL(6, 3) NOT NULL DEFAULT 0,
    `old_fertilizer_id` BIGINT                 DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`type`, `name`),
    INDEX idx_fertilizers (`status`, `type`, `name`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `fertilizer_price_values`
(
    `id`            BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `fertilizer_id` BIGINT         NOT NULL,
    `value`         decimal(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_fertilizer_price_values_fertilizers` FOREIGN KEY (`fertilizer_id`) REFERENCES `fertilizers` (`id`),
    INDEX idx_fertilizer_values (`fertilizer_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_fertilizers`
(
    `id`            BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`         DATETIME          DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`        TINYINT  NOT NULL DEFAULT 1 COMMENT '-1: deleted, 0:passive, 1:active',
    `city_id`       BIGINT   NOT NULL,
    `fertilizer_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_fertilizers_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    CONSTRAINT `FK_city_fertilizers_fertilizers` FOREIGN KEY (`fertilizer_id`) REFERENCES `fertilizers` (`id`),
    INDEX idx_city_fertilizers (`city_id`, `fertilizer_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_fertilizer_values`
(
    `id`                 BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`              DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_fertilizer_id` BIGINT         NOT NULL,
    `price`              DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_fertilizer_values_city_fertilizers` FOREIGN KEY (`city_fertilizer_id`) REFERENCES `city_fertilizers` (`id`),
    INDEX idx_city_fertilizer_values (`city_fertilizer_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `users`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT      NOT NULL DEFAULT 0 COMMENT '-1: deleted, 0:passive, 1:active',
    `name_surname` VARCHAR(255)          DEFAULT NULL,
    `tckn`         VARCHAR(255)          DEFAULT NULL,
    `email`        VARCHAR(255)          DEFAULT NULL,
    `phone`        VARCHAR(25)  NOT NULL,
    `password`     VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`email`, `phone`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_roles`
(
    `id`      BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` BIGINT   NOT NULL,
    `role`    TINYINT  NOT NULL DEFAULT 2 COMMENT '1:ADMIN, 2:USER',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_roles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_login_failures`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`      DATETIME             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`     TINYINT     NOT NULL DEFAULT 1 COMMENT '-1:deleted, 0:passive, 1:active',
    `user_id`    BIGINT      NOT NULL,
    `ip_address` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_login_failures_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_login_successes`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`    BIGINT      NOT NULL,
    `ip_address` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_login_successes_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_preferences`
(
    `id`              BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`         BIGINT      NOT NULL,
    `menu_mode`       VARCHAR(25) NOT NULL,
    `dark_mode`       VARCHAR(25) NOT NULL,
    `component_theme` VARCHAR(25) NOT NULL,
    `topbar_theme`    VARCHAR(25) NOT NULL,
    `menu_theme`      VARCHAR(25) NOT NULL,
    `input_style`     VARCHAR(25) NOT NULL,
    `light_logo`      TINYINT     NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_preferences_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;






