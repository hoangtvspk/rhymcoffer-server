-- Create artist_genres table (for genres collection in Artist)
CREATE TABLE artist_genres (
    artist_id BIGINT NOT NULL,
    genre VARCHAR(255) NOT NULL,
    PRIMARY KEY (artist_id, genre),
    FOREIGN KEY (artist_id) REFERENCES artists(id)
); 