DROP TABLE `user_plant_parcels`;
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























