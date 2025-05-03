package bui.dev.rhymcaffer.dto.response;

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
public class ArtistResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer popularity;
    private List<String> genres;
    private Set<Long> trackIds;
    private Set<Long> albumIds;
    private Set<Long> followerIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 