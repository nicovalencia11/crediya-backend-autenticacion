-- =====================================
-- TABLA ROL
-- =====================================
CREATE TABLE rol (
     id_rol SERIAL PRIMARY KEY,
     nombre VARCHAR(100) NOT NULL,
     descripcion VARCHAR(255)
);
-- =====================================
-- TABLA USUARIO
-- =====================================
CREATE TABLE usuario (
     id_usuario SERIAL PRIMARY KEY,
     nombres VARCHAR(100) NOT NULL,
     apellidos VARCHAR(100) NOT NULL,
     fecha_nacimiento DATE NOT NULL,
     documento_identidad VARCHAR(50) NOT NULL,
     direccion VARCHAR(150) NOT NULL,
     telefono VARCHAR(20) NOT NULL,
     contrasena VARCHAR(250) NOT NULL,
     correo_electronico VARCHAR(100) NOT NULL UNIQUE,
     id_rol INT NULL,
     salario_base NUMERIC(15,2) NOT NULL,
     CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);
-- =====================================
-- INSERTS INICIALES EN TABLA ROL
-- =====================================
INSERT INTO rol (nombre, descripcion) VALUES
    ('ADMIN', 'Administrador del sistema'),
    ('USER', 'Usuario estándar solicitante de crédito');

-- ************************************************************************
-- ************************************************************************
-- ************************************************************************
-- ************************************************************************
-- ************************************************************************

-- =====================================
-- INCREMENTALES EN TABLA USUARIO
-- =====================================
ALTER TABLE usuario
    ADD COLUMN contrasena VARCHAR(250) NOT NULL;