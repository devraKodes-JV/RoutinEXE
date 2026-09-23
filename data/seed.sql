-- RoutinEXE Database Seed (reset & reload)
-- 6 categories, 25 exercises each (150 total)
-- Mix of equipment-based and bodyweight exercises
-- time_based = 1 means measured by duration (cardio, holds, stretches)
-- time_based = 0 means measured by sets/reps
-- Run this against an empty database, or run to reset all data.

DELETE FROM session_exercises;
DELETE FROM sessions;
DELETE FROM routine_exercises;
DELETE FROM routine_days;
DELETE FROM routines;
DELETE FROM exercises;
DELETE FROM categories;
DELETE FROM users;

-- ============================================
-- CATEGORIES
-- ============================================
INSERT INTO categories (name) VALUES ('Cardio & Endurance');
INSERT INTO categories (name) VALUES ('Upper Body Strength');
INSERT INTO categories (name) VALUES ('Lower Body Strength');
INSERT INTO categories (name) VALUES ('Full Body Strength');
INSERT INTO categories (name) VALUES ('Core & Abs');
INSERT INTO categories (name) VALUES ('Flexibility & Mobility');

-- ============================================
-- CARDIO & ENDURANCE (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Jumping Jacks', 1, 'Full-body warm-up exercise with rhythmic arm and leg movements', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('High Knees', 1, 'Running in place lifting knees to hip height', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Burpees', 1, ' explosive full-body movement combining squat, push-up, and jump', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Mountain Climbers', 1, 'Core exercise with alternating knee drives in plank position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Running in Place', 1, 'Light cardio exercise mimicking outdoor running', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Shadow Boxing', 1, 'Punching combinations in the air for cardio training', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Tuck Jumps', 1, 'Jumping with knees pulled to chest mid-air', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Squat Jumps', 1, 'Explosive jump from a squat position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Lateral Shuffles', 1, 'Side-to-side quick foot movement', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Skater Jumps', 1, 'Lateral jumps mimicking ice skating motion', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Carioca', 1, 'Grapevine step with crossover leg movement', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bear Crawl', 1, 'Crawling on hands and feet with knees off ground', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Crab Walk', 1, 'Walking forward with belly facing up supported by hands and feet', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Speed Skaters', 1, 'Rapid lateral jumping with single-leg landing', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Frog Jumps', 1, 'Vertical jumps from deep squat with explosive power', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Inchworm', 1, 'Walking hands out to plank and walking feet to hands', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Plank Jack', 1, 'Jumping feet in and out while in plank position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Treadmill Run', 1, 'Running on treadmill at adjustable speed and incline', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Stationary Bike', 1, 'Cycling on a stationary bike at variable resistance', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Rowing Machine', 1, 'Full-body rowing motion on an ergometer', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Elliptical Trainer', 1, 'Low-impact gliding cardio on an elliptical machine', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Stair Climber', 1, 'Continuous step climbing on a stair machine', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Jump Rope', 1, 'Jumping over a rotating rope for cardio training', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Step-Up Cardio', 1, 'Alternating stepping onto a platform with rhythmic pace', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Swimming Laps', 1, 'Swimming continuous laps in a pool', 1);

-- ============================================
-- UPPER BODY STRENGTH (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Push-Up', 2, 'Standard push-up with hands shoulder-width apart', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Diamond Push-Up', 2, 'Push-up with hands together forming a diamond shape', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Wide Push-Up', 2, 'Push-up with hands wider than shoulder width', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Decline Push-Up', 2, 'Push-up with feet elevated on a surface', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Incline Push-Up', 2, 'Push-up with hands on an elevated surface', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Handstand Push-Up', 2, 'Push-up performed in a handstand position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Tricep Dip', 2, 'Dipping body down using arms on a parallel surface', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Pike Push-Up', 2, 'Push-up with hips raised in an inverted V shape targeting shoulders', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hindu Push-Up', 2, 'Push-up with a sweeping arc motion engaging full body', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Walking Push-Up', 2, 'Push-up alternating hand walks between reps', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Bench Press', 2, 'Lying flat pressing dumbbells upward from chest', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Fly', 2, 'Lying flat opening arms wide with dumbbells', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Row', 2, 'Rowing dumbbells to hip in a bent-over position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Curl', 2, 'Bicep curl with dumbbells', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Lateral Raise', 2, 'Raising dumbbells sideways to shoulder height', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Shoulder Press', 2, 'Pressing dumbbells overhead from shoulder level', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Tricep Extension', 2, 'Extending dumbbells overhead while lowering behind head', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Hammer Curl', 2, 'Curl with neutral grip like a hammer', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Bench Press', 2, 'Pressing a barbell upward from chest while lying flat', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Row', 2, 'Rowing a barbell to the chest in a bent-over stance', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Curl', 2, 'Bicep curl with a barbell', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Shoulder Press', 2, 'Pressing a barbell overhead from shoulder level', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Tricep Extension', 2, 'Skull crushers with a barbell', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cable Fly', 2, 'Pullover motion on cable machine targeting chest', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Lat Pulldown', 2, 'Pulling a bar down from overhead on a cable machine', 0);

-- ============================================
-- LOWER BODY STRENGTH (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bodyweight Squat', 3, 'Basic squat with feet shoulder-width apart', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Wall Sit', 3, 'Holding a seated position against a wall', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Calf Raise', 3, 'Rising onto toes from a standing position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Forward Lunge', 3, 'Stepping forward into a deep lunge', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Reverse Lunge', 3, 'Stepping backward into a lunge', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bulgarian Split Squat', 3, 'Single-leg squat with rear foot elevated on a bench', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Sumo Squat', 3, 'Wide-stance squat with toes pointed outward', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Single-Leg Squat', 3, 'Squat on one leg with the other extended forward', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Step-Up', 3, 'Stepping up onto a platform and stepping back down', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Glute Bridge', 3, 'Lifting hips by squeezing glutes while lying on back', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hip Thrust', 3, 'Pressing hips forward with shoulders on a bench', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Donkey Kick', 3, 'Kicking one leg upward while on all fours', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Fire Hydrant', 3, 'Lifting leg to the side in a standing position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Side Leg Raise', 3, 'Raising leg sideways while standing or lying', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Curtsy Lunge', 3, 'Lunge diagonally behind like a curtsy', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Box Jump', 3, 'Jumping onto a box or platform', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Squat', 3, 'Back squat with a loaded barbell', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Squat', 3, 'Squat holding dumbbells at sides or goblet style', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Leg Press', 3, 'Pushing a weighted platform away on a leg press machine', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Romanian Deadlift', 3, 'Deadlift with knees slightly bent targeting hamstrings', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Leg Curl', 3, 'Bending knees against resistance on a curl machine', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Leg Extension', 3, 'Extending knees against resistance on a machine', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hip Abduction Machine', 3, 'Pushing legs apart against resistance', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hip Adduction Machine', 3, 'Bringing legs together against resistance', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Calf Raise Machine', 3, 'Raising heels against resistance on a calf raise machine', 0);

-- ============================================
-- FULL BODY STRENGTH (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Burpee', 4, 'Full-body explosive movement from squat to jump', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Thrusters', 4, 'Squat to overhead press combining two movements', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Man-Maker', 4, 'Combination of push-up, row, and thruster', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Plank to Push-Up', 4, 'Transitioning from plank to push-up and back', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Inchworm Push-Up', 4, 'Inchworm walkout into a push-up', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bear Crawl Push-Up', 4, 'Bear crawl into a push-up position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Walking Lunge', 4, 'Forward lunges while walking forward', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Jumping Lunges', 4, 'Alternating lunges with explosive jumps', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Push-Up to Row', 4, 'Push-up followed by a dumbbell row each rep', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bodyweight Squat to Press', 4, 'Squat with imaginary overhead press at the top', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Deadlift', 4, 'Lifting a loaded barbell from the ground to standing', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Barbell Squat to Press', 4, 'Front squat transitioning to overhead press', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Thrusters', 4, 'Squat to dumbbell press at the top', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dumbbell Deadlift', 4, 'Deadlift with dumbbells', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Kettlebell Swing', 4, 'Swinging kettlebell between legs to shoulder height', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Kettlebell Goblet Squat', 4, 'Squat holding kettlebell at chest', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Kettlebell Snatch', 4, 'Lifting kettlebell overhead in one fluid motion', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Kettlebell Clean', 4, 'Pulling kettlebell to rack position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Medicine Ball Slam', 4, 'Slamming a medicine ball into the ground explosively', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Medicine Ball Push-Up', 4, 'Push-up with hands on medicine ball', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Sandbag Carry', 4, 'Carrying a loaded sandbag for distance or time', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Battle Rope Slam', 4, 'Slamming battle ropes alternately or simultaneously', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cable Row to Press', 4, 'Seated cable row transitioning to overhead press', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Landmine Press', 4, 'Pressing a barbell loaded in a landmine attachment', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Sled Push', 4, 'Pushing a weighted sled forward', 1);

-- ============================================
-- CORE & ABS (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Plank', 5, 'Holding a rigid plank position on forearms and toes', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Side Plank', 5, 'Holding a plank on one forearm with body sideways', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Crunches', 5, 'Lifting shoulders off the floor from a lying position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Reverse Crunches', 5, 'Lifting hips off the floor using lower abs', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Leg Raises', 5, 'Lifting straight legs from a lying position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bicycle Crunches', 5, 'Alternating elbow to opposite knee in a cycling motion', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Russian Twist', 5, 'Twisting torso side to side with or without weight', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('V-Up', 5, 'Lifting legs and torso simultaneously to form a V', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Flutter Kick', 5, 'Alternating up and down leg kicks while lying on back', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Scissors', 5, 'Crossing legs over each other while kicking up and down', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Dead Bug', 5, 'Extending opposite arm and leg while maintaining lower back contact', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hollow Hold', 5, 'Holding arms and legs off the ground in a curved position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Superman', 5, 'Lifting arms and legs simultaneously from a prone position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Bird Dog', 5, 'Extending opposite arm and leg from a tabletop position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Mountain Climber', 5, 'Alternating knee drives in a plank position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Plank Hip Dip', 5, 'Dipping hips side to side in a plank position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Plank Shoulder Tap', 5, 'Tapping shoulders alternately in a plank position', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Inchworm', 5, 'Walking hands out and feet in with engaging core', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Starfish Crunch', 5, 'Spreading arms and legs wide then crunching together', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Oblique Crunch', 5, 'Side crunch targeting obliques', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Ab Wheel Rollout', 5, 'Rolling an ab wheel forward and back while kneeling', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hanging Leg Raise', 5, 'Raising legs while hanging from a pull-up bar', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cable Crunch', 5, 'Crunching forward while pulling a cable rope down', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cable Woodchop', 5, 'Diagonal pull and rotate motion on cable machine', 0);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Landmine Rotation', 5, 'Rotating a barbell from one shoulder to the other', 0);

-- ============================================
-- FLEXIBILITY & MOBILITY (25 exercises)
-- ============================================
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Standing Hamstring Stretch', 6, 'Hamstring stretch standing with one leg elevated', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Seated Forward Fold', 6, 'Sitting and reaching forward toward toes', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Butterfly Stretch', 6, 'Sitting with soles of feet together and pressing knees down', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Pigeon Pose', 6, 'Hip stretch with one leg bent in front and one extended behind', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cat-Cow Stretch', 6, 'Alternating arching and rounding the spine on all fours', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ("Child's Pose", 6, 'Kneeling and reaching arms forward on the floor', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Cobra Stretch', 6, 'Lying prone and lifting chest with arms extended', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Downward Dog', 6, 'Inverted V shape stretching hamstrings and shoulders', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Upward Dog', 6, 'Lying prone with chest lifted and arms extended', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ("World's Greatest Stretch", 6, 'Deep lunge with spinal twist and hamstring stretch', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Quad Stretch', 6, 'Pulling one heel toward the glute while standing', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Hip Flexor Stretch', 6, 'Lunge position stretching the front hip', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Shoulder Stretch', 6, 'Pulling one arm across the chest', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Chest Opener Stretch', 6, 'Clasping hands behind back and opening the chest', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Tricep Stretch', 6, 'Bending arm overhead and pushing elbow behind head', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Side Bend Stretch', 6, 'Bending sideways to stretch obliques', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Spinal Twist', 6, 'Lying on back rotating knees to one side', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Frog Stretch', 6, 'Kneeling with knees spread wide and toes outward', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('90/90 Hip Stretch', 6, 'Hip rotation stretch in a 90/90 seated position', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Thread the Needle', 6, 'Shoulder and spinal twist on all fours', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Seated Spinal Twist', 6, 'Twisting torso while sitting on the floor', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Knee-to-Chest Stretch', 6, 'Pulling one or both knees to chest while lying down', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Supine Spinal Twist', 6, 'Lying on back with knees rotated to one side', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Figure Four Stretch', 6, 'Ankle crossed over knee while lying on back', 1);
INSERT INTO exercises (name, category_id, description, time_based) VALUES ('Foam Roller Quads', 6, 'Rolling quads on a foam roller to release tension', 1);
