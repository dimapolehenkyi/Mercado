ALTER TABLE modules
DROP CONSTRAINT uk_position_courseid;

ALTER TABLE modules
ADD CONSTRAINT uk_position_courseid
    UNIQUE (course_id, position)
DEFERRABLE INITIALLY DEFERRED;