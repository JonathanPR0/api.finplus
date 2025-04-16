package com.polarplus.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.ContaBancaria;
import com.polarplus.domain.Empresa;

@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {

        Optional<ContaBancaria> findByIdAndEmpresa(Long id, Empresa empresa);

        boolean existsByDescricao(String descricao);

        List<ContaBancaria> findByEmpresa(Empresa empresa);

        @Query(value = "SELECT c.*, b.nome as banco FROM fin_contas_bancarias c " +
                        "LEFT JOIN fin_bancos b ON b.id = c.id_banco " +
                        "WHERE (c.agencia ILIKE CONCAT('%', :termo, '%') OR " +
                        "c.conta ILIKE CONCAT('%', :termo, '%') OR " +
                        "unaccent(c.descricao) ILIKE CONCAT('%', :termo, '%') OR " +
                        "unaccent(b.nome) ILIKE CONCAT('%', :termo, '%')) " +
                        "AND c.id_empresa = :idEmpresa " +
                        "AND (:idBanco IS NULL OR b.id = :idBanco)", nativeQuery = true)
        Page<ContaBancaria> findByTermo(@Param("termo") String termo, @Param("idBanco") Long idBanco,
                        @Param("idEmpresa") Long idEmpresa, Pageable pageable);

}
