CREATE SEQUENCE author_id_seq;


CREATE TABLE IF NOT EXISTS authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone
);


CREATE INDEX IF NOT EXISTS idx_author_name ON authors (id);