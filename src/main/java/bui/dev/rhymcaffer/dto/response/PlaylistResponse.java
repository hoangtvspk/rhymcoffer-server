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
public class PlaylistResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isPublic;
    private Boolean collaborative;
    private Long ownerId;
    private Set<Long> trackIds;
    private Set<Long> followerIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 