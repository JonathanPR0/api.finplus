package com.polarplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Categoria;
import com.polarplus.domain.enums.TipoCategoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
        boolean existsByDescricao(String descricao);

        @Query("SELECT c FROM Categoria c " +
                        "WHERE (:nome IS NULL OR translate(c.nome, 'áéíóúãâêîôûàèìòù', 'aeiouaaeiouaeiou') ILIKE CONCAT('%', CAST(COALESCE(:nome, '') AS STRING), '%')) "
                        +
                        "AND (:tipo IS NULL OR c.tipo = :tipo)")
        Page<Categoria> findByNomeAndTipo(@Param("nome") String nome,
                        @Param("tipo") TipoCategoria tipo,
                        Pageable pageable);
}
