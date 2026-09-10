create database `agricalc2`;
USE `agricalc2`;

CREATE TABLE `cities`
(
    `id`            BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `code`          VARCHAR(25)    NOT NULL,
    `name`          VARCHAR(255)   NOT NULL,
    `neighbors_ids` VARCHAR(45)    NULL     DEFAULT NULL,
    `diesel_price`  DECIMAL(15, 3) NOT NULL DEFAULT 75,
    `fuel_price`    DECIMAL(15, 3) NOT NULL DEFAULT 80,
    `electricity`   DECIMAL(15, 3) NOT NULL DEFAULT 4,
    PRIMARY KEY (`id`),
    UNIQUE (`code`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `users`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT      NOT NULL COMMENT '-1: deleted, 0:passive, 1:active',
    `name`              VARCHAR(255) NOT NULL,
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

CREATE TABLE `user_informations`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`      BIGINT       NOT NULL,
    `tckn`         VARCHAR(255) NULL     DEFAULT NULL,
    `name_surname` VARCHAR(255) NULL     DEFAULT NULL,
    `email`        VARCHAR(255) NULL     DEFAULT NULL,
    `city_id`      BIGINT       NULL     DEFAULT NULL,
    `district`     VARCHAR(255) NULL     DEFAULT NULL,
    `village`      VARCHAR(255) NULL     DEFAULT NULL,
    `neighborhood` VARCHAR(255) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`tckn`),
    UNIQUE (`email`),
    CONSTRAINT `FK_user_informations_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_informations_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    INDEX idx_user_informations (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_assets`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT,
    `idate`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`     BIGINT      NOT NULL,
    `plant_asset` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_assets_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    INDEX idx_user_plant_assets (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_asset_details`
(
    `id`                  BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_plant_asset_id` BIGINT         NOT NULL,
    `asset_model`         VARCHAR(255)   NULL     DEFAULT NULL,
    `asset_price`         DECIMAL(15, 3) NULL     DEFAULT NULL,
    `tractor_brand`       VARCHAR(255)   NULL     DEFAULT NULL,
    `asset_type`          VARCHAR(255)   NULL     DEFAULT NULL,
    `spraying_tank`       DECIMAL(15, 3) NULL     DEFAULT NULL,
    `rent_income_price`   DECIMAL(15, 3) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_asset_details_user_plant_asset` FOREIGN KEY (`user_plant_asset_id`) REFERENCES `user_plant_assets` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `dairy_cows`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_dairy_cows (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_dairy_cow_barns`
(
    `id`                       BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`                    DATETIME       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`                   TINYINT        NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `user_id`                  BIGINT         NOT NULL,
    `dairy_cow_id`             BIGINT         NOT NULL,
    `barn_capacity`            INTEGER        NOT NULL,
    `milking_capacity`         INTEGER        NOT NULL,
    `barn_price`               DECIMAL(15, 3) NULL     DEFAULT NULL,
    `birth_rate`               DOUBLE         NULL     DEFAULT NULL,
    `death_rate`               DOUBLE         NULL     DEFAULT NULL,
    `insemination_rate`        DOUBLE         NULL     DEFAULT NULL,
    `milk_yield`               DOUBLE         NULL     DEFAULT NULL,
    `lactation_period`         INTEGER        NOT NULL,
    `total_count`              DOUBLE         NULL     DEFAULT NULL,
    `end_year_total_count`     DOUBLE         NULL     DEFAULT NULL,
    `average_feed_total_count` DOUBLE         NULL     DEFAULT NULL,
    `average_milking_count`    DOUBLE         NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dairy_cow_barns_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_dairy_cow_barns_dairy_cows` FOREIGN KEY (`dairy_cow_id`) REFERENCES `dairy_cows` (`id`),
    INDEX idx_user_dairy_cow_barns (`status`, `user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `dairy_cow_coefficients`
(
    `id`       BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `cow_type` TINYINT  NOT NULL,
    `value`    DOUBLE   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_dairy_cow_coefficients (`cow_type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_dairy_cow_counts`
(
    `id`                       BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`                    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_dairy_cow_barn_id`   BIGINT   NOT NULL,
    `dairy_cow_coefficient_id` BIGINT   NOT NULL,
    `current_count`            INTEGER  NULL     DEFAULT NULL,
    `purchase_count`           INTEGER  NULL     DEFAULT NULL,
    `sell_count`               INTEGER  NULL     DEFAULT NULL,
    `end_year_count`           DOUBLE   NULL     DEFAULT NULL,
    `average_feed_count`       DOUBLE   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dairy_cow_counts_user_dairy_cow_barns` FOREIGN KEY (`user_dairy_cow_barn_id`) REFERENCES `user_dairy_cow_barns` (`id`),
    CONSTRAINT `FK_user_dairy_cow_counts_dairy_cow_coefficients` FOREIGN KEY (`dairy_cow_coefficient_id`) REFERENCES `dairy_cow_coefficients` (`id`),
    INDEX idx_user_dairy_cow_counts (`user_dairy_cow_barn_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `feeds`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`     DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`    TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `category`  TINYINT      NOT NULL,
    `feed_type` TINYINT      NOT NULL,
    `name`      VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_feeds (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_dairy_cow_feeds`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_dairy_cow_barn_id` BIGINT         NOT NULL,
    `feed_id`                BIGINT         NOT NULL,
    `amount_kg`              DOUBLE         NOT NULL,
    `buying_date`            DATETIME       NOT NULL,
    `buying_price`           DECIMAL(15, 3) NOT NULL,
    `lactation`              DOUBLE         NULL     DEFAULT NULL,
    `roughage`               DOUBLE         NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dairy_cow_feeds_user_dairy_cow_barns` FOREIGN KEY (`user_dairy_cow_barn_id`) REFERENCES `user_dairy_cow_barns` (`id`),
    CONSTRAINT `FK_user_dairy_cow_feeds_feeds` FOREIGN KEY (`feed_id`) REFERENCES `feeds` (`id`),
    INDEX idx_user_dairy_cow_feeds (`user_dairy_cow_barn_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `dairy_cow_costs`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`     DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`    TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `cost_type` TINYINT      NOT NULL,
    `name`      VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_dairy_cow_costs (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_dairy_cow_costs`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_dairy_cow_barn_id` BIGINT         NOT NULL,
    `dairy_cow_cost_id`      BIGINT         NOT NULL,
    `count`                  DOUBLE         NULL     DEFAULT NULL,
    `total_cost`             DECIMAL(15, 3) NOT NULL,
    `hourly_interest`        DOUBLE         NULL     DEFAULT NULL,
    `cost_name`              VARCHAR(255)   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dairy_cow_costs_user_dairy_cow_barns` FOREIGN KEY (`user_dairy_cow_barn_id`) REFERENCES `user_dairy_cow_barns` (`id`),
    CONSTRAINT `FK_user_dairy_cow_costs_dairy_cow_costs` FOREIGN KEY (`dairy_cow_cost_id`) REFERENCES `dairy_cow_costs` (`id`),
    INDEX idx_user_dairy_cow_costs (`user_dairy_cow_barn_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `dairy_cow_incomes`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    `unit`   VARCHAR(25)  NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_dairy_cow_incomes (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_dairy_cow_incomes`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_dairy_cow_barn_id` BIGINT         NOT NULL,
    `dairy_cow_income_id`    BIGINT         NOT NULL,
    `income_value`           DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_dairy_cow_incomes_user_dairy_cow_barns` FOREIGN KEY (`user_dairy_cow_barn_id`) REFERENCES `user_dairy_cow_barns` (`id`),
    CONSTRAINT `FK_user_dairy_cow_incomes_dairy_cow_incomes` FOREIGN KEY (`dairy_cow_income_id`) REFERENCES `dairy_cow_incomes` (`id`),
    INDEX idx_user_dairy_cow_incomes (`user_dairy_cow_barn_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

##plantation

CREATE TABLE `plantation_products`
(
    `id`     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`  DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status` TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `name`   VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_plantation_products (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_product_options`
(
    `id`                    BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plantation_product_id` BIGINT       NOT NULL,
    `name`                  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_plantation_product_options_plantation_products` FOREIGN KEY (`plantation_product_id`) REFERENCES `plantation_products` (`id`),
    INDEX idx_plantation_product_options (`plantation_product_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_parcels`
(
    `id`              BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`           DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_id`         BIGINT         NOT NULL,
    `product_id`      BIGINT         NOT NULL,
    `parcel_type`     VARCHAR(255)   NULL     DEFAULT NULL,
    `parcel_name`     VARCHAR(255)   NOT NULL,
    `parcel_price`    DECIMAL(15, 3) NULL     DEFAULT NULL,
    `area_decare`     DOUBLE         NOT NULL,
    `rent_price`      DECIMAL(15, 3) NULL     DEFAULT NULL,
    `city_id`         BIGINT         NOT NULL,
    `district`        VARCHAR(255)   NULL     DEFAULT NULL,
    `village`         VARCHAR(255)   NULL     DEFAULT NULL,
    `neighborhood`    VARCHAR(255)   NULL     DEFAULT NULL,
    `ada_number`      INTEGER        NULL     DEFAULT NULL,
    `pafta_number`    INTEGER        NULL     DEFAULT NULL,
    `status_type`     VARCHAR(255)   NULL     DEFAULT NULL,
    `nadas`           TINYINT        NULL     DEFAULT NULL,
    `slope`           VARCHAR(255)   NULL     DEFAULT NULL,
    `orientation`     VARCHAR(255)   NULL     DEFAULT NULL,
    `soil_texture`    VARCHAR(255)   NULL     DEFAULT NULL,
    `soil_depth`      VARCHAR(255)   NULL     DEFAULT NULL,
    `organic_matter`  VARCHAR(255)   NULL     DEFAULT NULL,
    `soil_salinity`   VARCHAR(255)   NULL     DEFAULT NULL,
    `lime`            VARCHAR(255)   NULL     DEFAULT NULL,
    `phosphorus`      VARCHAR(255)   NULL     DEFAULT NULL,
    `potassium`       VARCHAR(255)   NULL     DEFAULT NULL,
    `watering_source` VARCHAR(255)   NULL     DEFAULT NULL,
    `watering_type`   VARCHAR(255)   NULL     DEFAULT NULL,
    `electric_source` VARCHAR(255)   NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`user_id`, `parcel_name`),
    CONSTRAINT `FK_user_plant_parcels_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_user_plant_parcels_plantation_products` FOREIGN KEY (`product_id`) REFERENCES `plantation_products` (`id`),
    CONSTRAINT `FK_user_plant_parcels_cities` FOREIGN KEY (`city_id`) REFERENCES `cities` (`id`),
    INDEX idx_user_plant_parcels (`user_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_questions`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`       DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`      TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `q_value`     VARCHAR(255) NOT NULL,
    `q_type`      TINYINT      NOT NULL,
    `a_type`      VARCHAR(255) NOT NULL,
    `r_type`      VARCHAR(45)  NOT NULL DEFAULT 'EVERY_TIME',
    `is_required` TINYINT      NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    INDEX idx_plantation_questions (`status`, `q_type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_question_options`
(
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plantation_question_id` BIGINT       NOT NULL,
    `value`                  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_plantation_question_options_plantation_questions` FOREIGN KEY (`plantation_question_id`) REFERENCES `plantation_questions` (`id`),
    INDEX idx_plantation_question_options (`plantation_question_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_product_questions`
(
    `id`                     BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`                  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plantation_product_id`  BIGINT         NOT NULL,
    `plantation_question_id` BIGINT         NOT NULL,
    `minimum_value`          DECIMAL(15, 3) NULL     DEFAULT NULL,
    `maximum_value`          DECIMAL(15, 3) NULL     DEFAULT NULL,
    `unknown_value`          DECIMAL(15, 3) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_plantation_product_questions_plantation_products` FOREIGN KEY (`plantation_product_id`) REFERENCES `plantation_products` (`id`),
    CONSTRAINT `FK_plantation_product_questions_plantation_questions` FOREIGN KEY (`plantation_question_id`) REFERENCES `plantation_questions` (`id`),
    INDEX idx_plantation_product_questions (`plantation_product_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_parcel_answers`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_parcel_id`      BIGINT       NOT NULL,
    `product_question_id` BIGINT       NOT NULL,
    `answer_value`        VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_parcel_answers_user_plant_parcels` FOREIGN KEY (`user_parcel_id`) REFERENCES `user_plant_parcels` (`id`),
    CONSTRAINT `FK_user_plant_parcel_answers_plantation_product_questions` FOREIGN KEY (`product_question_id`) REFERENCES `plantation_product_questions` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_coefficients`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `coef_type`    VARCHAR(255) NOT NULL,
    `diesel_value` DOUBLE       NULL     DEFAULT NULL,
    `labor_value`  DOUBLE       NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`coef_type`),
    INDEX idx_plantation_coefficients (`coef_type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_irrigation_values`
(
    `id`           BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `price_type`   VARCHAR(255)   NOT NULL,
    `price_value`  DECIMAL(15, 3) NOT NULL,
    `labor_value`  DOUBLE         NOT NULL,
    `diesel_value` DOUBLE         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`price_type`),
    INDEX idx_plantation_irrigation_values (`price_type`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_parcel_plans`
(
    `id`                  BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`               DATETIME       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`              TINYINT        NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `parcel_id`           BIGINT         NOT NULL,
    `start_date`          DATE           NULL     DEFAULT NULL,
    `gross_income`        DECIMAL(15, 3) NULL     DEFAULT NULL,
    `total_expense`       DECIMAL(15, 3) NULL     DEFAULT NULL,
    `soil_prep_cost`      DECIMAL(15, 3) NULL     DEFAULT NULL,
    `planting_cost`       DECIMAL(15, 3) NULL     DEFAULT NULL,
    `fertilizer_cost`     DECIMAL(15, 3) NULL     DEFAULT NULL,
    `weed_control_cost`   DECIMAL(15, 3) NULL     DEFAULT NULL,
    `irrigation_cost`     DECIMAL(15, 3) NULL     DEFAULT NULL,
    `cultural_cost`       DECIMAL(15, 3) NULL     DEFAULT NULL,
    `protection_cost`     DECIMAL(15, 3) NULL     DEFAULT NULL,
    `harvest_cost`        DECIMAL(15, 3) NULL     DEFAULT NULL,
    `blend_cost`          DECIMAL(15, 3) NULL     DEFAULT NULL,
    `drying_cost`         DECIMAL(15, 3) NULL     DEFAULT NULL,
    `baling_cost`         DECIMAL(15, 3) NULL     DEFAULT NULL,
    `transportation_cost` DECIMAL(15, 3) NULL     DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_parcel_plans_user_plant_parcels` FOREIGN KEY (`parcel_id`) REFERENCES `user_plant_parcels` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_parcel_plan_answers`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plant_plan_id`       BIGINT       NOT NULL,
    `product_question_id` BIGINT       NOT NULL,
    `answer_value`        VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_parcel_plan_answers_plans` FOREIGN KEY (`plant_plan_id`) REFERENCES `user_plant_parcel_plans` (`id`),
    CONSTRAINT `FK_user_plant_parcel_plan_answers_questions` FOREIGN KEY (`product_question_id`) REFERENCES `plantation_product_questions` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_plant_parcel_plan_allocations`
(
    `id`               BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`            DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `plant_plan_id`    BIGINT         NOT NULL,
    `q_type`           TINYINT        NOT NULL,
    `a_type`           VARCHAR(255)   NOT NULL,
    `calculated_value` DECIMAL(15, 3) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_plant_parcel_plan_allocations_user_plant_parcel_plans` FOREIGN KEY (`plant_plan_id`) REFERENCES `user_plant_parcel_plans` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `plantation_diseases`
(
    `id`           BIGINT                                                                                        NOT NULL AUTO_INCREMENT,
    `idate`        DATETIME                                                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`        DATETIME                                                                                      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`       TINYINT                                                                                       NOT NULL DEFAULT 1 COMMENT '-1:deleted, 0:passive, 1:active',
    `disease_type` ENUM ('ACARICIDE', 'FUNGICIDE', 'HERBICIDE', 'HORMONE', 'INSECTICIDE', 'PESTICIDE', 'OTHERS') NOT NULL DEFAULT 'OTHERS',
    `disease_name` VARCHAR(255)                                                                                  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY UK_diseases (`disease_type`, `disease_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `plantation_product_question_diseases`
(
    `id`                  BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`               DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `product_question_id` BIGINT         NOT NULL,
    `disease_id`          BIGINT         NOT NULL,
    `usage_coefficient`   DOUBLE         NOT NULL DEFAULT 0.0 COMMENT 'İlaç uygulama katsayısı',
    `adhesive_price`      DECIMAL(15, 3) NOT NULL DEFAULT 0.000 COMMENT 'YAPIŞTIRICI (TL/Da)',
    `water_amount`        DOUBLE         NOT NULL DEFAULT 25.0 COMMENT 'Su miktarı katsayısı',
    PRIMARY KEY (`id`),
    UNIQUE KEY UK_product_question_diseases (`product_question_id`, `disease_id`),
    CONSTRAINT `FK_product_question_diseases_product_question` FOREIGN KEY (`product_question_id`) REFERENCES `plantation_product_questions` (`id`),
    CONSTRAINT `FK_product_question_diseases_disease` FOREIGN KEY (`disease_id`) REFERENCES `plantation_diseases` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `plantation_medicines`
(
    `id`                BIGINT         NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME       NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT        NOT NULL DEFAULT 1 COMMENT '-1:deleted, 0:passive, 1:active',
    `disease_type_set`  JSON           NOT NULL,
    `medicine_brand`    VARCHAR(50)    NOT NULL,
    `commercial_name`   VARCHAR(255)   NOT NULL,
    `active_ingredient` VARCHAR(255)   NOT NULL,
    `medicine_price`    DECIMAL(15, 3) NOT NULL DEFAULT 0.000 COMMENT 'İlaç Fiyat (TL/Ad)',
    `unit_packaging`    DOUBLE         NOT NULL DEFAULT 0 COMMENT 'AMBALAJ BİRİMİ (Gr,ml)',
    PRIMARY KEY (`id`),
    UNIQUE KEY UK_medicines (`medicine_brand`, `commercial_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `plantation_product_question_disease_medicines`
(
    `id`                          BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `product_question_disease_id` BIGINT   NOT NULL,
    `medicine_id`                 BIGINT   NOT NULL,
    `medicine_dosage`             DOUBLE   NOT NULL DEFAULT 0 COMMENT 'DOZAJ (Gr/Da,ml/Da)',
    PRIMARY KEY (`id`),
    UNIQUE KEY UK_product_question_disease_medicines (`product_question_disease_id`, `medicine_id`),
    CONSTRAINT `FK_product_question_disease_medicines_product_question_disease` FOREIGN KEY (`product_question_disease_id`) REFERENCES `plantation_product_question_diseases` (`id`),
    CONSTRAINT `FK_product_question_disease_medicines_medicines` FOREIGN KEY (`medicine_id`) REFERENCES `plantation_medicines` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `literacy_questions`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `q_value` VARCHAR(255) NOT NULL,
    `q_type`  TINYINT      NOT NULL,
    PRIMARY KEY (`id`),
    INDEX idx_literacy_questions (`status`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `literacy_question_options`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `idate`                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`   DATETIME     NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`  TINYINT      NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `literacy_question_id` BIGINT       NOT NULL,
    `option_value`         VARCHAR(255) NOT NULL,
    `score_10_value`       DOUBLE       NOT NULL,
    `score_11_value`       DOUBLE       NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_literacy_question_options_literacy_questions` FOREIGN KEY (`literacy_question_id`) REFERENCES `literacy_questions` (`id`),
    INDEX idx_literacy_question_options (`literacy_question_id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_literacy_assessments`
(
    `id`                BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `udate`             DATETIME NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `status`            TINYINT  NOT NULL COMMENT '-1:deleted, 0:passive, 1:active',
    `user_id`           BIGINT   NOT NULL,
    `total_score`       DOUBLE,
    `budget_score`      DOUBLE,
    `debt_score`        DOUBLE,
    `operational_score` DOUBLE,
    `saving_score`      DOUBLE,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_literacy_assessments_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;

CREATE TABLE `user_literacy_answers`
(
    `id`                          BIGINT   NOT NULL AUTO_INCREMENT,
    `idate`                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `user_literacy_assessment_id` BIGINT   NOT NULL,
    `literacy_question_id`        BIGINT   NOT NULL,
    `literacy_question_option_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_user_literacy_answers_user_literacy_assessments` FOREIGN KEY (`user_literacy_assessment_id`) REFERENCES `user_literacy_assessments` (`id`),
    CONSTRAINT `FK_user_literacy_answers_literacy_questions` FOREIGN KEY (`literacy_question_id`) REFERENCES `literacy_questions` (`id`),
    CONSTRAINT `FK_user_literacy_answers_literacy_question_options` FOREIGN KEY (`literacy_question_option_id`) REFERENCES `literacy_question_options` (`id`)
)
    ENGINE = InnoDB
    AUTO_INCREMENT = 1;
