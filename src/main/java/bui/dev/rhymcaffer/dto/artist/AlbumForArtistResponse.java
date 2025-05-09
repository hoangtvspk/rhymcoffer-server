package bui.dev.rhymcaffer.dto.artist;

import bui.dev.rhymcaffer.dto.album.ArtistForAlbumResponse;
import bui.dev.rhymcaffer.dto.album.TrackForAlbumResponse;
import bui.dev.rhymcaffer.dto.album.UserForAlbumResponse;
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
public class AlbumForArtistResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer popularity;
    private String releaseDate;
    private String albumType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}