ALTER TABLE course_requirements
DROP CONSTRAINT uk_req_position_course_id;

ALTER TABLE course_requirements
ADD CONSTRAINT uk_req_position_course_id
UNIQUE (course_id, "position")
DEFERRABLE INITIALLY DEFERRED;


ALTER TABLE course_learning_points
DROP CONSTRAINT uk_lp_position_course_id;

ALTER TABLE course_learning_points
ADD CONSTRAINT uk_lp_position_course_id
UNIQUE (course_id, "position")
DEFERRABLE INITIALLY DEFERRED;