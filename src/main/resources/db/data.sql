-- Insert sample roles
INSERT INTO roles (name) VALUES 
('ROLE_USER'),
('ROLE_ADMIN');

-- Insert sample users
INSERT INTO users (username, email, password, display_name, bio, image_url, country) VALUES
('john_doe', 'john@example.com', '$2a$10$X7G3YzR8NQ1Uq1Z2W3Y4Z5', 'John Doe', 'Music enthusiast', 'https://example.com/john.jpg', 'US'),
('jane_smith', 'jane@example.com', '$2a$10$X7G3YzR8NQ1Uq1Z2W3Y4Z5', 'Jane Smith', 'Loves indie music', 'https://example.com/jane.jpg', 'UK'),
('admin', 'admin@example.com', '$2a$10$X7G3YzR8NQ1Uq1Z2W3Y4Z5', 'Admin', 'System administrator', 'https://example.com/admin.jpg', 'US');

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- John is a regular user
(2, 1), -- Jane is a regular user
(3, 2); -- Admin has admin role

-- Insert sample artists
INSERT INTO artists (name, image_url, description, popularity) VALUES
('The Beatles', 'https://example.com/beatles.jpg', 'Legendary British rock band', 100),
('Taylor Swift', 'https://example.com/taylor.jpg', 'American singer-songwriter', 95),
('BTS', 'https://example.com/bts.jpg', 'South Korean boy band', 90);

-- Insert sample albums
INSERT INTO albums (name, image_url, description, popularity, release_date, album_type) VALUES
('Abbey Road', 'https://example.com/abbeyroad.jpg', 'The Beatles final studio album', 100, '1969-09-26', 'ALBUM'),
('Folklore', 'https://example.com/folklore.jpg', 'Taylor Swift indie folk album', 95, '2020-07-24', 'ALBUM'),
('Map of the Soul: 7', 'https://example.com/mots7.jpg', 'BTS seventh studio album', 90, '2020-02-21', 'ALBUM');

-- Insert sample tracks
INSERT INTO tracks (name, image_url, duration_ms, popularity, preview_url, track_number, explicit, isrc, album_id) VALUES
('Come Together', 'https://example.com/cometogether.jpg', 259000, 90, 'https://example.com/preview1', '1', false, 'USABC1234567', 1),
('Here Comes the Sun', 'https://example.com/herecomesthesun.jpg', 185000, 95, 'https://example.com/preview2', '2', false, 'USABC1234568', 1),
('cardigan', 'https://example.com/cardigan.jpg', 239000, 90, 'https://example.com/preview3', '1', false, 'USABC1234569', 2),
('Dynamite', 'https://example.com/dynamite.jpg', 199000, 95, 'https://example.com/preview4', '1', false, 'USABC1234570', 3);

-- Link tracks to artists
INSERT INTO track_artists (track_id, artist_id) VALUES
(1, 1), -- Come Together by The Beatles
(2, 1), -- Here Comes the Sun by The Beatles
(3, 2), -- cardigan by Taylor Swift
(4, 3); -- Dynamite by BTS

-- Insert sample playlists
INSERT INTO playlists (name, description, image_url, is_public, collaborative, owner_id) VALUES
('Classic Rock Favorites', 'Best of classic rock', 'https://example.com/classicrock.jpg', true, false, 1),
('Pop Hits 2023', 'Latest pop hits', 'https://example.com/pophits.jpg', true, true, 2);

-- Add tracks to playlists
INSERT INTO playlist_tracks (playlist_id, track_id, position) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 1),
(2, 4, 2);

-- Add user follows
INSERT INTO user_followers (follower_id, following_id) VALUES
(1, 2), -- John follows Jane
(2, 1); -- Jane follows John

-- Add user followed artists
INSERT INTO user_followed_artists (user_id, artist_id) VALUES
(1, 1), -- John follows The Beatles
(2, 2); -- Jane follows Taylor Swift

-- Add user saved albums
INSERT INTO user_saved_albums (user_id, album_id) VALUES
(1, 1), -- John saved Abbey Road
(2, 2); -- Jane saved Folklore

-- Add user saved tracks
INSERT INTO user_saved_tracks (user_id, track_id) VALUES
(1, 1), -- John saved Come Together
(2, 3); -- Jane saved cardigan 