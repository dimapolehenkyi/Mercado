CREATE INDEX idx_module_status
    ON modules(status);

CREATE INDEX idx_module_course_id
    ON modules(course_id);

CREATE INDEX idx_module_course_position
    ON modules(course_id, position);