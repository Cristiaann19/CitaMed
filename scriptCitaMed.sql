CREATE DATABASE IF NOT EXISTS CitaMed;
USE CitaMed;

-- ========================
-- 1. USUARIOS
-- ========================
INSERT INTO Usuarios (user_name, password, rol, activo) VALUES
-- Admins
('admin01',            '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'ADMIN',          true),
('marcelo.alarcon',    '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'ADMIN',          true),
('cristian.huaman',    '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'ADMIN',          true),
('junior.zumaeta',     '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'ADMIN',          true),
('recep01',            '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',  true),
('recep02',            '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',  true),
('recep03',            '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',  false),
('med.garcia',         '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.torres',         '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.ramirez',        '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.flores',         '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.castro',         '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.herrera',        '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.vega',           '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.mora',           '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.rivas',          '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.pinto',          '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('med.aguilar',        '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'MEDICO',         true),
('enf.lopez',          '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',      true),
('enf.perez',          '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',      true),
('enf.ruiz',           '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',      true),
('enf.diaz',           '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',      true),
('enf.ortiz',          '$2a$10$800I/8HGwCkjx0iApGb/kevpKOnZ1jp2UjTTWCg9CKaUNKdFhoywa', 'RECEPCIONISTA',      true);

-- ========================
-- 2. ESPECIALIDADES
-- ========================
INSERT INTO Especialidades (nombre, descripcion) VALUES
('Urología',                  'Diagnóstico y tratamiento del sistema urinario y reproductor masculino'),
('Reumatología',              'Tratamiento de enfermedades articulares, musculares y autoinmunes'),
('Medicina General',          'Atención médica primaria y consultas generales de salud'),
('Cardiología',               'Diagnóstico y tratamiento de enfermedades cardiovasculares'),
('Pediatría',                 'Atención médica integral de niños desde el nacimiento hasta la adolescencia'),
('Ginecología y Obstetricia', 'Salud reproductiva femenina, embarazo y parto'),
('Traumatología y Ortopedia', 'Lesiones y enfermedades del aparato locomotor'),
('Neurología',                'Enfermedades del sistema nervioso central y periférico'),
('Dermatología',              'Enfermedades de la piel, cabello y uñas'),
('Endocrinología',            'Trastornos hormonales y metabólicos'),
('Gastroenterología',         'Enfermedades del sistema digestivo'),
('Oftalmología',              'Enfermedades y cirugías del ojo');

-- ========================
-- 3. CONSULTORIOS 
-- ========================
INSERT INTO Consultorios (numero, descripcion, disponible, especialidad_id) VALUES
('C-101', 'Consultorio de Medicina General N°1',  true,  1),
('C-102', 'Consultorio de Medicina General N°2',  true,  1),
('C-103', 'Consultorio de Medicina General N°3',  false, 1),
('C-201', 'Consultorio de Cardiología N°1',        true,  2),
('C-202', 'Consultorio de Cardiología N°2',        true,  2),
('C-301', 'Consultorio de Pediatría N°1',          true,  3),
('C-302', 'Consultorio de Pediatría N°2',          false, 3),
('C-401', 'Consultorio de Ginecología N°1',        true,  4),
('C-402', 'Consultorio de Ginecología N°2',        true,  4),
('C-501', 'Consultorio de Traumatología N°1',      true,  5),
('C-601', 'Consultorio de Neurología N°1',         true,  6),
('C-602', 'Consultorio de Neurología N°2',         true,  6);

-- ========================
-- 4. EMPLEADOS
-- ========================

INSERT INTO Empleados (nombre, apellido_paterno, apellido_materno, dni, telefono, direccion, email, fecha_nacimiento, genero, salario, fecha_ingreso, activo, usuario_id) VALUES
-- Integrantes del equipo (ADMIN) apuntando a IDs 2, 3, 4
('MARCELO ARIEL',  'ALARCON',   'MANAY',    '74100001', '987000001', 'Av. Los Desarrolladores 101, Chiclayo', 'marcelo.alarcon@citamed.com',  '1999-05-15', 'MASCULINO', 4500.00, '2024-01-01', true,  2),
('CRISTIAN',       'HUAMAN',    'CRUZ',     '74100002', '987000002', 'Jr. Full Stack 202, Chiclayo',          'cristian.huaman@citamed.com',  '1999-08-22', 'MASCULINO', 4500.00, '2024-01-01', true,  3),
('JUNIOR FERNANDO','ZUMAETA',   'GOLAC',    '74100003', '987000003', 'Calle Backend 303, Chiclayo',           'junior.zumaeta@citamed.com',   '1999-11-10', 'MASCULINO', 4500.00, '2024-01-01', true,  4),
-- Admin genérico apuntando al ID 1
('CARLOS',         'MENDOZA',   'QUISPE',   '45678901', '987654321', 'Av. Los Olivos 123, Chiclayo',          'carlos.mendoza@citamed.com',   '1985-03-15', 'MASCULINO', 4200.00, '2018-01-10', true,  1),
-- Recepcionistas apuntando a IDs 5 y 6
('MARIA',          'LOPEZ',     'SANCHEZ',  '45678902', '987654322', 'Jr. Los Pinos 456, Chiclayo',           'maria.lopez@citamed.com',      '1990-07-22', 'FEMENINO',  1800.00, '2020-03-01', true,  5),
('ANA',            'PEREZ',     'CASTILLO', '45678903', '987654323', 'Calle Manco Capac 789, Chiclayo',       'ana.perez@citamed.com',        '1992-11-05', 'FEMENINO',  1800.00, '2021-06-15', true,  6),
-- Enfermeros (IDs ajustados: 19, 20, 21...)
('JOSE',           'RUIZ',      'VARGAS',   '45678910', '987654330', 'Av. Salaverry 321, Chiclayo',           'jose.ruiz@citamed.com',        '1988-04-18', 'MASCULINO', 2200.00, '2019-08-01', true,  19),
('LUCIA',          'DIAZ',      'FLORES',   '45678911', '987654331', 'Jr. Amazonas 654, Chiclayo',            'lucia.diaz@citamed.com',       '1991-09-25', 'FEMENINO',  2200.00, '2020-01-15', true,  20),
-- Personal adicional (les asigné los IDs 21, 22 y 23 que son los que sobran de enfermeros)
('ROBERTO',        'NUNEZ',     'PAREDES',  '45678914', '987654334', 'Av. Grau 987, Chiclayo',                'roberto.nunez@citamed.com',    '1987-12-10', 'MASCULINO', 1500.00, '2018-05-01', true,  21),
('ELENA',          'MENDOZA',   'CRUZ',     '45678916', '987654336', 'Calle 7 de Enero 147, Chiclayo',        'elena.mendoza@citamed.com',    '1989-06-30', 'FEMENINO',  1500.00, '2019-11-01', true,  22),
('PEDRO',          'AGUILAR',   'RIOS',     '45678918', '987654338', 'Av. Bolognesi 258, Chiclayo',           'pedro.aguilar@citamed.com',    '1983-02-14', 'MASCULINO', 3800.00, '2017-03-20', true,  23);
-- ========================
-- 5. MEDICOS
-- ========================
INSERT INTO Medicos (nombre, apellido_paterno, apellido_materno, dni, telefono, direccion, email, fecha_nacimiento, genero, numero_colegiatura, activo, especialidad_id, usuario_id, consultorio_id) VALUES
('LUIS',      'GARCIA',   'MONTOYA',  '12345601', '912345601', 'Av. Pardo 101, Chiclayo',            'luis.garcia@citamed.com',    '1978-05-12', 'MASCULINO', 'CMP-045231', true,  1,  8,  1),
('ROSA',      'TORRES',   'GUTIERREZ','12345602', '912345602', 'Jr. Colon 202, Chiclayo',            'rosa.torres@citamed.com',    '1980-09-18', 'FEMENINO',  'CMP-038742', true,  2,  9,  2),
('MIGUEL',    'RAMIREZ',  'DELGADO',  '12345603', '912345603', 'Calle Real 303, Chiclayo',           'miguel.ramirez@citamed.com', '1975-03-27', 'MASCULINO', 'CMP-029183', true,  3,  10, 1),
('CARMEN',    'FLORES',   'ESPINOZA', '12345604', '912345604', 'Av. Venezuela 404, Chiclayo',        'carmen.flores@citamed.com',  '1982-07-08', 'FEMENINO',  'CMP-051647', true,  4,  11, 4),
('JORGE',     'CASTRO',   'HUAMAN',   '12345605', '912345605', 'Jr. Tacna 505, Chiclayo',            'jorge.castro@citamed.com',   '1977-11-14', 'MASCULINO', 'CMP-034829', true,  5,  12, 6),
('PATRICIA',  'HERRERA',  'LEON',     '12345606', '912345606', 'Av. Balta 606, Chiclayo',            'patricia.herrera@citamed.com','1983-01-30', 'FEMENINO',  'CMP-062318', true,  6,  13, 8),
('FERNANDO',  'VEGA',     'QUISPE',   '12345607', '912345607', 'Calle Elias Aguirre 707, Chiclayo',  'fernando.vega@citamed.com',  '1979-06-22', 'MASCULINO', 'CMP-041095', true,  7,  14, 10),
('SANDRA',    'MORA',     'PAREDES',  '12345608', '912345608', 'Jr. Torres Paz 808, Chiclayo',       'sandra.mora@citamed.com',    '1981-10-03', 'FEMENINO',  'CMP-057382', true,  8,  15, 11),
('HECTOR',    'RIVAS',    'CARDENAS', '12345609', '912345609', 'Av. Jose Balta 909, Chiclayo',       'hector.rivas@citamed.com',   '1976-04-17', 'MASCULINO', 'CMP-027461', true,  9,  16, 2),
('GABRIELA',  'PINTO',    'SALINAS',  '12345610', '912345610', 'Calle Siete de Enero 1010, Chiclayo','gabriela.pinto@citamed.com', '1984-08-25', 'FEMENINO',  'CMP-068754', true,  1,  17, 2);
-- ========================
-- 5b. ASEGURAR QUE TODOS LOS MEDICOS TENGAN CONSULTORIO
-- ========================
UPDATE Medicos SET consultorio_id = 1 WHERE consultorio_id IS NULL;

-- ========================
-- 6. HORARIOS MEDICOS (CORREGIDO - agregado médico 10)
-- ========================
INSERT INTO horarios_medicos (dia, hora_inicio, hora_fin, activo, medico_id, consultorio_id) VALUES
('LUNES',     '08:00:00', '13:00:00', true,  1,  1),
('MIERCOLES', '08:00:00', '13:00:00', true,  1,  1),
('VIERNES',   '08:00:00', '12:00:00', true,  1,  2),
('MARTES',    '09:00:00', '14:00:00', true,  2,  4),
('JUEVES',    '09:00:00', '14:00:00', true,  2,  4),
('SABADO',    '08:00:00', '12:00:00', true,  2,  5),
('LUNES',     '14:00:00', '19:00:00', true,  3,  6),
('MIERCOLES', '14:00:00', '19:00:00', true,  3,  6),
('VIERNES',   '14:00:00', '19:00:00', true,  3,  6),
('MARTES',    '08:00:00', '13:00:00', true,  4,  8),
('JUEVES',    '08:00:00', '13:00:00', true,  4,  8),
('SABADO',    '09:00:00', '12:00:00', true,  4,  9),
('LUNES',     '08:00:00', '14:00:00', true,  5,  10),
('MIERCOLES', '08:00:00', '14:00:00', true,  5,  10),
('MARTES',    '15:00:00', '20:00:00', true,  6,  11),
('JUEVES',    '15:00:00', '20:00:00', true,  6,  11),
('VIERNES',   '15:00:00', '20:00:00', true,  6,  12),
('LUNES',     '09:00:00', '14:00:00', true,  7,  2),
('MIERCOLES', '09:00:00', '14:00:00', true,  8,  2),
('VIERNES',   '09:00:00', '13:00:00', true,  9,  1),
('LUNES',     '08:00:00', '13:00:00', true,  10, 1),
('MIERCOLES', '08:00:00', '13:00:00', true,  10, 2),
('VIERNES',   '08:00:00', '13:00:00', true,  10, 2);
-- ========================
-- 7. PACIENTES
-- ========================
INSERT INTO Pacientes (nombre, apellido_paterno, apellido_materno, dni, telefono, direccion, email, fecha_nacimiento, genero, grupo_sanguineo, activo) VALUES
('JUAN CARLOS',      'QUISPE',      'MAMANI',     '71234501', '951234501', 'Av. Agricultura 101, Chiclayo',      'jc.quispe@gmail.com',              '1990-02-14', 'MASCULINO', 'O_POSITIVO', TRUE),
('MARIA ELENA',      'SOTO',        'VARGAS',     '71234502', '951234502', 'Jr. Los Alamos 202, Chiclayo',       'maria.soto@gmail.com',             '1985-06-23', 'FEMENINO',  'A_POSITIVO', TRUE),
('PEDRO PABLO',      'HUAMAN',      'QUISPE',     '71234503', '951234503', 'Calle Los Fresnos 303, Chiclayo',    'pedro.huaman@gmail.com',           '1978-11-05', 'MASCULINO', 'B_POSITIVO', TRUE),
('LUCIA ROSA',       'PAREDES',     'TORRES',     '71234504', '951234504', 'Av. Fitzcarrald 404, Chiclayo',      'lucia.paredes@gmail.com',          '1995-03-18', 'FEMENINO',  'AB_POSITIVO', TRUE),
('CARLOS ALBERTO',   'MENDOZA',     'LOPEZ',      '71234505', '951234505', 'Jr. San Martin 505, Chiclayo',       'carlos.mendoza.p@gmail.com',       '1988-07-30', 'MASCULINO', 'O_NEGATIVO', TRUE),
('ANA SOFIA',        'ROJAS',       'CASTRO',     '71234506', '951234506', 'Calle Dos de Mayo 606, Chiclayo',    'ana.rojas@gmail.com',              '1992-09-12', 'FEMENINO',  'A_NEGATIVO', TRUE),
('ROBERTO JESUS',    'VARGAS',      'DIAZ',       '71234507', '951234507', 'Av. Saenz Pena 707, Chiclayo',       'roberto.vargas@gmail.com',         '1975-01-08', 'MASCULINO', 'B_NEGATIVO', TRUE),
('ISABEL CRISTINA',  'CHAVEZ',      'MORALES',    '71234508', '951234508', 'Jr. Ladislao Espinar 808, Chiclayo', 'isabel.chavez@gmail.com',          '1983-05-27', 'FEMENINO',  'O_POSITIVO', TRUE),
('DIEGO ALEJANDRO',  'FUENTES',     'HERRERA',    '71234509', '951234509', 'Calle Pedro Ruiz 909, Chiclayo',     'diego.fuentes@gmail.com',          '1998-12-03', 'MASCULINO', 'A_POSITIVO', TRUE),
('VALENTINA',        'ESPINOZA',    'NUNEZ',      '71234510', '951234510', 'Av. Leguia 1010, Chiclayo',          'valentina.espinoza@gmail.com',     '2001-04-16', 'FEMENINO',  'AB_NEGATIVO', TRUE),
('ANDRES FELIPE',    'LLONTOP',     'BANCES',     '71234511', '951234511', 'Jr. Colon 111, Chiclayo',            'andres.llontop@gmail.com',         '1970-08-21', 'MASCULINO', 'O_POSITIVO', TRUE),
('GABRIELA NOEMI',   'ZULOETA',     'CARRASCO',   '71234512', '951234512', 'Av. Pedro Ruiz 222, Chiclayo',       'gabriela.zuloeta@gmail.com',       '1987-10-09', 'FEMENINO',  'B_POSITIVO', TRUE),
('RICARDO MANUEL',   'GONZALES',    'MURO',       '71234513', '951234513', 'Calle Izaga 333, Chiclayo',          'ricardo.gonzales@gmail.com',       '1993-02-28', 'MASCULINO', 'A_POSITIVO', TRUE),
('SOFIA VALENTINA',  'CHAFLOQUE',   'SOLIS',      '71234514', '951234514', 'Jr. 8 de Octubre 444, Chiclayo',     'sofia.chafloque@gmail.com',        '2000-06-14', 'FEMENINO',  'O_POSITIVO', TRUE),
('MARTIN EDUARDO',   'IDROGO',      'BUSTAMANTE', '71234515', '951234515', 'Av. Los Incas 555, Chiclayo',        'martin.idrogo@gmail.com',          '1965-11-30', 'MASCULINO', 'A_NEGATIVO', TRUE),
('PATRICIA LUZ',     'CORONADO',    'DIAZ',       '71234516', '951234516', 'Calle Real 666, Chiclayo',           'patricia.coronado@gmail.com',      '1980-03-07', 'FEMENINO',  'B_POSITIVO', TRUE),
('EDUARDO JESUS',    'SAAVEDRA',    'NEYRA',      '71234517', '951234517', 'Jr. San Jose 777, Chiclayo',         'eduardo.saavedra@gmail.com',       '1972-07-19', 'MASCULINO', 'O_POSITIVO', TRUE),
('CLAUDIA BEATRIZ',  'SEGURA',      'MONTOYA',    '71234518', '951234518', 'Av. Chiclayo 888, Chiclayo',         'claudia.segura@gmail.com',         '1996-09-02', 'FEMENINO',  'AB_POSITIVO', TRUE),
('MARCO ANTONIO',    'BECERRA',     'VILLANUEVA', '71234519', '951234519', 'Calle Los Pinos 999, Chiclayo',      'marco.becerra@gmail.com',          '1982-01-25', 'MASCULINO', 'A_POSITIVO', TRUE),
('NATALIA ANDREA',   'CUBAS',       'MEJIA',      '71234520', '951234520', 'Jr. Victor Raul 1100, Chiclayo',     'natalia.cubas@gmail.com',          '1991-05-11', 'FEMENINO',  'O_NEGATIVO', TRUE),
('CESAR AUGUSTO',    'FERNANDEZ',   'PAZ',        '71234521', '951234521', 'Av. Chinchaysuyo 200, Chiclayo',     'cesar.fernandez@gmail.com',        '1969-03-15', 'MASCULINO', 'B_POSITIVO', TRUE),
('MILAGROS',         'PUELLES',     'ASALDE',     '71234522', '951234522', 'Jr. Los Jazmines 300, Chiclayo',     'milagros.puelles@gmail.com',       '1976-08-29', 'FEMENINO',  'O_POSITIVO', TRUE),
('JHON ALEXANDER',   'GUERRERO',    'RIVADENEYRA','71234523', '951234523', 'Calle Manco Capac 400, Chiclayo',    'jhon.guerrero@gmail.com',          '1999-12-17', 'MASCULINO', 'A_POSITIVO', TRUE),
('FIORELLA MILAGROS','TELLO',       'PERALES',    '71234524', '951234524', 'Av. Venezuela 500, Chiclayo',        'fiorella.tello@gmail.com',         '2003-04-05', 'FEMENINO',  'AB_POSITIVO', TRUE),
('ALEJANDRO LUIS',   'SANTISTEBAN', 'CABREJOS',   '71234525', '951234525', 'Jr. Elias Aguirre 600, Chiclayo',    'alejandro.santisteban@gmail.com',  '1960-06-20', 'MASCULINO', 'O_POSITIVO', TRUE),
('YESICA PAMELA',    'FALLA',       'MEDINA',     '71234526', '951234526', 'Calle Torres Paz 700, Chiclayo',     'yesica.falla@gmail.com',           '1994-10-08', 'FEMENINO',  'B_NEGATIVO', TRUE),
('HUGO ARMANDO',     'BRAVO',       'ALCANTARA',  '71234527', '951234527', 'Av. Grecia 800, Chiclayo',           'hugo.bravo@gmail.com',             '1973-02-14', 'MASCULINO', 'A_NEGATIVO', TRUE),
('ROSARIO ESPERANZA','ALIAGA',      'DELGADO',    '71234528', '951234528', 'Jr. Balta 900, Chiclayo',            'rosario.aliaga@gmail.com',         '1968-07-03', 'FEMENINO',  'O_POSITIVO', TRUE),
('KEVIN SEBASTIAN',  'HEREDIA',     'CAMPOS',     '71234529', '951234529', 'Calle Junin 1000, Chiclayo',         'kevin.heredia@gmail.com',          '2005-11-28', 'MASCULINO', 'B_POSITIVO', TRUE),
('LORENA PATRICIA',  'VALDERRAMA',  'CHIRINOS',   '71234530', '951234530', 'Av. Mariscal Castilla 1100, Chiclayo','lorena.valderrama@gmail.com',     '1986-04-22', 'FEMENINO',  'A_POSITIVO', TRUE);
-- ========================
-- 8. HISTORIALES MEDICOS
-- ========================
INSERT INTO Historiales_Medicos (paciente_id) VALUES
(1),(2),(3),(4),(5),(6),(7),(8),(9),(10),
(11),(12),(13),(14),(15),(16),(17),(18),(19),(20),
(21),(22),(23),(24),(25),(26),(27),(28),(29),(30);

-- ========================
-- 9. CITAS
-- ========================

INSERT INTO Citas (paciente_id, medico_id, consultorio_id, fecha_hora, motivo_consulta, estado) VALUES
(1,  1,  1,  '2026-05-04 08:00:00', 'Dolor de cabeza persistente y mareos',      'ATENDIDA'), -- ID 1
(2,  2,  4,  '2026-05-05 09:00:00', 'Revision de presion arterial alta',         'ATENDIDA'), -- ID 2
(3,  3,  6,  '2026-05-05 14:00:00', 'Control pediatrico de nino de 5 anos',       'ATENDIDA'), -- ID 3
(4,  4,  8,  '2026-05-06 08:00:00', 'Consulta por irregularidades menstruales',   'ATENDIDA'), -- ID 4
(5,  5,  10, '2026-05-06 09:00:00', 'Dolor en rodilla derecha tras caida',        'ATENDIDA'), -- ID 5
(6,  6,  11, '2026-05-07 15:00:00', 'Episodios de migrana frecuentes',            'ATENDIDA'), -- ID 6
(7,  1,  1,  '2026-05-07 08:00:00', 'Tos persistente y fiebre leve',              'ATENDIDA'), -- ID 7
(8,  2,  4,  '2026-05-08 09:00:00', 'Palpitaciones y cansancio',                  'ATENDIDA'), -- ID 8
(1,  10, 1,  '2026-05-10 08:00:00', 'Control medico trimestral',                  'ATENDIDA'), -- ID 9
(9,  7,  2,  '2026-05-11 09:00:00', 'Dermatitis en brazos',                       'ATENDIDA'), -- ID 10
(5,  5,  10, '2026-05-11 08:00:00', 'Revision de fractura curada',                'ATENDIDA'), -- ID 11
(15, 8,  2,  '2026-05-11 09:00:00', 'Hipotiroidismo, control mensual',            'ATENDIDA'), -- ID 12
(10, 8,  2,  '2026-05-12 09:00:00', 'Fatiga extrema y aumento de peso',            'ATENDIDA'), -- ID 13
(11, 9,  1,  '2026-05-12 08:00:00', 'Nauseas y dolor abdominal',                  'ATENDIDA'), -- ID 14
(12, 1,  2,  '2026-05-13 08:00:00', 'Chequeo medico general',                     'ATENDIDA'), -- ID 15
(13, 3,  6,  '2026-05-13 14:00:00', 'Fiebre alta en nino de 8 anos',              'ATENDIDA'), -- ID 16
(14, 4,  9,  '2026-05-14 08:00:00', 'Control de embarazo mes 5',                  'ATENDIDA'), -- ID 17
(15, 5,  10, '2026-05-14 08:00:00', 'Dolor lumbar cronico',                       'ATENDIDA'), -- ID 18
(22, 9,  1,  '2026-05-14 08:00:00', 'Gastroenterologia, seguimiento',             'ATENDIDA'), -- ID 19
(28, 2,  4,  '2026-05-15 09:00:00', 'Hipertension, ajuste de tratamiento',        'ATENDIDA'), -- ID 20
(16, 10, 1,  '2026-05-18 08:00:00', 'Resfriado y dolor de garganta',              'ATENDIDA'), -- ID 21
(17, 6,  11, '2026-05-19 15:00:00', 'Entumecimiento en extremidades',             'ATENDIDA'), -- ID 22
(18, 2,  5,  '2026-05-19 09:00:00', 'Control post-operatorio cardiaco',           'ATENDIDA'), -- ID 23
(19, 1,  1,  '2026-05-20 08:00:00', 'Gastritis y acidez estomacal',               'ATENDIDA'), -- ID 24
(20, 4,  8,  '2026-05-20 08:00:00', 'Revision de quiste ovarico',                 'ATENDIDA'), -- ID 25
(21, 3,  6,  '2026-05-21 14:00:00', 'Vacunacion y control de crecimiento',        'ATENDIDA'), -- ID 26
(22, 9,  1,  '2026-05-21 08:00:00', 'Colon irritable, control mensual',            'ATENDIDA'), -- ID 27
(23, 5,  10, '2026-05-25 08:00:00', 'Esguince de tobillo izquierdo',              'ATENDIDA'), -- ID 28
(24, 7,  2,  '2026-05-26 09:00:00', 'Acne y problemas en piel grasa',              'ATENDIDA'), -- ID 29
(25, 8,  2,  '2026-05-26 09:00:00', 'Diabetes tipo 2, control trimestral',        'ATENDIDA'), -- ID 30
(10, 2,  2,  '2026-05-27 09:00:00', 'Dolor de espalda y fatiga',                  'PROGRAMADA'), -- ID 31
(6,  6,  12, '2026-05-27 15:00:00', 'Vertigo y perdida de equilibrio',            'PROGRAMADA'), -- ID 32
(28, 1,  1,  '2026-05-28 08:00:00', 'Presion alta y dolor de cabeza',             'PROGRAMADA'), -- ID 33
(3,  3,  6,  '2026-05-29 14:00:00', 'Control de asma infantil',                   'PROGRAMADA'), -- ID 34
(2,  2,  4,  '2026-05-30 09:00:00', 'Arritmia, control semestral',                'PROGRAMADA'); -- ID 35
-- ========================
-- 10. PAGOS
-- ========================
INSERT INTO Pagos (cita_id, monto, metodo_pago, estado, fecha_pago) VALUES
(1,  80.00,  'EFECTIVO',      'PAGADO', '2026-05-04 08:05:00'),
(2,  120.00, 'TARJETA',       'PAGADO', '2026-05-05 09:10:00'),
(3,  90.00,  'EFECTIVO',      'PAGADO', '2026-05-05 14:05:00'),
(4,  100.00, 'TRANSFERENCIA', 'PAGADO', '2026-05-06 08:05:00'),
(5,  110.00, 'EFECTIVO',      'PAGADO', '2026-05-06 09:05:00'),
(6,  130.00, 'TARJETA',       'PAGADO', '2026-05-07 15:05:00'),
(7,  80.00,  'EFECTIVO',      'PAGADO', '2026-05-07 08:05:00'),
(8,  120.00, 'EFECTIVO',      'PAGADO', '2026-05-08 09:05:00'),
(9,  100.00, 'TARJETA',       'PAGADO', '2026-05-10 08:05:00'),
(10, 150.00, 'TRANSFERENCIA', 'PAGADO', '2026-05-11 09:05:00'),
(11, 90.00,  'EFECTIVO',      'PAGADO', '2026-05-11 08:05:00'),
(12, 80.00,  'EFECTIVO',      'PAGADO', '2026-05-11 09:05:00'),
(13, 90.00,  'TARJETA',       'PAGADO', '2026-05-12 09:05:00'),
(14, 100.00, 'EFECTIVO',      'PAGADO', '2026-05-12 08:05:00'),
(15, 110.00, 'TRANSFERENCIA', 'PAGADO', '2026-05-13 08:05:00'),
(16, 80.00,  'EFECTIVO',      'PAGADO', '2026-05-13 14:05:00'),
(17, 130.00, 'TARJETA',       'PAGADO', '2026-05-14 08:05:00'),
(18, 120.00, 'TARJETA',       'PAGADO', '2026-05-14 08:05:00'),
(19, 80.00,  'EFECTIVO',      'PAGADO', '2026-05-14 08:05:00'),
(20, 100.00, 'EFECTIVO',      'PAGADO', '2026-05-15 09:05:00'),
(21, 90.00,  'EFECTIVO',      'PAGADO', '2026-05-18 08:05:00'),
(22, 90.00,  'TARJETA',       'PAGADO', '2026-05-19 15:05:00'),
(23, 110.00, 'EFECTIVO',      'PAGADO', '2026-05-19 09:05:00'),
(24, 100.00, 'TARJETA',       'PAGADO', '2026-05-20 08:05:00'),
(25, 150.00, 'TRANSFERENCIA', 'PAGADO', '2026-05-20 08:05:00'),
(26, 80.00,  'EFECTIVO',      'PAGADO', '2026-05-21 14:05:00'),
(27, 130.00, 'TARJETA',       'PAGADO', '2026-05-21 08:05:00'),
(28, 80.00,  'EFECTIVO',      'PAGADO', '2026-05-25 08:05:00'),
(29, 90.00,  'EFECTIVO',      'PAGADO', '2026-05-26 09:05:00'),
(30, 120.00, 'TARJETA',       'PAGADO', '2026-05-26 09:05:00');

-- ========================
-- 12. DIAGNOSTICOS
-- ========================
INSERT INTO Diagnosticos (enfermedad, descripcion, receta, indicaciones, cita_id) VALUES
('CEFALEA TENSIONAL',                  'Dolor de cabeza de tipo tensional sin signos neurologicos focales',              'Ibuprofeno 400mg / Paracetamol 500mg',                                                                   'Reposo relativo, evitar pantallas, hidratacion. Tomar cada 8 horas con alimentos.',      1),
('HIPERTENSION ARTERIAL',              'HTA grado 2, en control farmacologico con enalapril',                            'Enalapril 10mg cada 12h / Hidroclorotiazida 12.5mg',                                                     'Dieta hiposodica, ejercicio moderado diario. No suspender sin consultar.',                2),
('DESARROLLO NORMAL',                  'Nino sano, desarrollo psicomotor adecuado para 5 anos',                         'Vitamina D 1000UI / Vitamina C 500mg',                                                                  'Dieta equilibrada, actividad fisica regular. Suplemento vitaminico.',                     3),
('OLIGOMENORREA',                      'Ciclos menstruales irregulares, posible etiologia hormonal',                    'Anticonceptivo oral combinado 1 comprimido cada 24h / Acido folico 5mg',                                 'Tomar a la misma hora todos los dias. Control ecografico en 30 dias.',                   4),
('CONTUSION DE RODILLA',               'Traumatismo directo en cara anterior de rodilla derecha sin fractura',          'Naproxeno 500mg cada 12h / Tramadol 50mg cada 8h segun dolor',                                           'Reposo, hielo local 20 min cada 4h. Puede producir somnolencia.',                        5),
('MIGRANA CON AURA',                   'Migrana cronica episodica con fenomenos visuales previos',                      'Propranolol 40mg/dia / Sumatriptan 50mg en crisis',                                                      'No suspender bruscamente. Usar sumatriptan al inicio del dolor. Max 200mg/dia.',         6),
('INFECCION RESPIRATORIA AGUDA',       'Rinofaringitis aguda de probable etiologia viral',                              'Amoxicilina 500mg cada 8h / Ibuprofeno 400mg cada 8h',                                                   'Terminar tratamiento completo. Solo ibuprofeno si fiebre >38°. Hidratacion.',            7),
('TAQUICARDIA SINUSAL',                'Taquicardia sinusal por ansiedad, sin cardiopatia subyacente',                 'Atenolol 25mg/dia / Loratadina 10mg/dia',                                                               'Tomar con desayuno. Controlar frecuencia cardiaca. Reducir cafeina.',                    8),
('DERMATITIS ATOPICA',                 'Dermatitis atopica leve en pliegues de codos y rodillas',                      'Betametasona crema 0.05% topico / Emoliente corporal',                                                   'Aplicar capa fina en zona afectada. Emoliente tras el bano. No en cara.',                9),
('HIPOTIROIDISMO PRIMARIO',            'Hipotiroidismo diagnosticado por TSH elevada y T4 libre bajo',                 'Levotiroxina 50mcg en ayunas',                                                                           'Esperar 30 min antes de comer. No con calcio o hierro. Control TSH en 4 sem.',           10),
('GASTRITIS CRONICA',                  'Gastritis antral cronica no erosiva, H. pylori negativo',                      'Omeprazol 20mg antes del desayuno / Sucralfato 1g 3 veces al dia',                                       'Dieta blanda sin irritantes. Evitar AINES. Sucralfato 1h antes de comidas.',             11),
('SIN PATOLOGIA AGUDA',                'Examen fisico y laboratorios dentro de rangos normales',                       '',                                                                                                        'Continuar habitos saludables. Actividad fisica 150 min/semana. Control anual.',          12),
('AMIGDALITIS BACTERIANA',             'Amigdalitis pultacea compatible con Streptococcus pyogenes',                   'Amoxicilina 500mg cada 8h / Ibuprofeno pediatrico 200mg',                                                'Completar tratamiento 7 dias. Gargaras salinas. Calcular ibuprofeno por peso.',          13),
('EMBARAZO NORMAL SEMANA 22',          'Gestacion de bajo riesgo con feto en adecuadas condiciones',                   'Acido folico 1mg/dia / Sulfato ferroso 300mg',                                                           'Indispensable en embarazo. Tomar con vitamina C para absorcion de hierro.',              14),
('LUMBALGIA MECANICA',                 'Lumbalgia inespecifica de tipo mecanico sin irradiacion',                      'Ciclobenzaprina 5mg cada 8h / Diclofenaco 75mg cada 12h',                                                'Fisioterapia 3 veces/semana. Faja lumbar en actividad. Puede causar somnolencia.',       15),
('FARINGOAMIGDALITIS VIRAL',           'Infeccion viral de vias respiratorias altas sin complicaciones',              'Paracetamol 1g cada 8h',                                                                                 'Reposo relativo 48h. No sobrepasar 3g al dia. Hidratacion abundante.',                   16),
('NEUROPATIA PERIFERICA LEVE',         'Parestesias en manos sin deficit motor, origen a determinar',                 'Vitamina B1 100mg/dia / Vitamina B12 500mcg/dia',                                                        'Ejercicios de movilidad de manos. Tomar con el desayuno.',                               17),
('POST-OPERATORIO BYPASS',             'Buena evolucion post bypass aortocoronario de doble vaso',                    'Aspirina 100mg/dia / Clopidogrel 75mg/dia / Warfarina 5mg/dia',                                          'Antiagregante + anticoagulante. Control INR semanal. Consultar antes de procedimientos.',18),
('GASTRITIS POR AINES',                'Irritacion gastrica secundaria al uso cronico de ibuprofeno',                 'Omeprazol 20mg antes del desayuno',                                                                      'Suspender AINES. Dieta blanda. Evitar alcohol.',                                         19),
('QUISTE OVARICO FUNCIONAL',           'Quiste folicular en ovario derecho de 3cm, sin tabiques',                     'Anticonceptivo oral 1 comprimido cada 24h',                                                              'Tomar a la misma hora. Ecografia de control en 60 dias.',                                20),
('DESARROLLO NORMAL',                  'Crecimiento y desarrollo acordes a percentiles para la edad',                 'Sulfato ferroso 300mg/dia / Vitamina D 400UI/dia',                                                       'Vacuna triple viral aplicada. Suplemento para crecimiento.',                             21),
('SINDROME DE INTESTINO IRRITABLE',    'SII con predominio diarreico, criterios de Roma IV',                          'Loperamida 2mg segun necesidad / Probiotico Lactobacillus 1 capsula/dia',                                 'Dieta baja en FODMAP. Evitar lactosa y gluten. Refrigerar probiotico.',                   22),
('ESGUINCE DE TOBILLO GRADO II',       'Lesion ligamentosa parcial en tobillo izquierdo',                             'Ibuprofeno 400mg cada 8h / Diclofenaco gel 1% topico',                                                   'Inmovilizacion 10 dias. Hielo local. Aplicar gel con masaje suave.',                     23),
('ACNE VULGAR GRADO 2',                'Acne inflamatorio moderado en region facial y dorsal',                        'Tretinona crema 0.025% nocturna / Doxiciclina 100mg cada 12h',                                           'Protector solar diario. Doxiciclina con abundante agua. No acostarse inmediatamente.',   24),
('DIABETES MELLITUS TIPO 2',           'DM2 descompensada con HbA1c de 9.2%',                                        'Metformina 850mg cada 12h / Insulina Glargina 10 UI nocturna',                                           'Control glucemico diario. Dieta diabetica estricta. Rotar zonas de insulina.',           25),
('LUMBALGIA OCUPACIONAL',              'Dolor lumbar secundario a postura inadecuada y sobrecarga',                   'Ciclobenzaprina 5mg nocturna / Diclofenaco 75mg cada 12h',                                               'Fisioterapia lumbar. Cambio ergonomico en trabajo. Faja de soporte.',                    26),
('VERTIGO POSICIONAL BENIGNO',         'VPPB de canal posterior derecho, test de Dix-Hallpike positivo',              'Betahistina 16mg cada 8h / Dimenhidrinato 50mg cada 8h si vertigo',                                      'Maniobra de Epley realizada. Ejercicios vestibulares. Puede causar somnolencia.',        27),
('HIPERTENSION ARTERIAL NO CONTROLADA','HTA con cifras persistentemente elevadas pese a monoterapia',                 'Amlodipino 5mg/dia / Losartan 50mg/dia',                                                                 'Dieta hiposodica estricta. Control semanal de presion. No suspender.',                   28),
('ASMA BRONQUIAL',                     'Asma intermitente en nino con FEV1 80% del predicho',                          'Salbutamol inhalador 100mcg c/4-6h en crisis / Budesonida inhalada 200mcg',                              'Max 4 inhalaciones por crisis. Enjuagar boca tras cada uso.',                            29),
('FIBRILACION AURICULAR',              'FA paroxistica en seguimiento con anticoagulacion oral',                      'Warfarina 5mg/dia / Digoxina 0.125mg/dia',                                                               'Control INR mensual. Controlar pulso antes de digoxina. Evitar AINES.',                  30);

