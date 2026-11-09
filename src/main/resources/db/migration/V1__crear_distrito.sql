CREATE TABLE distrito (
    id          SMALLINT    PRIMARY KEY,
    nombre      VARCHAR(80) NOT NULL UNIQUE,
    zona_urbana VARCHAR(30) NOT NULL,
    activo      BOOLEAN     NOT NULL DEFAULT TRUE
);

-- 22 distritos del BCRP; ids en orden alfabetico (se replican igual en msprediccion)
INSERT INTO distrito (id, nombre, zona_urbana) VALUES
    (1,  'Ate Vitarte',        'Lima Este'),
    (2,  'Barranco',           'Lima Top'),
    (3,  'Bellavista',         'Callao'),
    (4,  'Breña',              'Lima Centro'),
    (5,  'Carabayllo',         'Lima Norte'),
    (6,  'Cercado de Lima',    'Lima Centro'),
    (7,  'Chorrillos',         'Lima Sur'),
    (8,  'Comas',              'Lima Norte'),
    (9,  'Jesús María',        'Lima Moderna'),
    (10, 'La Molina',          'Lima Top'),
    (11, 'La Perla',           'Callao'),
    (12, 'La Victoria',        'Lima Centro'),
    (13, 'Lince',              'Lima Moderna'),
    (14, 'Los Olivos',         'Lima Norte'),
    (15, 'Magdalena del Mar',  'Lima Moderna'),
    (16, 'Miraflores',         'Lima Top'),
    (17, 'Pueblo Libre',       'Lima Moderna'),
    (18, 'San Borja',          'Lima Top'),
    (19, 'San Isidro',         'Lima Top'),
    (20, 'San Miguel',         'Lima Moderna'),
    (21, 'Santiago de Surco',  'Lima Top'),
    (22, 'Surquillo',          'Lima Moderna');
