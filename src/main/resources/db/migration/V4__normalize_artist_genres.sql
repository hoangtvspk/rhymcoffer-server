-- Create genres table (master list of genres)
CREATE TABLE genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Drop old artist_genres table if it exists
DROP TABLE IF EXISTS artist_genres;

-- Create artist_genres table (normalized, references genres)
CREATE TABLE artist_genres (
    artist_id BIGINT NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (artist_id, genre_id),
    FOREIGN KEY (artist_id) REFERENCES artists(id),
    FOREIGN KEY (genre_id) REFERENCES genres(id)
); 