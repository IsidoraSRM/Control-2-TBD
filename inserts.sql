-- insert admin contraseña:admin1
INSERT INTO public.usuarios (
    nombreusuario, 
    contrasena, 
    correo, 
    localizacion, 
    rol
) 
VALUES (
    'admin', 
    '$2y$10$dPDS.V6zJNYTeDyzZoXi9.gnJqYZInE2OfeTEyj/kzupc1FkwNtYa', 
    'admin@example.com', 
    ST_GeomFromText('POINT(-70.6483 -33.4564)', 4326), 
    'ADMIN'
),

-- Usuarios Clientes (Región Metropolitana)
(
    'Juan Pérez', 
    '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',  -- cliente1
    'juan.perez@example.com', 
    ST_GeomFromText('POINT(-70.5796 -33.4150)', 4326),  -- Providencia
    'CLIENTE'
),
(
    'María González', 
    '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',  -- cliente1
    'maria.gonzalez@example.com', 
    ST_GeomFromText('POINT(-70.5750 -33.4167)', 4326),  -- Las Condes
    'CLIENTE'
);

-- Usuarios Clientes (Regiones)
INSERT INTO public.usuarios (
    nombreusuario, 
    contrasena, 
    correo, 
    localizacion, 
    rol
) VALUES (
    'Carlos Rojas', 
    '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',  -- cliente1
    'carlos.rojas@example.com', 
    ST_GeomFromText('POINT(-71.6200 -33.0450)', 4326),  -- Valparaíso
    'CLIENTE'
),
(
    'Ana Silva', 
    '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',  -- cliente1
    'ana.silva@example.com', 
    ST_GeomFromText('POINT(-73.0496 -36.8267)', 4326),  -- Concepción
    'CLIENTE'
),
(
    'Pedro Martínez', 
    '$2y$10$bZM2F0RgXSxZCHa8fuTNq.dvH6gcT8Jr5H560GudHbYpCCL6YwPGG',  -- cliente1
    'pedro.martinez@example.com', 
    ST_GeomFromText('POINT(-70.3989 -23.6339)', 4326),  -- Antofagasta
    'CLIENTE'
);





-- insert de distintos sectores en Chile
INSERT INTO sectores (nombre, descripcion, localizacion) VALUES
-- Región Metropolitana
('Construcción Santiago Centro', 'Obras de construcción en el centro de Santiago', ST_SetSRID(ST_MakePoint(-70.6682, -33.4489), 4326)),
('Reparación Semáforos Providencia', 'Mantenimiento semáforos en Providencia', ST_SetSRID(ST_MakePoint(-70.6167, -33.4325), 4326)),
('Mantención Calles Las Condes', 'Reparación de vías en Las Condes', ST_SetSRID(ST_MakePoint(-70.5750, -33.4167), 4326)),

-- Valparaíso
('Reparación Muelles Valparaíso', 'Mantención de muelles portuarios', ST_SetSRID(ST_MakePoint(-71.6200, -33.0450), 4326)),
('Áreas Verdes Viña del Mar', 'Mantención de parques y jardines', ST_SetSRID(ST_MakePoint(-71.5517, -33.0245), 4326)),

-- Concepción
('Construcción Edificios Concepción', 'Nuevos proyectos inmobiliarios', ST_SetSRID(ST_MakePoint(-73.0496, -36.8267), 4326)),
('Reparación Puentes Bío Bío', 'Mantención de infraestructura vial', ST_SetSRID(ST_MakePoint(-73.0786, -36.8144), 4326)),

-- Antofagasta
('Mantención Minera Escondida', 'Servicios para minería', ST_SetSRID(ST_MakePoint(-69.3481, -24.2627), 4326)),
('Infraestructura Portuaria', 'Mejoras en puerto de Antofagasta', ST_SetSRID(ST_MakePoint(-70.3989, -23.6339), 4326)),

-- Puerto Montt
('Reparación Costanera', 'Mantención de borde costero', ST_SetSRID(ST_MakePoint(-72.9439, -41.4717), 4326)),
('Sector Pesquero', 'Área de procesamiento pesquero', ST_SetSRID(ST_MakePoint(-72.9364, -41.4686), 4326));



-- Insertar tareas para usuarios en diferentes sectores
INSERT INTO public.tareas (
    titulo,
    descripcion,
    estado,
    fechavencimiento,
    idsector,
    idusuario,
    localizacion
) VALUES
-- Tareas para Juan Pérez (Providencia - Construcción)
(
    'Construcción Edificio A', 
    'Construcción de estructura principal', 
    'PENDIENTE', 
    CURRENT_DATE + INTERVAL '15 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Construcción%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Juan Pérez' LIMIT 1),
    ST_GeomFromText('POINT(-70.6000 -33.4200)', 4326)  -- Ubicación cerca de Providencia
),
(
    'Instalación Ventanas', 
    'Colocación de ventanas en pisos superiores', 
    'EN PROGRESO', 
    CURRENT_DATE + INTERVAL '7 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Construcción%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Juan Pérez' LIMIT 1),
    ST_GeomFromText('POINT(-70.6050 -33.4180)', 4326)
),

-- Tareas para María González (Las Condes - Reparación Semáforos)
(
    'Reparación Semáforo Apoquindo', 
    'Cambio de lámparas y revisión eléctrica', 
    'PENDIENTE', 
    CURRENT_DATE + INTERVAL '3 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Semáforos%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'María González' LIMIT 1),
    ST_GeomFromText('POINT(-70.5700 -33.4100)', 4326)  -- Apoquindo, Las Condes
),
(
    'Mantención Sistema Central', 
    'Actualización software control semafórico', 
    'COMPLETADA', 
    CURRENT_DATE - INTERVAL '2 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Semáforos%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'María González' LIMIT 1),
    ST_GeomFromText('POINT(-70.5755 -33.4125)', 4326)
),

-- Tareas para Carlos Rojas (Valparaíso - Reparación Muelles)
(
    'Reparación Muelle 1', 
    'Cambio de tablones deteriorados', 
    'PENDIENTE', 
    CURRENT_DATE + INTERVAL '10 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Muelles%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Carlos Rojas' LIMIT 1),
    ST_GeomFromText('POINT(-71.6220 -33.0440)', 4326)  -- Muelle Barón
),
(
    'Pintura Estructuras', 
    'Pintura anticorrosiva para estructuras metálicas', 
    'EN PROGRESO', 
    CURRENT_DATE + INTERVAL '20 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Muelles%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Carlos Rojas' LIMIT 1),
    ST_GeomFromText('POINT(-71.6180 -33.0460)', 4326)
),

-- Tareas para Ana Silva (Concepción - Construcción Edificios)
(
    'Cimentación Edificio B', 
    'Excavación y hormigonado de cimientos', 
    'PENDIENTE', 
    CURRENT_DATE + INTERVAL '30 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Edificios%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Ana Silva' LIMIT 1),
    ST_GeomFromText('POINT(-73.0500 -36.8250)', 4326)  -- Centro Concepción
),

-- Tareas para Pedro Martínez (Antofagasta - Mantención Minera)
(
    'Reparación Cinta Transportadora', 
    'Mantención preventiva sistema de transporte', 
    'PENDIENTE', 
    CURRENT_DATE + INTERVAL '5 days', 
    (SELECT idsector FROM sectores WHERE nombre LIKE '%Minera%' LIMIT 1),
    (SELECT idusuario FROM usuarios WHERE nombreusuario = 'Pedro Martínez' LIMIT 1),
    ST_GeomFromText('POINT(-70.4000 -23.6350)', 4326)  -- Cerca de puerto
);



