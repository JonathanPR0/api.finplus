package com.polarplus.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Cartao;
import com.polarplus.domain.Empresa;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

        Optional<Cartao> findByIdAndEmpresa(Long id, Empresa empresa);

        void deleteByIdAndEmpresa(Long id, Empresa empresa);

        // Consulta personalizada com filtro LIKE e paginação
        @Query(value = "SELECT fp.* FROM fin_cartoes fp WHERE " +
                        "(:termo IS NULL OR unaccent(fp.nome) LIKE CONCAT('%', :termo, '%')) " +
                        "AND fp.id_empresa = :idEmpresa ", nativeQuery = true)
        Page<Cartao> findByTermo(@Param("termo") String termo, Long idEmpresa, Pageable pageable);

}
