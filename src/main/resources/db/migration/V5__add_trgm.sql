ALTER TABLE tags DROP COLUMN IF EXISTS formated_name;

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX IF NOT EXISTS trgm_idx
    ON tags USING gin(name gin_trgm_ops);