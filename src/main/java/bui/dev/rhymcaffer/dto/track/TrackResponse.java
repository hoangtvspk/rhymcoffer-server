package bui.dev.rhymcaffer.dto.track;

import bui.dev.rhymcaffer.dto.artist.ArtistResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Integer durationMs;
    private Integer popularity;
    private String trackUrl;
    private String trackNumber;
    private Boolean explicit;
    private String isrc;
    private Long albumId;
    private Set<Long> artistIds;
    private List<ArtistForTrackResponse> artists;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}