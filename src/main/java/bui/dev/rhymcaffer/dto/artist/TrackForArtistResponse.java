package bui.dev.rhymcaffer.dto.artist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrackForArtistResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private Integer durationMs;
    private Integer popularity;
    private String trackUrl;
    private String trackNumber;
    private Boolean explicit;
    private String isrc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}