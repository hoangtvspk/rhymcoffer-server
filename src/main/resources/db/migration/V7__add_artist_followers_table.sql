-- Create artist_followers table (Many-to-Many relationship between artists and users who follow them)
CREATE TABLE artist_followers (
    artist_id BIGINT,
    user_id BIGINT,
    followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (artist_id, user_id),
    FOREIGN KEY (artist_id) REFERENCES artists(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
); 