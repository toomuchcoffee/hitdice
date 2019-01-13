CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    created TIMESTAMP,
    modified TIMESTAMP,
    name VARCHAR(64),
    experience INTEGER,
    level INTEGER,
    strength INTEGER,
    dexterity INTEGER,
    stamina INTEGER,
    health INTEGER,
    max_health INTEGER,
    weapon VARCHAR(64),
    armor VARCHAR(64)
);