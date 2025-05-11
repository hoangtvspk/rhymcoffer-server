package bui.dev.rhymcaffer.dto.track;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrackListRequest extends bui.dev.rhymcaffer.dto.track.BaseFilterRequest {
    // Filter fields
    private String name;
    private Long artistId;
    private Long albumId;
    private Long userId;
    private Integer minPopularity;
    private Boolean explicit;

}