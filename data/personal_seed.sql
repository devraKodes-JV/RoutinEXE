-- RoutinEXE Personal Seed
-- User, exercises, and 3-month routine starting today
-- This does NOT delete existing data - it only adds

-- ============================================
-- USER
-- ============================================
INSERT INTO users (username, age, height, weight) VALUES ('JH', 25, 175.0, 70.0);

-- ============================================
-- EXERCISES (new ones for the routine)
-- ============================================

-- Mon/Wed/Fri Warm-up
INSERT INTO exercises (name, category_id, description, time_based) VALUES
    ('Rotaciones articulares', 1, 'Rotaciones articulares: 10 círculos hacia cada lado (tobillos →→ rodillas →→ cadera)', 1),
    ('Balanceo de piernas', 1, 'Balanceo de piernas: 15 repeticiones hacia adelante y atrás por pierna', 1),
    ('Caminata en el sitio', 1, 'Caminata en el sitio: 2 minutos elevando rodillas a altura moderada', 1),
    ('Puentes de glúteo dinámicos', 4, 'Puentes de glúteo dinámicos: 15 repeticiones rápidas para activar la cadena posterior', 1),
    ('Movilidad torácica', 6, 'Movilidad torácica: 10 rotaciones de tronco sentado por lado', 1);

-- Mon/Wed/Fri Strength (Circuito Blindaje de Piernas)
INSERT INTO exercises (name, category_id, description, time_based) VALUES
    ('Sentadilla Búlgara', 3, 'Sentadilla Búlgara: una pierna apoyada atrás en silla, al fallo técnico', 0),
    ('Silla Isométrica', 3, 'Silla isométrica: 60-90 seg. Espalda en pared, empuja talones al suelo', 1),
    ('Flexiones Diamante', 2, 'Flexiones diamante: manos formando un diamante, al fallo técnico', 0),
    ('Puente Glúteo 1 Pierna', 3, 'Puente glúteo 1 pierna: al fallo técnico, aprieta glúteo 3 seg arriba', 0),
    ('Zancada Isométrica', 3, 'Zancada isométrica: 45 seg por pierna, mantente a 2cm del suelo', 1),
    ('Plancha toque hombro', 5, 'Plancha toque hombro: al fallo técnico, cadera totalmente inmóvil', 0),
    ('Extensión Cuádriceps', 3, 'Extensión cuádriceps: 45 seg por pierna, pierna extendida y contraída al máximo', 1),
    ('Elevaciones de Talones', 3, 'Elevaciones de talones: al fallo técnico, pausa 2 seg arriba, bajada muy lenta', 0),
    ('Dips de Tríceps', 2, 'Dips de tríceps: al fallo técnico, en silla o sofá', 0),
    ('Superman Isométrico', 5, 'Superman isométrico: 60 seg. Brazos y piernas elevados', 1);

-- Tue/Thu Mobility
INSERT INTO exercises (name, category_id, description, time_based) VALUES
    ('Gato-Camello', 6, 'Gato-camello: 3 series de 12 repeticiones', 0),
    ('Perro boca abajo', 6, 'Perro boca abajo: mantener 30 segundos, pedaleando talones suavemente', 1),
    ('Rotaciones de Cadera 90/90', 6, 'Rotaciones de cadera 90/90: 10 repeticiones por lado', 0),
    ('Estiramiento de Psoas Dinámico', 6, 'Estiramiento de psoas dinámico: 10 repeticiones por lado', 0),
    ('Estiramiento de Isquiotibiales', 6, 'Estiramiento de isquiotibiales: 45 segundos por pierna', 1),
    ('Estiramiento de Cuádriceps', 6, 'Estiramiento de cuádriceps: 45 segundos por pierna', 1),
    ('Movilidad de Tobillos', 6, 'Movilidad de tobillos: 15 círculos hacia cada lado por pie', 1);

-- Sat Light
INSERT INTO exercises (name, category_id, description, time_based) VALUES
    ('Caminata suave', 1, 'Caminata suave o natación', 1);

-- Cool-down for Mon/Wed/Fri
INSERT INTO exercises (name, category_id, description, time_based) VALUES
    ('Caminata muy lenta', 1, 'Caminata muy lenta: 2 minutos para normalizar pulsaciones', 1),
    ('Respiración Diafragmática', 6, 'Respiración diafragmática: 3 minutos acostado boca arriba', 1);

-- ============================================
-- ROUTINE: Programa Maestro Integral 3 Meses
-- ============================================
INSERT INTO routines (user_id, name, since_date, until_date)
VALUES (1, 'Programa Maestro Integral - 3 Meses', '2026-09-23', '2026-12-23');

-- Get routine id
-- The routine id is 1 (first routine for user 1)

-- ============================================
-- ROUTINE DAYS (active days per week)
-- ============================================
-- Mon(0), Wed(2), Fri(4): Strength
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 0);
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 2);
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 4);

-- Tue(1), Thu(3): Mobility
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 1);
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 3);

-- Sat(5): Light
INSERT INTO routine_days (routine_id, day_of_week) VALUES (1, 5);

-- ============================================
-- ROUTINE EXERCISES - Strength Days (Mon/Wed/Fri)
-- ============================================
INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 0, 4, 0 FROM exercises e WHERE e.name IN (
    'Sentadilla Búlgara', 'Silla Isométrica', 'Flexiones Diamante',
    'Puente Glúteo 1 Pierna', 'Zancada Isométrica', 'Plancha toque hombro',
    'Extensión Cuádriceps', 'Elevaciones de Talones', 'Dips de Tríceps',
    'Superman Isométrico'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 2, 4, 0 FROM exercises e WHERE e.name IN (
    'Sentadilla Búlgara', 'Silla Isométrica', 'Flexiones Diamante',
    'Puente Glúteo 1 Pierna', 'Zancada Isométrica', 'Plancha toque hombro',
    'Extensión Cuádriceps', 'Elevaciones de Talones', 'Dips de Tríceps',
    'Superman Isométrico'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 4, 4, 0 FROM exercises e WHERE e.name IN (
    'Sentadilla Búlgara', 'Silla Isométrica', 'Flexiones Diamante',
    'Puente Glúteo 1 Pierna', 'Zancada Isométrica', 'Plancha toque hombro',
    'Extensión Cuádriceps', 'Elevaciones de Talones', 'Dips de Tríceps',
    'Superman Isométrico'
);

-- ============================================
-- ROUTINE EXERCISES - Mobility Days (Tue/Thu)
-- ============================================
INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 1, 3, 0 FROM exercises e WHERE e.name IN (
    'Gato-Camello', 'Perro boca abajo', 'Rotaciones de Cadera 90/90',
    'Estiramiento de Psoas Dinámico', 'Estiramiento de Isquiotibiales',
    'Estiramiento de Cuádriceps', 'Movilidad de Tobillos'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 3, 3, 0 FROM exercises e WHERE e.name IN (
    'Gato-Camello', 'Perro boca abajo', 'Rotaciones de Cadera 90/90',
    'Estiramiento de Psoas Dinámico', 'Estiramiento de Isquiotibiales',
    'Estiramiento de Cuádriceps', 'Movilidad de Tobillos'
);

-- ============================================
-- ROUTINE EXERCISES - Saturday (Light)
-- ============================================
INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 5, 1, 20 FROM exercises e WHERE e.name = 'Caminata suave';

-- ============================================
-- ROUTINE EXERCISES - Warm-up (Mon/Wed/Fri)
-- ============================================
INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 0, 1, 1 FROM exercises e WHERE e.name IN (
    'Rotaciones articulares', 'Balanceo de piernas', 'Caminata en el sitio',
    'Puentes de glúteo dinámicos', 'Movilidad torácica'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 2, 1, 1 FROM exercises e WHERE e.name IN (
    'Rotaciones articulares', 'Balanceo de piernas', 'Caminata en el sitio',
    'Puentes de glúteo dinámicos', 'Movilidad torácica'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 4, 1, 1 FROM exercises e WHERE e.name IN (
    'Rotaciones articulares', 'Balanceo de piernas', 'Caminata en el sitio',
    'Puentes de glúteo dinámicos', 'Movilidad torácica'
);

-- ============================================
-- ROUTINE EXERCISES - Cool-down (Mon/Wed/Fri)
-- ============================================
INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 0, 1, 1 FROM exercises e WHERE e.name IN (
    'Caminata muy lenta', 'Respiración Diafragmática'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 2, 1, 1 FROM exercises e WHERE e.name IN (
    'Caminata muy lenta', 'Respiración Diafragmática'
);

INSERT INTO routine_exercises (routine_id, exercise_id, day_of_week, sets, reps)
SELECT 1, e.id, 4, 1, 1 FROM exercises e WHERE e.name IN (
    'Caminata muy lenta', 'Respiración Diafragmática'
);
