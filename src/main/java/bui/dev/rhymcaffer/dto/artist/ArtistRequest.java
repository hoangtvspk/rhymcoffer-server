package bui.dev.rhymcaffer.dto.artist;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String imageUrl;
    private String description;
    private Integer popularity;
} 