-- ============================================================================
-- Script de Poblado Robusto para RateMyProfs
-- Versión: 2.0
-- Descripción: Datos de prueba completos y realistas para testing exhaustivo
-- ============================================================================

-- Limpiar datos existentes (opcional, descomentar si es necesario)
-- TRUNCATE TABLE ReviewVotes, Reviews, ProfessorSubjects, ProfessorUniversities, 
-- Users, Professors, Subjects, Universities RESTART IDENTITY CASCADE;

-- ============================================================================
-- 1. UNIVERSIDADES (Más instituciones para pruebas)
-- ============================================================================
INSERT INTO Universities (Name) VALUES 
('FIME UANL'),
('FCFM UANL'),
('FIC UANL'),
('FACPYA UANL'),
('FARQ UANL'),
('FASPYN UANL'),
('FCB UANL'),
('FIC UANL')
ON CONFLICT (Name) DO NOTHING;

-- ============================================================================
-- 2. MATERIAS (Más específicas y organizadas por departamento)
-- ============================================================================
INSERT INTO Subjects (Name, DepartmentName) VALUES 
-- Computación e Informática
('Programación Orientada a Objetos', 'Ingeniería en Computación'),
('Estructuras de Datos', 'Ingeniería en Computación'),
('Bases de Datos', 'Ingeniería en Computación'),
('Desarrollo Web', 'Ingeniería en Computación'),
('Algoritmos Avanzados', 'Ingeniería en Computación'),
('Sistemas Operativos', 'Ingeniería en Computación'),
('Compiladores', 'Ingeniería en Computación'),
('Inteligencia Artificial', 'Ingeniería en Computación'),
('Machine Learning', 'Ingeniería en Computación'),
('Arquitectura de Computadoras', 'Ingeniería en Computación'),
('Redes de Computadoras', 'Ingeniería en Computación'),
('Seguridad Informática', 'Ingeniería en Computación'),

-- Matemáticas
('Cálculo Diferencial', 'Matemáticas'),
('Cálculo Integral', 'Matemáticas'),
('Álgebra Lineal', 'Matemáticas'),
('Ecuaciones Diferenciales', 'Matemáticas'),
('Probabilidad y Estadística', 'Matemáticas'),
('Matemáticas Discretas', 'Matemáticas'),

-- Física
('Física I', 'Física'),
('Física II', 'Física'),
('Mecánica Clásica', 'Física'),
('Electromagnetismo', 'Física'),

-- Química
('Química General', 'Química'),
('Química Orgánica', 'Química'),

-- Ingenierías Específicas
('Termodinámica', 'Ingeniería Mecánica'),
('Mecánica de Fluidos', 'Ingeniería Mecánica'),
('Circuitos Eléctricos', 'Ingeniería Eléctrica'),
('Electrónica Digital', 'Ingeniería Electrónica'),
('Electrónica Analógica', 'Ingeniería Electrónica'),
('Control Automático', 'Ingeniería Mecatrónica'),

-- Humanidades y Negocios
('Administración de Proyectos', 'Administración'),
('Contabilidad Financiera', 'Contaduría'),
('Ética Profesional', 'Humanidades'),
('Comunicación Oral y Escrita', 'Humanidades');

-- ============================================================================
-- 3. PROFESORES (Más variedad para diferentes escenarios de prueba)
-- ============================================================================
INSERT INTO Professors (Name, Title, DepartmentName, PhotoURL) VALUES 
-- Profesores de Computación (excelentes ratings esperados)
('Dr. Juan Carlos Rodríguez', 'Dr.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=12'),
('Dra. María Elena García', 'Dra.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=5'),
('M.C. Carlos Alberto Sánchez', 'M.C.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=14'),
('Dr. Roberto Chen', 'Dr.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=68'),
('Ing. Diana Morales', 'Ing.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=47'),

-- Profesores de Matemáticas (ratings variados)
('Dr. Luis Fernando López', 'Dr.', 'Matemáticas', 'https://i.pravatar.cc/150?img=33'),
('Dra. Sofía Valdez', 'Dra.', 'Matemáticas', 'https://i.pravatar.cc/150?img=32'),
('M.C. Alberto Ramírez', 'M.C.', 'Matemáticas', 'https://i.pravatar.cc/150?img=56'),

-- Profesores de Física
('Dra. Ana Patricia Hernández', 'Dra.', 'Física', 'https://i.pravatar.cc/150?img=45'),
('Dr. Fernando Gutiérrez', 'Dr.', 'Física', 'https://i.pravatar.cc/150?img=70'),

-- Profesores de otras áreas
('Dr. José Luis Pérez', 'Dr.', 'Química', 'https://i.pravatar.cc/150?img=23'),
('Ing. Roberto Martínez', 'Ing.', 'Ingeniería Mecánica', 'https://i.pravatar.cc/150?img=51'),
('Dr. Miguel Ángel Ramírez', 'Dr.', 'Ingeniería Eléctrica', 'https://i.pravatar.cc/150?img=60'),
('Ing. Laura Gabriela Torres', 'Ing.', 'Ingeniería Electrónica', 'https://i.pravatar.cc/150?img=28'),

-- Profesores con pocos ratings (para pruebas de casos edge)
('Dr. Alejandro Moreno', 'Dr.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=15'),
('Dra. Claudia Ramírez', 'Dra.', 'Matemáticas', 'https://i.pravatar.cc/150?img=20'),

-- Profesores de Humanidades/Administración
('Lic. Patricia Sánchez', 'Lic.', 'Administración', 'https://i.pravatar.cc/150?img=38'),
('Mtro. Jorge Hernández', 'Mtro.', 'Humanidades', 'https://i.pravatar.cc/150?img=42'),

-- Profesores sin reseñas (para pruebas de nuevos profesores)
('Dr. Eduardo Salinas', 'Dr.', 'Ingeniería en Computación', 'https://i.pravatar.cc/150?img=65'),
('Dra. Valeria Mendoza', 'Dra.', 'Matemáticas', 'https://i.pravatar.cc/150?img=25');

-- ============================================================================
-- 4. RELACIÓN PROFESORES-UNIVERSIDADES
-- ============================================================================
INSERT INTO ProfessorUniversities (ProfessorID, UniversityID) VALUES 
-- Dr. Juan Carlos Rodríguez (multi-universidad)
(1, 1), (1, 3),
-- Dra. María Elena García
(2, 1),
-- M.C. Carlos Alberto Sánchez (multi-universidad)
(3, 1), (3, 2),
-- Dr. Roberto Chen
(4, 1),
-- Ing. Diana Morales
(5, 1), (5, 3),
-- Dr. Luis Fernando López
(6, 1), (6, 2),
-- Dra. Sofía Valdez
(7, 2), (7, 1),
-- M.C. Alberto Ramírez
(8, 1),
-- Dra. Ana Patricia Hernández
(9, 1),
-- Dr. Fernando Gutiérrez
(10, 1), (10, 2),
-- Dr. José Luis Pérez
(11, 1), (11, 2),
-- Ing. Roberto Martínez
(12, 1),
-- Dr. Miguel Ángel Ramírez
(13, 1), (13, 3),
-- Ing. Laura Gabriela Torres
(14, 1),
-- Dr. Alejandro Moreno
(15, 1),
-- Dra. Claudia Ramírez
(16, 2),
-- Lic. Patricia Sánchez
(17, 4),
-- Mtro. Jorge Hernández
(18, 1), (18, 4),
-- Dr. Eduardo Salinas (sin asignar aún)
(19, 1),
-- Dra. Valeria Mendoza (sin asignar aún)
(20, 2);

-- ============================================================================
-- 5. RELACIÓN PROFESORES-MATERIAS (Más realista)
-- ============================================================================
INSERT INTO ProfessorSubjects (ProfessorID, SubjectID) VALUES 
-- Dr. Juan Carlos Rodríguez - Experto en POO, Estructuras y SO
(1, 1), (1, 2), (1, 6),
-- Dra. María Elena García - BD, Web, IA
(2, 3), (2, 4), (2, 8),
-- M.C. Carlos Alberto Sánchez - Web, SO, Algoritmos
(3, 4), (3, 6), (3, 5),
-- Dr. Roberto Chen - IA, ML, Algoritmos
(4, 8), (4, 9), (4, 5),
-- Ing. Diana Morales - Arquitectura, Redes, Seguridad
(5, 10), (5, 11), (5, 12),
-- Dr. Luis Fernando López - Cálculos y Álgebra
(6, 13), (6, 14), (6, 15),
-- Dra. Sofía Valdez - Ecuaciones, Probabilidad, Matemáticas Discretas
(7, 16), (7, 17), (7, 18),
-- M.C. Alberto Ramírez - Álgebra, Matemáticas Discretas
(8, 15), (8, 18),
-- Dra. Ana Patricia Hernández - Física I y II
(9, 19), (9, 20),
-- Dr. Fernando Gutiérrez - Mecánica y Electromagnetismo
(10, 21), (10, 22),
-- Dr. José Luis Pérez - Química
(11, 23), (11, 24),
-- Ing. Roberto Martínez - Termodinámica, Fluidos
(12, 25), (12, 26),
-- Dr. Miguel Ángel Ramírez - Circuitos, Control
(13, 27), (13, 30),
-- Ing. Laura Gabriela Torres - Electrónica
(14, 28), (14, 29),
-- Dr. Alejandro Moreno - Compiladores
(15, 7),
-- Dra. Claudia Ramírez - Probabilidad
(16, 17),
-- Lic. Patricia Sánchez - Administración
(17, 31),
-- Mtro. Jorge Hernández - Humanidades
(18, 33), (18, 34),
-- Dr. Eduardo Salinas - IA (sin reseñas aún)
(19, 8),
-- Dra. Valeria Mendoza - Cálculo (sin reseñas aún)
(20, 13);

-- ============================================================================
-- 6. USUARIOS (Más variedad para pruebas de autenticación y roles)
-- ============================================================================
-- NOTA: Todas las contraseñas son "password123" hasheadas con BCrypt
-- Hash: $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru

INSERT INTO Users (Name, Email, PasswordHash, RoleID) VALUES 
-- Administradores (RoleID = 1)
('Admin Sistema', 'admin@ratemyprofs.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 1),
('Super Admin', 'superadmin@ratemyprofs.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 1),

-- Usuarios Regulares (RoleID = 2) - Activos y participativos
('María González', 'maria@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Pedro Ramírez', 'pedro@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Laura Martínez', 'laura@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Carlos Hernández', 'carlos@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Ana López', 'ana@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Diego Torres', 'diego@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Sofía Vargas', 'sofia@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Roberto Sánchez', 'roberto@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Daniela Moreno', 'daniela@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),
('Fernando Ruiz', 'fernando@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2),

-- Usuario de prueba específico
('Test User', 'test@example.com', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36A1Z9h3bvVwTdGT6.M5Eru', 2);

-- ============================================================================
-- 7. RESEÑAS (Variedad de casos: aprobadas, pendientes, rechazadas)
-- ============================================================================
INSERT INTO Reviews (UserID, ProfessorID, SubjectID, Rating, Comment, IsAnonymous, StatusID) VALUES 
-- Reseñas APROBADAS (StatusID = 2) - Ratings altos (4-5 estrellas)
(3, 1, 1, 5, 'Excelente profesor, explica muy bien los conceptos de POO. Sus clases son dinámicas y siempre está dispuesto a ayudar. Los proyectos son retadores pero aprendes muchísimo.', false, 2),
(4, 1, 2, 5, 'El mejor profesor de estructuras de datos. Hace que temas complejos sean fáciles de entender. Muy recomendado.', false, 2),
(5, 1, 6, 4, 'Buen profesor de sistemas operativos, aunque a veces va muy rápido. Se nota que domina el tema completamente.', false, 2),

(3, 2, 3, 5, 'La mejor profesora de bases de datos que he tenido. Sus ejemplos prácticos son muy útiles y las explicaciones son claras.', false, 2),
(4, 2, 4, 5, 'Increíble profesora de desarrollo web. Enseña tecnologías actuales y sus proyectos son del mundo real.', false, 2),
(6, 2, 8, 5, 'Sus clases de IA son fascinantes. Explica desde lo básico hasta conceptos avanzados de manera comprensible.', false, 2),

(5, 3, 4, 4, 'Muy actualizado en tecnologías web modernas. Sus clases son prácticas y útiles.', false, 2),
(7, 3, 5, 4, 'Buenos proyectos en algoritmos avanzados. A veces los exámenes son muy difíciles pero justos.', false, 2),

(4, 4, 8, 5, 'Excelente profesor de IA. Sus explicaciones son claras y los proyectos te preparan para el mundo real.', false, 2),
(6, 4, 9, 5, 'El curso de Machine Learning con él es increíble. Vale totalmente la pena el esfuerzo.', false, 2),

-- Reseñas con ratings medios (3 estrellas)
(5, 6, 13, 3, 'Explica cálculo de manera clara, aunque deja mucha tarea. A veces se salta pasos importantes.', false, 2),
(8, 8, 15, 3, 'Sabe del tema pero sus clases pueden ser un poco monótonas. Los exámenes son justos.', true, 2),
(7, 12, 25, 3, 'Regular. Conoce bien termodinámica pero no siempre explica claramente. Hay que estudiar mucho por tu cuenta.', false, 2),

-- Reseñas con ratings altos en otras áreas
(3, 6, 14, 5, 'El mejor profesor de cálculo integral que he tenido. Paciente y muy claro en sus explicaciones.', false, 2),
(6, 7, 16, 4, 'Muy buena profesora de ecuaciones diferenciales. Sus métodos de enseñanza son efectivos.', false, 2),
(8, 7, 17, 5, 'Excelente en probabilidad y estadística. Hace que la materia sea interesante y aplicable.', false, 2),

(4, 9, 19, 5, 'Hace que la física sea interesante y fácil de entender. Sus experimentos en clase son geniales.', false, 2),
(5, 9, 20, 4, 'Buena profesora de Física II. A veces los laboratorios son complicados pero se aprende mucho.', false, 2),

(6, 10, 21, 5, 'Excelente en mecánica clásica. Sus clases son muy bien estructuradas y los ejemplos son claros.', false, 2),
(9, 10, 22, 4, 'Buen profesor de electromagnetismo. Los problemas son retadores pero te preparan bien para los exámenes.', false, 2),

(7, 13, 27, 5, 'Excelente para entender circuitos eléctricos. Muy paciente y siempre dispuesto a ayudar.', false, 2),
(10, 13, 30, 4, 'Buen profesor de control automático. Sus proyectos son interesantes y prácticos.', false, 2),

(8, 14, 28, 4, 'Buena profesora de electrónica digital. Sus laboratorios son desafiantes pero educativos.', false, 2),

-- Reseñas anónimas (para probar funcionalidad de anonimato)
(3, 11, 23, 4, 'Buen profesor de química, aunque sus clases pueden ser un poco aburridas a veces.', true, 2),
(9, 15, 7, 3, 'El contenido de compiladores es bueno pero siento que podría explicar mejor algunos temas avanzados.', true, 2),
(10, 17, 31, 4, 'Buena profesora de administración. Sus casos prácticos son muy útiles para entender la teoría.', true, 2),

-- Reseñas PENDIENTES de aprobación (StatusID = 1) - Para pruebas de moderación
(11, 1, 1, 5, 'Acabo de terminar el semestre con el Dr. Rodríguez y fue una experiencia increíble. Aprendí más de lo que esperaba.', false, 1),
(11, 2, 3, 4, 'Muy buena profesora, aunque sus exámenes son un poco difíciles. En general recomendada.', false, 1),
(3, 5, 11, 5, 'Excelente profesora de redes. Sus ejemplos prácticos hacen que todo sea más fácil de entender.', false, 1),
(4, 4, 5, 4, 'Buen profesor de algoritmos. Los ejercicios son retadores y te hacen pensar.', false, 1),

-- Reseñas RECHAZADAS (StatusID = 3) - Para pruebas de moderación
(12, 8, 15, 1, 'Pésimo profesor, no explica nada bien.', false, 3),
(12, 12, 25, 2, 'No me gustó su clase, muy aburrida.', false, 3);

-- ============================================================================
-- 8. VOTOS EN RESEÑAS (Para probar sistema de likes/dislikes)
-- ============================================================================
INSERT INTO ReviewVotes (UserID, ReviewID, VoteTypeID) VALUES 
-- Likes (VoteTypeID = 1) en las primeras reseñas
(3, 1, 1), (4, 1, 1), (5, 1, 1), (6, 1, 1), (7, 1, 1),  -- 5 likes en reseña 1
(3, 2, 1), (5, 2, 1), (6, 2, 1), (8, 2, 1),              -- 4 likes en reseña 2
(4, 3, 1), (5, 3, 1), (7, 3, 1),                         -- 3 likes en reseña 3
(3, 4, 1), (4, 4, 1), (6, 4, 1), (7, 4, 1), (9, 4, 1),   -- 5 likes en reseña 4
(5, 5, 1), (6, 5, 1), (8, 5, 1), (9, 5, 1),              -- 4 likes en reseña 5
(3, 6, 1), (7, 6, 1), (10, 6, 1),                        -- 3 likes en reseña 6
(4, 7, 1), (8, 7, 1),                                    -- 2 likes en reseña 7
(6, 8, 1), (9, 8, 1), (10, 8, 1),                        -- 3 likes en reseña 8
(3, 9, 1), (5, 9, 1), (7, 9, 1), (8, 9, 1),              -- 4 likes en reseña 9
(4, 10, 1), (6, 10, 1),                                  -- 2 likes en reseña 10

-- Dislikes (VoteTypeID = 2) en reseñas con ratings bajos
(7, 11, 2), (9, 11, 2),                                  -- 2 dislikes en reseña 11
(8, 13, 2),                                              -- 1 dislike en reseña 13
(10, 15, 2), (11, 15, 2),                                -- 2 dislikes en reseña 15

-- Votos mixtos
(11, 14, 1), (12, 14, 1),                                -- Likes en reseña 14
(10, 16, 1), (11, 16, 1), (12, 16, 1);                   -- Likes en reseña 16

-- ============================================================================
-- 9. VERIFICACIÓN Y ESTADÍSTICAS
-- ============================================================================
SELECT '============================================================' as "";
SELECT '          RESUMEN DE DATOS INSERTADOS                      ' as "";
SELECT '============================================================' as "";

SELECT 'Universidades insertadas:' as Categoría, COUNT(*) as Total FROM Universities
UNION ALL
SELECT 'Materias insertadas:', COUNT(*) FROM Subjects
UNION ALL
SELECT 'Profesores insertados:', COUNT(*) FROM Professors
UNION ALL
SELECT 'Usuarios insertados:', COUNT(*) FROM Users
UNION ALL
SELECT 'Reseñas insertadas:', COUNT(*) FROM Reviews
UNION ALL
SELECT 'Votos insertados:', COUNT(*) FROM ReviewVotes
UNION ALL
SELECT '------------------------------------------------------------' as "", '' as "";

-- Estadísticas por estado de reseñas
SELECT '          ESTADÍSTICAS DE RESEÑAS POR ESTADO               ' as "";
SELECT '============================================================' as "";

SELECT 
    rs.StatusName as Estado,
    COUNT(*) as Cantidad,
    ROUND(AVG(r.Rating)::numeric, 2) as "Rating Promedio"
FROM Reviews r
JOIN ReviewStatus rs ON r.StatusID = rs.StatusID
GROUP BY rs.StatusName, rs.StatusID
ORDER BY rs.StatusID;

SELECT '------------------------------------------------------------' as "", '' as "";

-- Top 5 profesores con más reseñas
SELECT '          TOP 5 PROFESORES CON MÁS RESEÑAS                 ' as "";
SELECT '============================================================' as "";

SELECT 
    p.Name as Profesor,
    p.DepartmentName as Departamento,
    COUNT(r.ReviewID) as "Total Reseñas",
    ROUND(AVG(r.Rating)::numeric, 2) as "Rating Promedio"
FROM Professors p
LEFT JOIN Reviews r ON p.ProfessorID = r.ProfessorID
WHERE r.StatusID = 2  -- Solo aprobadas
GROUP BY p.ProfessorID, p.Name, p.DepartmentName
HAVING COUNT(r.ReviewID) > 0
ORDER BY COUNT(r.ReviewID) DESC, AVG(r.Rating) DESC
LIMIT 5;

SELECT '------------------------------------------------------------' as "", '' as "";
SELECT 'Datos de prueba cargados exitosamente!' as "";
SELECT 'Contraseña para todos los usuarios: password123' as "";
SELECT '============================================================' as "";
