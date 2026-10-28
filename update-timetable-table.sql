-- ============================================================
-- Update Timetable Table - Add classroom column
-- Run this script if you already have the database set up
-- and need to add the classroom field
-- ============================================================

-- Add classroom column to existing timetable table
ALTER TABLE timetable ADD COLUMN IF NOT EXISTS classroom VARCHAR(50) DEFAULT NULL;

-- Update existing records with classroom data
UPDATE timetable SET classroom = 'Room 101' WHERE classroom IS NULL AND id <= 20;

-- Insert comprehensive timetable data for class 10-A
INSERT INTO timetable (active, class_section, day, period, subject, classroom, teacher_id) VALUES
    (1, '10-A', 'MON', 1, 'Mathematics', 'Room 101', 1),
    (1, '10-A', 'MON', 2, 'Physics', 'Lab 1', 1),
    (1, '10-A', 'MON', 3, 'Chemistry', 'Lab 2', 1),
    (1, '10-A', 'MON', 4, 'English', 'Room 105', 1),
    (1, '10-A', 'TUE', 1, 'Physics', 'Lab 1', 1),
    (1, '10-A', 'TUE', 2, 'Mathematics', 'Room 101', 1),
    (1, '10-A', 'TUE', 3, 'English', 'Room 105', 1),
    (1, '10-A', 'TUE', 4, 'Chemistry', 'Lab 2', 1),
    (1, '10-A', 'WED', 1, 'English', 'Room 105', 1),
    (1, '10-A', 'WED', 2, 'Chemistry', 'Lab 2', 1),
    (1, '10-A', 'WED', 3, 'Mathematics', 'Room 101', 1),
    (1, '10-A', 'WED', 4, 'Physics', 'Lab 1', 1),
    (1, '10-A', 'THU', 1, 'Computer Science', 'Lab 3', 1),
    (1, '10-A', 'THU', 2, 'Mathematics', 'Room 101', 1),
    (1, '10-A', 'THU', 3, 'Physics', 'Lab 1', 1),
    (1, '10-A', 'THU', 4, 'English', 'Room 105', 1),
    (1, '10-A', 'FRI', 1, 'Chemistry', 'Lab 2', 1),
    (1, '10-A', 'FRI', 2, 'Computer Science', 'Lab 3', 1),
    (1, '10-A', 'FRI', 3, 'Mathematics', 'Room 101', 1),
    (1, '10-A', 'FRI', 4, 'Physics', 'Lab 1', 1),
    -- Timetable for class 10-B
    (1, '10-B', 'MON', 1, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'MON', 2, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'MON', 3, 'Physics', 'Lab 1', 1),
    (1, '10-B', 'MON', 4, 'English', 'Room 105', 1),
    (1, '10-B', 'TUE', 1, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'TUE', 2, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'TUE', 3, 'English', 'Room 105', 1),
    (1, '10-B', 'TUE', 4, 'Physics', 'Lab 1', 1),
    (1, '10-B', 'WED', 1, 'Physics', 'Lab 1', 1),
    (1, '10-B', 'WED', 2, 'English', 'Room 105', 1),
    (1, '10-B', 'WED', 3, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'WED', 4, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'THU', 1, 'English', 'Room 105', 1),
    (1, '10-B', 'THU', 2, 'Physics', 'Lab 1', 1),
    (1, '10-B', 'THU', 3, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'THU', 4, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'FRI', 1, 'Mathematics', 'Room 202', 1),
    (1, '10-B', 'FRI', 2, 'Biology', 'Lab 4', 1),
    (1, '10-B', 'FRI', 3, 'English', 'Room 105', 1),
    (1, '10-B', 'FRI', 4, 'Physics', 'Lab 1', 1);

SELECT 'Timetable updated successfully!' AS Status;
