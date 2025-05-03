package bui.dev.rhymcaffer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String displayName;
    private String bio;
    private String imageUrl;
    private Set<Long> playlistIds;
    private Set<Long> savedTrackIds;
    private Set<Long> savedAlbumIds;
    private Set<Long> followedArtistIds;
    private Set<Long> followerIds;
    private Set<Long> followingIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Add more fields as needed, e.g., profileImage, bio, etc.
} 