package com.polarplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.ContaBancaria;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    boolean existsByDescricao(String descricao);

    @Query("SELECT c FROM ContaBancaria c " +
            "JOIN c.banco b " +
            "WHERE " +
            "(c.agencia ILIKE CONCAT('%', :termo, '%') OR " +
            "c.conta ILIKE CONCAT('%', :termo, '%') OR " +
            "translate(c.descricao, 'áéíóúãâêîôûàèìòù', 'aeiouaaeiouaeiou') ILIKE CONCAT('%', :termo, '%') OR " +
            "translate(b.nome, 'áéíóúãâêîôûàèìòù', 'aeiouaaeiouaeiou') ILIKE CONCAT('%', :termo, '%'))")
    Page<ContaBancaria> findByTermo(@Param("termo") String termo, Pageable pageable);
}
