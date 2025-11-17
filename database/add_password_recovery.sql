-- -----------------------------------------------------
-- Script para agregar tabla de tokens de recuperación
-- Ejecutar después de init-db.sql
-- -----------------------------------------------------

-- Tabla: PasswordRecoveryTokens
CREATE TABLE IF NOT EXISTS PasswordRecoveryTokens (
  TokenID SERIAL NOT NULL,
  UserID INT NOT NULL,
  Token VARCHAR(255) NOT NULL,
  ExpiresAt TIMESTAMP NOT NULL,
  Used BOOLEAN NOT NULL DEFAULT FALSE,
  CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (TokenID),
  UNIQUE (Token),
  CONSTRAINT fk_recovery_user
    FOREIGN KEY (UserID)
    REFERENCES Users (UserID)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE INDEX idx_recovery_token ON PasswordRecoveryTokens(Token);
CREATE INDEX idx_recovery_user ON PasswordRecoveryTokens(UserID);
CREATE INDEX idx_recovery_expires ON PasswordRecoveryTokens(ExpiresAt);

COMMENT ON TABLE PasswordRecoveryTokens IS 'Tokens para recuperación de contraseña con expiración';
COMMENT ON COLUMN PasswordRecoveryTokens.Token IS 'UUID token generado para recuperación';
COMMENT ON COLUMN PasswordRecoveryTokens.ExpiresAt IS 'Fecha de expiración (1 hora típicamente)';
COMMENT ON COLUMN PasswordRecoveryTokens.Used IS 'Si el token ya fue usado';
