DROP INDEX IF EXISTS idx_module_status;

ALTER TABLE modules
DROP CONSTRAINT IF EXISTS modules_status_check;

ALTER TABLE modules
DROP CONSTRAINT IF EXISTS modules_type_check;