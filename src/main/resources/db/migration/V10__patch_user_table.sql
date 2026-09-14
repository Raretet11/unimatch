ALTER TABLE users RENAME COLUMN sex TO gender;

ALTER TABLE users
    ADD CONSTRAINT users_course_check
    CHECK (course IS NULL OR (course >= 1 AND course <= 6));
