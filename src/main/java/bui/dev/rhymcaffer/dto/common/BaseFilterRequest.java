package bui.dev.rhymcaffer.dto.track;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class BaseFilterRequest {

    // Pagination
    private Integer page = 0;
    private Integer size = 10;

    // Sorting
    private String sortBy = "name";
    private Sort.Direction direction = Sort.Direction.ASC;
}