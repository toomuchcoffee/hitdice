CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    hero_id INTEGER REFERENCES hero (id) NOT NULL,
    created TIMESTAMP,
    name VARCHAR(64)
);

ALTER TABLE hero DROP items;