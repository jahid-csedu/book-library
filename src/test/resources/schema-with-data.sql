CREATE SEQUENCE author_id_seq;

CREATE TABLE IF NOT EXISTS authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone
);


CREATE SEQUENCE category_id_seq;

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone
);


CREATE SEQUENCE publisher_id_seq;

CREATE TABLE IF NOT EXISTS publishers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone
);


CREATE SEQUENCE book_id_seq;

CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    isbn VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author_id bigint,
    category_id bigint,
    publisher_id bigint,
    publication_year integer,
    price numeric(38,2),
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_date timestamp(6) without time zone,
    CONSTRAINT books_isbn_uk UNIQUE (isbn),
    CONSTRAINT books_author_fk FOREIGN KEY (author_id)
        REFERENCES public.authors (id),
    CONSTRAINT books_category_fk FOREIGN KEY (category_id)
        REFERENCES public.categories (id),
    CONSTRAINT books_publisher_fk FOREIGN KEY (publisher_id)
        REFERENCES public.publishers (id)
);


INSERT INTO authors (id, name, created_date) VALUES (1, 'John Doe', '2023-09-25T21:00:00');
INSERT INTO authors (id, name, created_date) VALUES (2, 'Mickey', '2023-09-25T21:00:00');


INSERT INTO categories (id, name, created_date) VALUES (1, 'IT', '2023-09-25T21:00:00');
INSERT INTO categories (id, name, created_date) VALUES (2, 'Novel', '2023-09-25T21:00:00');


INSERT INTO publishers (id, name, created_date) VALUES (1, 'Mars', '2023-09-25T21:00:00');
INSERT INTO publishers (id, name, created_date) VALUES (2, 'Neptune', '2023-09-25T21:00:00');