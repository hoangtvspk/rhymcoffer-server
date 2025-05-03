package bui.dev.rhymcaffer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class AlbumRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String imageUrl;
    private String description;
    private Integer popularity;
    private String releaseDate;
    private String albumType; // album, single, compilation
    private Set<Long> artistIds;
} 