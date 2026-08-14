package com.example.internshipmanagementsystem.dto.response;

import java.util.List;
import org.springframework.data.domain.Pageable;

public record PageResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {

  public static <T> PageResponse<T> from(List<T> items, Pageable pageable) {
    int totalElements = items.size();
    int start = Math.min((int) pageable.getOffset(), totalElements);
    int end = Math.min(start + pageable.getPageSize(), totalElements);
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
    return new PageResponse<>(
        items.subList(start, end),
        pageable.getPageNumber(),
        pageable.getPageSize(),
        totalElements,
        totalPages,
        pageable.getPageNumber() == 0,
        pageable.getPageNumber() >= Math.max(totalPages - 1, 0));
  }
}
