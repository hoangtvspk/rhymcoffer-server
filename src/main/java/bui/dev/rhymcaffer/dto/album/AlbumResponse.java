package bui.dev.rhymcaffer.dto.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer popularity;
    private String releaseDate;
    private String albumType;
    private List<ArtistForAlbumResponse> artists;
    private List<TrackForAlbumResponse> tracks;
    private List<UserForAlbumResponse> followers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}