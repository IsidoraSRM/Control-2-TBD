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
