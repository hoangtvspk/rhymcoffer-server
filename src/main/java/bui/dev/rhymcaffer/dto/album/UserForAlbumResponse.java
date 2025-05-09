package bui.dev.rhymcaffer.dto.album;

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
public class UserForAlbumResponse {
    private Long id;
    private String displayName;
    private String imageUrl;
    // Add more fields as needed, e.g., profileImage, bio, etc.
}