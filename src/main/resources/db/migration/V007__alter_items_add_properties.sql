DELETE FROM item;
ALTER TABLE item DROP COLUMN name;
ALTER TABLE item ADD COLUMN item_type VARCHAR(64);
ALTER TABLE item ADD COLUMN display_name VARCHAR(128);
ALTER TABLE item ADD COLUMN ordinal INTEGER;
ALTER TABLE item ADD COLUMN metallic BOOLEAN;
ALTER TABLE item ADD COLUMN properties JSONB;
