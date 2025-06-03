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