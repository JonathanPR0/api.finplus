package com.polarplus.utils;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtil {

    public static <T> PaginatedResponse<T> createOptionalPagination(
            List<T> items, Integer page, Integer size, String sortBy, String direction) {
        int totalItems = items.size();

        if (page != null && size != null) {
            // Se `sortBy` for nulo, definimos como "id" (padrão)
            sortBy = (sortBy != null) ? sortBy : "id";
            // Se `direction` for nulo, definimos como "asc" (padrão)
            direction = (direction != null) ? direction : "asc";

            // Define o Pageable
            Sort sort = Sort.by(Sort.Direction.fromString(direction != null ? direction : "asc"),
                    sortBy != null ? sortBy : "id");
            Pageable pageable = PageRequest.of(page, size, sort);

            // Calcula os índices de sublista
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), totalItems);

            // Retorna a sublista paginada
            List<T> paginatedItems = items.subList(start, end);
            return new PaginatedResponse<>(paginatedItems, page, size, totalItems);
        }

        // Retorna todos os itens se a paginação não for especificada
        return new PaginatedResponse<>(items, null, null, totalItems);
    }

    public static class PaginatedResponse<T> {
        private final List<T> items;
        private final Integer page;
        private final Integer size;
        private final int totalItems;

        public PaginatedResponse(List<T> items, Integer page, Integer size, int totalItems) {
            this.items = items;
            this.page = page;
            this.size = size;
            this.totalItems = totalItems;
        }

        public List<T> getItems() {
            return items;
        }

        public Integer getPage() {
            return page;
        }

        public Integer getSize() {
            return size;
        }

        public int getTotalItems() {
            return totalItems;
        }
    }
}
