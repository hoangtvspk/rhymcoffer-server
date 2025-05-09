package bui.dev.rhymcaffer.dto.track;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumForTrackResponse {
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