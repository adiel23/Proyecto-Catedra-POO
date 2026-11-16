-- ==========================================
-- SISTEMA DE GESTION BANCARIA ACADEMICA
-- BASE DE DATOS
-- ==========================================

CREATE DATABASE IF NOT EXISTS banco_academico;

USE banco_academico;

-- ==========================================
-- TABLA DE CLIENTES
-- ==========================================

CREATE TABLE IF NOT EXISTS clientes (
                                        id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                                        codigo_cliente VARCHAR(20) NOT NULL UNIQUE,
    dui VARCHAR(10) NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    correo_electronico VARCHAR(100),
    direccion VARCHAR(150),
    estado ENUM('Activo', 'Inactivo', 'Bloqueado')
    NOT NULL DEFAULT 'Activo'
    );

-- ==========================================
-- TABLA DE CUENTAS BANCARIAS
-- ==========================================

CREATE TABLE IF NOT EXISTS cuentas_bancarias (
                                                 id_cuenta INT AUTO_INCREMENT PRIMARY KEY,
                                                 numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta ENUM('Ahorro', 'Corriente') NOT NULL,
    saldo DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    estado ENUM('Activa', 'Inactiva', 'Bloqueada')
    NOT NULL DEFAULT 'Activa',
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente)
    REFERENCES clientes(id_cliente)
    );

-- ==========================================
-- CONSULTAR CLIENTES
-- ==========================================

SELECT * FROM clientes;

-- ==========================================
-- CONSULTAR CUENTAS BANCARIAS
-- ==========================================

SELECT * FROM cuentas_bancarias;

-- ==========================================
-- CONSULTAR CLIENTES Y SUS CUENTAS
-- ==========================================

SELECT
    clientes.nombre_completo,
    clientes.dui,
    cuentas_bancarias.numero_cuenta,
    cuentas_bancarias.tipo_cuenta,
    cuentas_bancarias.saldo
FROM clientes
         INNER JOIN cuentas_bancarias
                    ON clientes.id_cliente = cuentas_bancarias.id_cliente;

-- ==========================================
-- TABLA DE MOVIMIENTOS BANCARIOS - FASE 2
-- ==========================================

CREATE TABLE IF NOT EXISTS movimientos (
                                           id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
                                           tipo_movimiento ENUM('Deposito', 'Retiro', 'Transferencia') NOT NULL,
    monto DECIMAL(12,2) NOT NULL,
    fecha_movimiento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion VARCHAR(200),
    id_cuenta INT NOT NULL,
    id_cuenta_destino INT NULL,

    CONSTRAINT fk_mov_cuenta_origen
    FOREIGN KEY (id_cuenta)
    REFERENCES cuentas_bancarias(id_cuenta),

    CONSTRAINT fk_mov_cuenta_destino
    FOREIGN KEY (id_cuenta_destino)
    REFERENCES cuentas_bancarias(id_cuenta),

    CONSTRAINT chk_mov_monto
    CHECK (monto > 0),

    CONSTRAINT chk_mov_destino
    CHECK (
(tipo_movimiento = 'Transferencia'
 AND id_cuenta_destino IS NOT NULL
 AND id_cuenta_destino <> id_cuenta)
    OR
(tipo_movimiento IN ('Deposito', 'Retiro')
    AND id_cuenta_destino IS NULL)
    )
    );