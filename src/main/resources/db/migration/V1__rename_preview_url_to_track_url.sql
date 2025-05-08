-- Rename preview_url column to track_url in tracks table
ALTER TABLE tracks CHANGE COLUMN preview_url track_url VARCHAR(255); 