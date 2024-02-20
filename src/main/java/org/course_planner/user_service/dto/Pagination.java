package org.course_planner.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private Integer size;
    private Integer page;
    private Integer totalPages;
    private Integer totalSize;

    public Pagination(Page<?> item) {
        this.size = item.getSize();
        this.page = item.getNumber();
        this.totalPages = item.getTotalPages();
        this.totalSize = Integer.parseInt(item.getTotalElements() + "");
    }

    public static boolean isValidPaginationObj(Pagination pagination) {
        return pagination != null && (pagination.isInvalidInteger(pagination.getSize())
                || pagination.isInvalidInteger(pagination.getPage()));
    }

    private boolean isInvalidInteger(Integer value) {
        return value == null || value < 1;
    }
}
