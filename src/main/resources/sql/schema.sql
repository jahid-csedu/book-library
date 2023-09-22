CREATE SEQUENCE author_id_seq;


CREATE TABLE IF NOT EXISTS author (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


CREATE INDEX IF NOT EXISTS idx_author_name ON author (id);