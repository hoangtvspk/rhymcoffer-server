package bui.dev.rhymcaffer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}