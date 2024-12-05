package com.polarplus.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Banco;
import com.polarplus.domain.Empresa;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    Optional<Banco> findByIdAndEmpresa(Long id, Empresa empresa);

    boolean existsByNome(String nome);

    boolean existsByCodigo(String codCodigo);

    @Query(value = "SELECT * FROM fin_bancos b " +
            "WHERE (unaccent(b.nome) ILIKE CONCAT('%', :termo, '%') " +
            "OR b.codigo ILIKE CONCAT('%', :termo, '%')) " +
            "AND b.id_empresa = :idEmpresa", nativeQuery = true)
    Page<Banco> findByTermo(@Param("termo") String termo, @Param("idEmpresa") Long idEmpresa, Pageable pageable);

}
