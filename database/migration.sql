-- -----------------------------------------------------
-- Base de Datos: ratemyprofs_db
-- -----------------------------------------------------
DROP DATABASE IF EXISTS `ratemyprofs_db`;
CREATE DATABASE IF NOT EXISTS `ratemyprofs_db`
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE `ratemyprofs_db`;

-- -----------------------------------------------------
-- Tabla: Roles (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Roles` (
  `RoleID` INT NOT NULL AUTO_INCREMENT,
  `RoleName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`RoleID`),
  UNIQUE INDEX `RoleName_UNIQUE` (`RoleName` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: ReviewStatuses (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ReviewStatuses` (
  `StatusID` INT NOT NULL AUTO_INCREMENT,
  `StatusName` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`StatusID`),
  UNIQUE INDEX `StatusName_UNIQUE` (`StatusName` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: VoteTypes (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `VoteTypes` (
  `VoteTypeID` INT NOT NULL AUTO_INCREMENT,
  `VoteName` VARCHAR(30) NOT NULL COMMENT 'Ej. "Like", "Dislike", "Helpful"',
  `VoteValue` INT NOT NULL DEFAULT 0 COMMENT 'Valor para agregación (ej. 1, -1, 2)',
  PRIMARY KEY (`VoteTypeID`),
  UNIQUE INDEX `VoteName_UNIQUE` (`VoteName` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: Users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Users` (
  `UserID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `PasswordHash` VARCHAR(255) NOT NULL,
  `RoleID` INT NOT NULL,
  `CreatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`),
  UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
  INDEX `fk_Users_Roles_idx` (`RoleID` ASC) VISIBLE,
  CONSTRAINT `fk_Users_Roles`
    FOREIGN KEY (`RoleID`)
    REFERENCES `Roles` (`RoleID`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: Universities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Universities` (
  `UniversityID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`UniversityID`),
  UNIQUE INDEX `Name_UNIQUE` (`Name` ASC) VISIBLE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: Subjects (Materias)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Subjects` (
  `SubjectID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  `DepartmentName` VARCHAR(255) NULL,
  PRIMARY KEY (`SubjectID`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: Professors
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Professors` (
  `ProfessorID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(255) NOT NULL,
  `Title` VARCHAR(50) NULL COMMENT 'Ej. "Dr.", "Ing.", "M.C."',
  `DepartmentName` VARCHAR(255) NULL,
  `PhotoURL` VARCHAR(2083) NULL,
  PRIMARY KEY (`ProfessorID`)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: ProfessorUniversities (Unión M:N)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProfessorUniversities` (
  `ProfessorID` INT NOT NULL,
  `UniversityID` INT NOT NULL,
  PRIMARY KEY (`ProfessorID`, `UniversityID`),
  INDEX `fk_ProfUniv_Universities_idx` (`UniversityID` ASC) VISIBLE,
  CONSTRAINT `fk_ProfUniv_Professors`
    FOREIGN KEY (`ProfessorID`)
    REFERENCES `Professors` (`ProfessorID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ProfUniv_Universities`
    FOREIGN KEY (`UniversityID`)
    REFERENCES `Universities` (`UniversityID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: ProfessorSubjects (Unión M:N)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ProfessorSubjects` (
  `ProfessorID` INT NOT NULL,
  `SubjectID` INT NOT NULL,
  PRIMARY KEY (`ProfessorID`, `SubjectID`),
  INDEX `fk_ProfSubj_Subjects_idx` (`SubjectID` ASC) VISIBLE,
  CONSTRAINT `fk_ProfSubj_Professors`
    FOREIGN KEY (`ProfessorID`)
    REFERENCES `Professors` (`ProfessorID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ProfSubj_Subjects`
    FOREIGN KEY (`SubjectID`)
    REFERENCES `Subjects` (`SubjectID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: Reviews
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Reviews` (
  `ReviewID` INT NOT NULL AUTO_INCREMENT,
  `UserID` INT NULL COMMENT 'Permitir NULL si el usuario es eliminado',
  `ProfessorID` INT NOT NULL,
  `SubjectID` INT NOT NULL,
  `Rating` TINYINT UNSIGNED NOT NULL COMMENT 'Calificación 1-5',
  `Comment` VARCHAR(1000) NOT NULL,
  `IsAnonymous` BOOLEAN NOT NULL DEFAULT 0,
  `StatusID` INT NOT NULL DEFAULT 1 COMMENT 'FK a ReviewStatuses. Default 1 = "Pending"',
  `CreatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ReviewID`),
  INDEX `fk_Reviews_Users_idx` (`UserID` ASC) VISIBLE,
  INDEX `fk_Reviews_Professors_idx` (`ProfessorID` ASC) VISIBLE,
  INDEX `fk_Reviews_Subjects_idx` (`SubjectID` ASC) VISIBLE,
  INDEX `fk_Reviews_Status_idx` (`StatusID` ASC) VISIBLE,
  CONSTRAINT `fk_Reviews_Users`
    FOREIGN KEY (`UserID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Reviews_Professors`
    FOREIGN KEY (`ProfessorID`)
    REFERENCES `Professors` (`ProfessorID`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Reviews_Subjects`
    FOREIGN KEY (`SubjectID`)
    REFERENCES `Subjects` (`SubjectID`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Reviews_Status`
    FOREIGN KEY (`StatusID`)
    REFERENCES `ReviewStatuses` (`StatusID`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: ReviewImages
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ReviewImages` (
  `ReviewImageID` INT NOT NULL AUTO_INCREMENT,
  `ReviewID` INT NOT NULL,
  `ImageURL` VARCHAR(2083) NOT NULL,
  PRIMARY KEY (`ReviewImageID`),
  INDEX `fk_ReviewImages_Reviews_idx` (`ReviewID` ASC) VISIBLE,
  CONSTRAINT `fk_ReviewImages_Reviews`
    FOREIGN KEY (`ReviewID`)
    REFERENCES `Reviews` (`ReviewID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Tabla: ReviewVotes
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ReviewVotes` (
  `VoteID` INT NOT NULL AUTO_INCREMENT,
  `UserID` INT NOT NULL,
  `ReviewID` INT NOT NULL,
  `VoteTypeID` INT NOT NULL COMMENT 'FK a VoteTypes (ej. 1 = "Like")',
  `CreatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`VoteID`),
  INDEX `fk_ReviewVotes_Users_idx` (`UserID` ASC) VISIBLE,
  INDEX `fk_ReviewVotes_Reviews_idx` (`ReviewID` ASC) VISIBLE,
  INDEX `fk_ReviewVotes_VoteTypes_idx` (`VoteTypeID` ASC) VISIBLE,
  UNIQUE INDEX `UQ_User_Review_Vote` (`UserID` ASC, `ReviewID` ASC, `VoteTypeID` ASC) VISIBLE COMMENT 'Evita duplicados del mismo tipo de voto',
  CONSTRAINT `fk_ReviewVotes_Users`
    FOREIGN KEY (`UserID`)
    REFERENCES `Users` (`UserID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ReviewVotes_Reviews`
    FOREIGN KEY (`ReviewID`)
    REFERENCES `Reviews` (`ReviewID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ReviewVotes_VoteTypes`
    FOREIGN KEY (`VoteTypeID`)
    REFERENCES `VoteTypes` (`VoteTypeID`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- DATOS INICIALES
-- Estos valores ahora definen la lógica de negocio
-- -----------------------------------------------------
INSERT INTO `Roles` (`RoleID`, `RoleName`) VALUES (1, 'Admin'), (2, 'Estudiante');

INSERT INTO `ReviewStatuses` (`StatusID`, `StatusName`) VALUES (1, 'Pending'), (2, 'Approved'), (3, 'Rejected');

INSERT INTO `VoteTypes` (`VoteTypeID`, `VoteName`, `VoteValue`) VALUES (1, 'Like', 1), (2, 'Dislike', -1);s