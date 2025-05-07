package bui.dev.rhymcaffer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class TrackRequest {
    @NotBlank(message = "Name is required")
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
}