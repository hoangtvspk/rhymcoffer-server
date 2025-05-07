-- Create album_followers table (Many-to-Many relationship between albums and users who follow them)
CREATE TABLE album_followers (
    album_id BIGINT,
    user_id BIGINT,
    followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (album_id, user_id),
    FOREIGN KEY (album_id) REFERENCES albums(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
); 