-- Create album_artists table (Many-to-Many relationship between albums and artists)
CREATE TABLE album_artists (
    album_id BIGINT,
    artist_id BIGINT,
    PRIMARY KEY (album_id, artist_id),
    FOREIGN KEY (album_id) REFERENCES albums(id),
    FOREIGN KEY (artist_id) REFERENCES artists(id)
); 