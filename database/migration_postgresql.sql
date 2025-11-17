-- -----------------------------------------------------
-- Base de Datos: ratemyprofs_db
-- PostgreSQL Version
-- -----------------------------------------------------
DROP DATABASE IF EXISTS ratemyprofs_db;
CREATE DATABASE ratemyprofs_db
    WITH 
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0;

\c ratemyprofs_db;

-- -----------------------------------------------------
-- Tabla: Roles (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Roles (
  RoleID SERIAL NOT NULL,
  RoleName VARCHAR(50) NOT NULL,
  PRIMARY KEY (RoleID),
  UNIQUE (RoleName)
);


-- -----------------------------------------------------
-- Tabla: ReviewStatuses (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ReviewStatuses (
  StatusID SERIAL NOT NULL,
  StatusName VARCHAR(50) NOT NULL,
  PRIMARY KEY (StatusID),
  UNIQUE (StatusName)
);


-- -----------------------------------------------------
-- Tabla: VoteTypes (TABLA DE BÚSQUEDA)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS VoteTypes (
  VoteTypeID SERIAL NOT NULL,
  VoteName VARCHAR(30) NOT NULL,
  VoteValue INT NOT NULL DEFAULT 0,
  PRIMARY KEY (VoteTypeID),
  UNIQUE (VoteName)
);

COMMENT ON COLUMN VoteTypes.VoteName IS 'Ej. "Like", "Dislike", "Helpful"';
COMMENT ON COLUMN VoteTypes.VoteValue IS 'Valor para agregación (ej. 1, -1, 2)';


-- -----------------------------------------------------
-- Tabla: Users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Users (
  UserID SERIAL NOT NULL,
  Name VARCHAR(255) NOT NULL,
  Email VARCHAR(255) NOT NULL,
  PasswordHash VARCHAR(255) NOT NULL,
  RoleID INT NOT NULL,
  CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (UserID),
  UNIQUE (Email),
  CONSTRAINT fk_Users_Roles
    FOREIGN KEY (RoleID)
    REFERENCES Roles (RoleID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

CREATE INDEX idx_Users_RoleID ON Users(RoleID);


-- -----------------------------------------------------
-- Tabla: Universities
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Universities (
  UniversityID SERIAL NOT NULL,
  Name VARCHAR(255) NOT NULL,
  PRIMARY KEY (UniversityID),
  UNIQUE (Name)
);


-- -----------------------------------------------------
-- Tabla: Subjects (Materias)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Subjects (
  SubjectID SERIAL NOT NULL,
  Name VARCHAR(255) NOT NULL,
  DepartmentName VARCHAR(255) NULL,
  PRIMARY KEY (SubjectID)
);


-- -----------------------------------------------------
-- Tabla: Professors
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Professors (
  ProfessorID SERIAL NOT NULL,
  Name VARCHAR(255) NOT NULL,
  Title VARCHAR(50) NULL,
  DepartmentName VARCHAR(255) NULL,
  PhotoURL VARCHAR(2083) NULL,
  PRIMARY KEY (ProfessorID)
);

COMMENT ON COLUMN Professors.Title IS 'Ej. "Dr.", "Ing.", "M.C."';


-- -----------------------------------------------------
-- Tabla: ProfessorUniversities (Unión M:N)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ProfessorUniversities (
  ProfessorID INT NOT NULL,
  UniversityID INT NOT NULL,
  PRIMARY KEY (ProfessorID, UniversityID),
  CONSTRAINT fk_ProfUniv_Professors
    FOREIGN KEY (ProfessorID)
    REFERENCES Professors (ProfessorID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_ProfUniv_Universities
    FOREIGN KEY (UniversityID)
    REFERENCES Universities (UniversityID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX idx_ProfUniv_UniversityID ON ProfessorUniversities(UniversityID);


-- -----------------------------------------------------
-- Tabla: ProfessorSubjects (Unión M:N)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ProfessorSubjects (
  ProfessorID INT NOT NULL,
  SubjectID INT NOT NULL,
  PRIMARY KEY (ProfessorID, SubjectID),
  CONSTRAINT fk_ProfSubj_Professors
    FOREIGN KEY (ProfessorID)
    REFERENCES Professors (ProfessorID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_ProfSubj_Subjects
    FOREIGN KEY (SubjectID)
    REFERENCES Subjects (SubjectID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX idx_ProfSubj_SubjectID ON ProfessorSubjects(SubjectID);


-- -----------------------------------------------------
-- Tabla: Reviews
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Reviews (
  ReviewID SERIAL NOT NULL,
  UserID INT NULL,
  ProfessorID INT NOT NULL,
  SubjectID INT NOT NULL,
  Rating SMALLINT NOT NULL,
  Comment VARCHAR(1000) NOT NULL,
  IsAnonymous BOOLEAN NOT NULL DEFAULT false,
  StatusID INT NOT NULL DEFAULT 1,
  CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (ReviewID),
  CONSTRAINT fk_Reviews_Users
    FOREIGN KEY (UserID)
    REFERENCES Users (UserID)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT fk_Reviews_Professors
    FOREIGN KEY (ProfessorID)
    REFERENCES Professors (ProfessorID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_Reviews_Subjects
    FOREIGN KEY (SubjectID)
    REFERENCES Subjects (SubjectID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_Reviews_Status
    FOREIGN KEY (StatusID)
    REFERENCES ReviewStatuses (StatusID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT chk_Rating CHECK (Rating >= 1 AND Rating <= 5)
);

COMMENT ON COLUMN Reviews.UserID IS 'Permitir NULL si el usuario es eliminado';
COMMENT ON COLUMN Reviews.Rating IS 'Calificación 1-5';
COMMENT ON COLUMN Reviews.StatusID IS 'FK a ReviewStatuses. Default 1 = "Pending"';

CREATE INDEX idx_Reviews_UserID ON Reviews(UserID);
CREATE INDEX idx_Reviews_ProfessorID ON Reviews(ProfessorID);
CREATE INDEX idx_Reviews_SubjectID ON Reviews(SubjectID);
CREATE INDEX idx_Reviews_StatusID ON Reviews(StatusID);


-- -----------------------------------------------------
-- Tabla: ReviewImages
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ReviewImages (
  ReviewImageID SERIAL NOT NULL,
  ReviewID INT NOT NULL,
  ImageURL VARCHAR(2083) NOT NULL,
  PRIMARY KEY (ReviewImageID),
  CONSTRAINT fk_ReviewImages_Reviews
    FOREIGN KEY (ReviewID)
    REFERENCES Reviews (ReviewID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX idx_ReviewImages_ReviewID ON ReviewImages(ReviewID);


-- -----------------------------------------------------
-- Tabla: ReviewVotes
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ReviewVotes (
  VoteID SERIAL NOT NULL,
  UserID INT NOT NULL,
  ReviewID INT NOT NULL,
  VoteTypeID INT NOT NULL,
  CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (VoteID),
  CONSTRAINT fk_ReviewVotes_Users
    FOREIGN KEY (UserID)
    REFERENCES Users (UserID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_ReviewVotes_Reviews
    FOREIGN KEY (ReviewID)
    REFERENCES Reviews (ReviewID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_ReviewVotes_VoteTypes
    FOREIGN KEY (VoteTypeID)
    REFERENCES VoteTypes (VoteTypeID)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  UNIQUE (UserID, ReviewID, VoteTypeID)
);

COMMENT ON COLUMN ReviewVotes.VoteTypeID IS 'FK a VoteTypes (ej. 1 = "Like")';

CREATE INDEX idx_ReviewVotes_UserID ON ReviewVotes(UserID);
CREATE INDEX idx_ReviewVotes_ReviewID ON ReviewVotes(ReviewID);
CREATE INDEX idx_ReviewVotes_VoteTypeID ON ReviewVotes(VoteTypeID);


-- -----------------------------------------------------
-- DATOS INICIALES
-- Estos valores ahora definen la lógica de negocio
-- -----------------------------------------------------
INSERT INTO Roles (RoleID, RoleName) VALUES (1, 'Admin'), (2, 'Estudiante');

INSERT INTO ReviewStatuses (StatusID, StatusName) VALUES (1, 'Pending'), (2, 'Approved'), (3, 'Rejected');

INSERT INTO VoteTypes (VoteTypeID, VoteName, VoteValue) VALUES (1, 'Like', 1), (2, 'Dislike', -1);

-- Resetear secuencias para que coincidan con los IDs insertados
SELECT setval('roles_roleid_seq', (SELECT MAX(RoleID) FROM Roles));
SELECT setval('reviewstatuses_statusid_seq', (SELECT MAX(StatusID) FROM ReviewStatuses));
SELECT setval('votetypes_votetypeid_seq', (SELECT MAX(VoteTypeID) FROM VoteTypes));
