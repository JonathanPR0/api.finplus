package com.polarplus.dto;

public class PaginationDTO {
        private Integer page;
        private Integer size;
        private String direction;
        private String sortBy;

        // Construtor com valores padrão
        public PaginationDTO() {
                this.page = 0; // Página padrão
                this.size = Integer.MAX_VALUE; // Tamanho padrão
                this.direction = "ASC"; // Direção padrão
                this.sortBy = "id"; // Campo de ordenação padrão
        }

        // Getters e Setters
        public Integer getPage() {
                return page;
        }

        public void setPage(Integer page) {
                this.page = page != null && page >= 0 ? page : 0;
        }

        public Integer getSize() {
                return size;
        }

        public void setSize(Integer size) {
                this.size = size != null && size > 0 ? size : Integer.MAX_VALUE;
        }

        public String getDirection() {
                return direction;
        }

        public void setDirection(String direction) {
                if (direction != null && (direction.equalsIgnoreCase("ASC") || direction.equalsIgnoreCase("DESC"))) {
                        this.direction = direction;
                } else {
                        this.direction = "ASC";
                }
        }

        public String getSortBy() {
                return sortBy;
        }

        public void setSortBy(String sortBy) {
                this.sortBy = sortBy != null ? sortBy : "id";
        }
}
