CREATE TABLE usuario (
    id                           UUID         PRIMARY KEY,
    nombre                       VARCHAR(80)  NOT NULL,
    apellido                     VARCHAR(80)  NOT NULL,
    email                        VARCHAR(150) NOT NULL UNIQUE,
    telefono                     CHAR(9)      NOT NULL,
    password_hash                VARCHAR(100) NOT NULL,
    ruc                          CHAR(11),
    nombre_agencia               VARCHAR(120),
    distrito_especializacion_id  SMALLINT     REFERENCES distrito (id),
    url_firma_digital            VARCHAR(500),
    url_foto_perfil              VARCHAR(500),
    rol                          VARCHAR(20)  NOT NULL CHECK (rol IN ('AGENTE', 'ADMINISTRADOR')),
    activo                       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at                   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at                   TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);
