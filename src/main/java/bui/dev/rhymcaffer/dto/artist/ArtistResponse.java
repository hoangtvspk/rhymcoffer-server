package bui.dev.rhymcaffer.dto.artist;

import bui.dev.rhymcaffer.dto.album.AlbumResponse;
import bui.dev.rhymcaffer.dto.track.TrackResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer popularity;
    // private List<TrackForArtistResponse> tracks;
    // private List<AlbumForArtistResponse> albums;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}