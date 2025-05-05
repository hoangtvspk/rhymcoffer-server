package bui.dev.rhymcaffer.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class PlaylistRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    private String imageUrl;
    private Boolean isPublic;
    private Boolean collaborative;
    private Set<Long> trackIds;
}