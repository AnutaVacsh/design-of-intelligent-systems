package ru.lama.expertCookingSystem.dto.response;

import java.util.List;

// For pagination
public record PageResponse<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean last
) {

}
