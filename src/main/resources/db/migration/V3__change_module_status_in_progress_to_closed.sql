ALTER TABLE modules
DROP CONSTRAINT modules_status_check;

UPDATE modules
SET status = 'CLOSED'
WHERE status = 'IN_PROGRESS';

ALTER TABLE modules
ADD CONSTRAINT modules_status_check
    CHECK (
        ((status)::text = ANY (
            ARRAY[
            'DRAFT'::character varying,
            'PUBLISHED'::character varying,
            'ARCHIVED'::character varying,
            'CLOSED'::character varying
            ]::text[]
    ))
);

