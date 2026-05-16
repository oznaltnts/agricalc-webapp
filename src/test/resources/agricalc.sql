create database `tektarim`;
USE `tektarim`;

CREATE TABLE `cities`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `code`          VARCHAR(25)  NOT NULL,
    `name`          VARCHAR(255) NOT NULL,
    `neighbors_ids` VARCHAR(45)  NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`code`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crops`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`      DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`     TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `group_name` VARCHAR(255) NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`group_name`, `name`),
    INDEX idx_crops (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crops`
(
    `id`      BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT  NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
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
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `type`   VARCHAR(25)  NOT NULL,
    `value`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_questions (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_questions`
(
    `id`           BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT  NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT   NOT NULL,
    `question_id`  BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_questions_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    CONSTRAINT `FK_city_crop_questions_questions` FOREIGN KEY (`question_id`) REFERENCES `questions` (`id`),
    INDEX idx_city_crop_questions (`status`, `city_crop_id`, `question_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_question_values`
(
    `id`                    BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_question_id` BIGINT       NOT NULL,
    `value`                 VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_question_values_city_crop_questions` FOREIGN KEY (`city_crop_question_id`) REFERENCES `city_crop_questions` (`id`),
    INDEX idx_city_crop_question_values (`idate`, `city_crop_question_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `coefficients`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `type`   VARCHAR(25)  NOT NULL,
    `value`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_coefficients (`status`, `type`, `value`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_coefficients`
(
    `id`             BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`          DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`         TINYINT  NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `crop_id`        BIGINT   NOT NULL,
    `coefficient_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_coefficients_crops` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`id`),
    CONSTRAINT `FK_crop_coefficients_coefficients` FOREIGN KEY (`coefficient_id`) REFERENCES `coefficients` (`id`),
    INDEX idx_crop_coefficients (`status`, `crop_id`, `coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_coefficient_values`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `crop_coefficient_id` BIGINT       NOT NULL,
    `value`               VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_coefficient_values_crop_coefficient` FOREIGN KEY (`crop_coefficient_id`) REFERENCES `crop_coefficients` (`id`),
    INDEX idx_crop_coefficient_values (`idate`, `crop_coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_diesel_distances`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `city_id` BIGINT       NOT NULL,
    `type`    VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_diesel_distances_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    INDEX idx_city_diesel_distances (`status`, `city_id`)
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
    INDEX idx_city_diesel_distance_values (`idate`, `city_diesel_distance_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `general_coefficients`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_general_coefficients (`status`)
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
    CONSTRAINT `FK_general_coefficient_values_general_coefficients` FOREIGN KEY (`general_coefficient_id`) REFERENCES `general_coefficients` (`id`),
    INDEX idx_city_diesel_distance_values (`idate`, `general_coefficient_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_seed_and_seedling_numbers`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT       NOT NULL,
    `type`         VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_numbers_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_seed_and_seedling_numbers (`status`, `city_crop_id`)
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
    `udate`        DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `city_crop_id` BIGINT       NOT NULL,
    `type`         VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_seed_and_seedling_prices_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_seed_and_seedling_prices (`status`, `city_crop_id`)
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
    INDEX idx_city_crop_field_harvest_average_values (`idate`, `city_crop_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_crop_watering_values`
(
    `id`           BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `city_crop_id` BIGINT         NOT NULL,
    `maintenance`  DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_crop_watering_values_city_crops` FOREIGN KEY (`city_crop_id`) REFERENCES `city_crops` (`id`),
    INDEX idx_city_crop_watering_values (`idate`, `city_crop_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `fertilizers`
(
    `id`                BIGINT        NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT       NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `type`              VARCHAR(255)  NOT NULL,
    `name`              VARCHAR(255)  NOT NULL,
    `nitrogen_percent`  DECIMAL(6, 3) NULL     DEFAULT NULL,
    `phosphor_percent`  DECIMAL(6, 3) NULL     DEFAULT NULL,
    `potassium_percent` DECIMAL(6, 3) NULL     DEFAULT NULL,
    `old_fertilizer_id` BIGINT        NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`type`, `name`),
    INDEX idx_fertilizers (`status`, `type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `city_fertilizers`
(
    `id`            BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`         DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`        TINYINT  NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `city_id`       BIGINT   NOT NULL,
    `fertilizer_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_city_fertilizers_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    CONSTRAINT `FK_city_fertilizers_fertilizers` FOREIGN KEY (`fertilizer_id`) REFERENCES `fertilizers` (`id`),
    INDEX idx_city_fertilizers (`status`, `city_id`, `fertilizer_id`)
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
    INDEX idx_city_fertilizer_values (`idate`, `city_fertilizer_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `medicine_groups`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`         DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`        TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `type`          VARCHAR(255) NOT NULL,
    `group_disease` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `status_type_group_disease` (`type`, `group_disease`),
    INDEX idx_medicine_groups (`status`, `type`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1;

CREATE TABLE `crop_medicine_groups`
(
    `id`                BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT  NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `crop_id`           BIGINT   NOT NULL,
    `medicine_group_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_medicine_groups_crops` FOREIGN KEY (`crop_id`) REFERENCES `crops` (`id`),
    CONSTRAINT `FK_crop_medicine_groups_medicine_groups` FOREIGN KEY (`medicine_group_id`) REFERENCES `medicine_groups` (`id`),
    INDEX idx_crop_medicine_groups (`status`, `crop_id`, `medicine_group_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_medicine_group_values`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `crop_medicine_group_id` BIGINT         NOT NULL,
    `coefficient`            DECIMAL(15, 3) NOT NULL,
    `price`                  DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_medicine_group_values_crop_medicine_groups` FOREIGN KEY (`crop_medicine_group_id`) REFERENCES `crop_medicine_groups` (`id`),
    INDEX idx_crop_medicine_group_values (`idate`, `crop_medicine_group_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `medicines`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`      DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`     TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `brand`      VARCHAR(255) NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    `ingredient` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_medicines (`status`, `brand`)
)
    ENGINE = INNODB
    AUTO_INCREMENT = 1;

CREATE TABLE `medicine_values`
(
    `id`             BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`          DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `medicine_id`    BIGINT         NOT NULL,
    `packaging_size` DECIMAL(15, 3) NOT NULL,
    `price`          DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_medicine_values_medicines` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
    INDEX idx_medicine_values (`idate`, `medicine_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_medicine_group_medicines`
(
    `id`                     BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`                  DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`                 TINYINT  NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `crop_medicine_group_id` BIGINT   NOT NULL,
    `medicine_id`            BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_medicine_group_medicines_crop_medicine_groups` FOREIGN KEY (`crop_medicine_group_id`) REFERENCES `crop_medicine_groups` (`id`),
    CONSTRAINT `FK_crop_medicine_group_medicines_medicines` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`),
    INDEX idx_crop_medicine_group_medicines (`status`, `crop_medicine_group_id`, `medicine_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `crop_medicine_group_medicine_values`
(
    `id`                              BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `crop_medicine_group_medicine_id` BIGINT         NOT NULL,
    `amount`                          DECIMAL(15, 3) NOT NULL,
    `water_amount`                    DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_crop_medicine_group_medicine_values_1` FOREIGN KEY (`crop_medicine_group_medicine_id`) REFERENCES `crop_medicine_group_medicines` (`id`),
    INDEX idx_crop_medicine_group_medicine_values (`idate`, `crop_medicine_group_medicine_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `users`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `phone`             VARCHAR(25)  NOT NULL,
    `password`          VARCHAR(255) NOT NULL,
    `last_login`        DATETIME     NULL     DEFAULT NULL,
    `before_last_login` DATETIME     NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`phone`),
    INDEX idx_users (`status`, `phone`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_roles`
(
    `id`      BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id` BIGINT   NOT NULL,
    `role`    TINYINT  NOT NULL COMMENT '1:ADMIN, 2:USER',
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_roles_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    INDEX idx_user_roles (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_login_failures`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`      DATETIME    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`     TINYINT     NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `user_id`    BIGINT      NOT NULL,
    `ip_address` VARCHAR(25) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_login_failures_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    INDEX idx_user_login_failures (`status`, `user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_login_successes`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`    BIGINT      NOT NULL,
    `ip_address` VARCHAR(25) NULL     DEFAULT NULL,
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
    `light_logo`      TINYINT     NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_preferences_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    INDEX idx_user_preferences (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_informations`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`      BIGINT       NOT NULL,
    `city_id`      BIGINT       NULL     DEFAULT NULL,
    `tckn`         VARCHAR(255) NULL     DEFAULT NULL,
    `name_surname` VARCHAR(255) NULL     DEFAULT NULL,
    `email`        VARCHAR(255) NULL     DEFAULT NULL,
    `district`     VARCHAR(255) NULL     DEFAULT NULL,
    `village`      VARCHAR(255) NULL     DEFAULT NULL,
    `neighborhood` VARCHAR(255) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_informations_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_informations_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    INDEX idx_user_informations (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;





