-- =====================================================
-- AulaNota - Modelo relacional (compatible con SQLite / Room)
-- =====================================================
PRAGMA foreign_keys = ON;

CREATE TABLE usuario (
    id_usuario      INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre          TEXT    NOT NULL,
    correo          TEXT    NOT NULL UNIQUE,
    contrasena_hash TEXT    NOT NULL,
    fecha_registro  TEXT    NOT NULL DEFAULT (datetime('now'))
);

CREATE TABLE materia (
    id_materia  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario  INTEGER NOT NULL,
    nombre      TEXT    NOT NULL,
    profesor    TEXT,
    aula        TEXT,
    color       TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TABLE horario_clase (
    id_horario   INTEGER PRIMARY KEY AUTOINCREMENT,
    id_materia   INTEGER NOT NULL,
    dia_semana   INTEGER NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_inicio  TEXT    NOT NULL,
    hora_fin     TEXT    NOT NULL,
    FOREIGN KEY (id_materia) REFERENCES materia(id_materia) ON DELETE CASCADE
);

CREATE TABLE asistencia (
    id_asistencia INTEGER PRIMARY KEY AUTOINCREMENT,
    id_horario    INTEGER NOT NULL,
    fecha         TEXT    NOT NULL,
    estado        TEXT    NOT NULL CHECK (estado IN ('asistio','falta','retardo')),
    UNIQUE (id_horario, fecha),
    FOREIGN KEY (id_horario) REFERENCES horario_clase(id_horario) ON DELETE CASCADE
);

CREATE TABLE apunte (
    id_apunte           INTEGER PRIMARY KEY AUTOINCREMENT,
    id_materia          INTEGER NOT NULL,
    titulo              TEXT    NOT NULL,
    contenido           TEXT,
    fecha_creacion      TEXT    NOT NULL DEFAULT (datetime('now')),
    fecha_modificacion  TEXT,
    FOREIGN KEY (id_materia) REFERENCES materia(id_materia) ON DELETE CASCADE
);

CREATE TABLE mazo (
    id_mazo     INTEGER PRIMARY KEY AUTOINCREMENT,
    id_materia  INTEGER NOT NULL,
    titulo      TEXT    NOT NULL,
    FOREIGN KEY (id_materia) REFERENCES materia(id_materia) ON DELETE CASCADE
);

CREATE TABLE tarjeta (
    id_tarjeta  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_mazo     INTEGER NOT NULL,
    pregunta    TEXT    NOT NULL,
    respuesta   TEXT    NOT NULL,
    dominada    INTEGER NOT NULL DEFAULT 0 CHECK (dominada IN (0,1)),
    FOREIGN KEY (id_mazo) REFERENCES mazo(id_mazo) ON DELETE CASCADE
);

CREATE INDEX idx_materia_usuario  ON materia(id_usuario);
CREATE INDEX idx_horario_materia  ON horario_clase(id_materia);
CREATE INDEX idx_apunte_materia   ON apunte(id_materia);
CREATE INDEX idx_mazo_materia     ON mazo(id_materia);
CREATE INDEX idx_tarjeta_mazo     ON tarjeta(id_mazo);
