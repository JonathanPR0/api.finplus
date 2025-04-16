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

    @Query(value = "SELECT * FROM fin_bancos b " +
            "WHERE (unaccent(b.nome) ILIKE CONCAT('%', :termo, '%') " +
            "OR b.codigo ILIKE CONCAT('%', :termo, '%')) ", nativeQuery = true)
    Page<Banco> findByTermo(@Param("termo") String termo, Pageable pageable);

}
