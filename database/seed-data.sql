-- Script para poblar la base de datos con datos de prueba

-- Insertar Universidades
INSERT INTO Universities (Name) VALUES 
('FIME UANL'),
('FCFM UANL'),
('FIC UANL'),
('FACPYA UANL'),
('FARQ UANL')
ON CONFLICT (Name) DO NOTHING;

-- Insertar Materias
INSERT INTO Subjects (Name, DepartmentName) VALUES 
('Programación Orientada a Objetos', 'Ingeniería en Computación'),
('Estructuras de Datos', 'Ingeniería en Computación'),
('Bases de Datos', 'Ingeniería en Computación'),
('Desarrollo Web', 'Ingeniería en Computación'),
('Algoritmos Avanzados', 'Ingeniería en Computación'),
('Cálculo Diferencial', 'Matemáticas'),
('Cálculo Integral', 'Matemáticas'),
('Álgebra Lineal', 'Matemáticas'),
('Física I', 'Física'),
('Física II', 'Física'),
('Química General', 'Química'),
('Termodinámica', 'Ingeniería Mecánica'),
('Circuitos Eléctricos', 'Ingeniería Eléctrica'),
('Electrónica Digital', 'Ingeniería Electrónica'),
('Sistemas Operativos', 'Ingeniería en Computación');

-- Insertar Profesores
INSERT INTO Professors (Name, Title, DepartmentName, PhotoURL) VALUES 
('Dr. Juan Carlos Rodríguez', 'Dr.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=12'),
('Dra. María Elena García', 'Dra.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=5'),
('Ing. Roberto Martínez', 'Ing.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=33'),
('Dr. Luis Fernando López', 'Dr.', 'Matemáticas', 'https://i.pravatar.cc/150?img=68'),
('Dra. Ana Patricia Hernández', 'Dra.', 'Física', 'https://i.pravatar.cc/150?img=45'),
('M.C. Carlos Alberto Sánchez', 'M.C.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=14'),
('Dr. José Luis Pérez', 'Dr.', 'Química', 'https://i.pravatar.cc/150?img=56'),
('Ing. Laura Gabriela Torres', 'Ing.', 'Ingeniería Mecánica', 'https://i.pravatar.cc/150?img=47'),
('Dr. Miguel Ángel Ramírez', 'Dr.', 'Ingeniería Eléctrica', 'https://i.pravatar.cc/150?img=60'),
('Dra. Sofía Valdez', 'Dra.', 'Matemáticas', 'https://i.pravatar.cc/150?img=32');

-- Relacionar Profesores con Universidades
INSERT INTO ProfessorUniversities (ProfessorID, UniversityID) VALUES 
(1, 1), (1, 3),
(2, 1),
(3, 1), (3, 2),
(4, 1), (4, 2),
(5, 1),
(6, 1),
(7, 1), (7, 2),
(8, 1),
(9, 1), (9, 3),
(10, 2), (10, 1);

-- Relacionar Profesores con Materias
INSERT INTO ProfessorSubjects (ProfessorID, SubjectID) VALUES 
(1, 1), (1, 2), (1, 15),
(2, 3), (2, 4),
(3, 2), (3, 5),
(4, 6), (4, 7), (4, 8),
(5, 9), (5, 10),
(6, 4), (6, 15),
(7, 11),
(8, 12),
(9, 13), (9, 14),
(10, 6), (10, 8);

-- Insertar algunos usuarios de prueba (sin incluir a gino, ese lo crearemos con el endpoint)
-- Las contraseñas deben estar hasheadas con BCrypt
-- Contraseña de ejemplo: "password123" -> $2a$10$rqWJ5K9xK5K5K5K5K5K5K5K5K5K5K5K5K5K5K5K5K5K5K5K
INSERT INTO Users (Name, Email, PasswordHash, RoleID) VALUES 
('María González', 'maria@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Pedro Ramírez', 'pedro@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Laura Martínez', 'laura@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Admin Sistema', 'admin@ratemyprofs.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 1);

-- Insertar Reseñas de ejemplo
INSERT INTO Reviews (UserID, ProfessorID, SubjectID, Rating, Comment, IsAnonymous, StatusID) VALUES 
(1, 1, 1, 5, 'Excelente profesor, explica muy bien los conceptos de POO. Sus clases son dinámicas y siempre está dispuesto a ayudar.', false, 2),
(2, 1, 2, 4, 'Buen profesor, aunque a veces va muy rápido. Se nota que domina el tema.', false, 2),
(3, 2, 3, 5, 'La mejor profesora de bases de datos. Sus ejemplos prácticos son muy útiles.', false, 2),
(1, 2, 4, 5, 'Increíble profesora, aprendo mucho en sus clases de desarrollo web.', false, 2),
(2, 3, 2, 3, 'Es buen profesor pero sus exámenes son muy difíciles.', false, 2),
(3, 4, 6, 4, 'Explica cálculo de manera clara, aunque deja mucha tarea.', false, 2),
(1, 4, 7, 5, 'El mejor profesor de cálculo integral que he tenido.', false, 2),
(2, 5, 9, 5, 'Hace que la física sea interesante y fácil de entender.', false, 2),
(3, 6, 4, 4, 'Muy actualizado en tecnologías web modernas.', false, 2),
(1, 7, 11, 3, 'Sabe mucho pero sus clases son un poco aburridas.', true, 2),
(2, 8, 12, 4, 'Buena profesora, sus proyectos son retadores.', false, 2),
(3, 9, 13, 5, 'Excelente para entender circuitos eléctricos.', false, 2),
(1, 10, 6, 4, 'Muy paciente al explicar, recomendada.', false, 2);

-- Insertar algunos votos en las reseñas
INSERT INTO ReviewVotes (UserID, ReviewID, VoteTypeID) VALUES 
(1, 2, 1), (1, 3, 1), (1, 4, 1),
(2, 1, 1), (2, 3, 1), (2, 5, 1),
(3, 1, 1), (3, 2, 1), (3, 4, 1), (3, 8, 1),
(1, 5, 2), (2, 10, 2);

-- Verificación
SELECT 'Universidades insertadas:' as info, COUNT(*) as total FROM Universities;
SELECT 'Materias insertadas:' as info, COUNT(*) as total FROM Subjects;
SELECT 'Profesores insertados:' as info, COUNT(*) as total FROM Professors;
SELECT 'Usuarios insertados:' as info, COUNT(*) as total FROM Users;
SELECT 'Reseñas insertadas:' as info, COUNT(*) as total FROM Reviews;
