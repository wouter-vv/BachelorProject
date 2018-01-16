-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema ErasmusProject
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ErasmusProject
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ErasmusProject` DEFAULT CHARACTER SET utf8 ;
USE `ErasmusProject` ;

-- -----------------------------------------------------
-- Table `ErasmusProject`.`Rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ErasmusProject`.`Rooms` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `nameRoom` VARCHAR(45) NULL,
  `width` INT NULL,
  `length` INT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ErasmusProject`.`Devices`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ErasmusProject`.`Devices` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `Rooms_id` INT(11) NOT NULL,
  `moment` DATETIME NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Devices_Rooms_idx` (`Rooms_id` ASC),
  CONSTRAINT `fk_Devices_Rooms`
    FOREIGN KEY (`Rooms_id`)
    REFERENCES `ErasmusProject`.`Rooms` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ErasmusProject`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ErasmusProject`.`Users` (
  `id` INT(11) NOT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL,
  `updated_at` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ErasmusProject`.`Values`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ErasmusProject`.`Values` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Rooms_id` INT(11) NOT NULL,
  `valueBeacon1` VARCHAR(45) NULL,
  `valueBeacon2` VARCHAR(45) NULL,
  `valueBeacon3` VARCHAR(45) NULL,
  `valueBeacon4` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Values_Rooms1_idx` (`Rooms_id` ASC),
  CONSTRAINT `fk_Values_Rooms1`
    FOREIGN KEY (`Rooms_id`)
    REFERENCES `ErasmusProject`.`Rooms` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ErasmusProject`.`Houses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ErasmusProject`.`Houses` (
  `Rooms_id` INT(11) NOT NULL,
  `Users_id` INT(11) NOT NULL,
  `city` VARCHAR(45) NULL,
  PRIMARY KEY (`Rooms_id`, `Users_id`),
  INDEX `fk_Rooms_has_Users_Users1_idx` (`Users_id` ASC),
  INDEX `fk_Rooms_has_Users_Rooms1_idx` (`Rooms_id` ASC),
  CONSTRAINT `fk_Rooms_has_Users_Rooms1`
    FOREIGN KEY (`Rooms_id`)
    REFERENCES `ErasmusProject`.`Rooms` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rooms_has_Users_Users1`
    FOREIGN KEY (`Users_id`)
    REFERENCES `ErasmusProject`.`Users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
