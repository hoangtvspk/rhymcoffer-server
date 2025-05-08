package bui.dev.rhymcaffer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ArtistRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String imageUrl;
    private String description;
    private Integer popularity;
} 