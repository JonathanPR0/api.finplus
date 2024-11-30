package com.polarplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Banco;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    boolean existsByNome(String nome);

    boolean existsByCodigo(String codCodigo);

    @Query("SELECT c FROM Banco c WHERE " +
            "( translate(c.nome, 'áéíóúãâêîôûàèìòù', 'aeiouaaeiouaeiou') ILIKE CONCAT('%', :termo, '%') OR " +
            "c.codigo ILIKE CONCAT('%', :termo, '%'))")
    Page<Banco> findByTermo(@Param("termo") String termo, Pageable pageable);
}
