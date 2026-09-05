CREATE TABLE authors(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE books(
    id SERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    publication_year INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    genre TEXT NOT NULL,

    CONSTRAINT fk_books_author
                  FOREIGN KEY (author_id)
                  REFERENCES authors(id)
);