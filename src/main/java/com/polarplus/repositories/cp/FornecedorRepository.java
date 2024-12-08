package com.polarplus.repositories.cp;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cp.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

        Optional<Fornecedor> findByIdAndEmpresa(Long id, Empresa empresa);

        void deleteByIdAndEmpresa(Long id, Empresa empresa);

        // Consulta personalizada com filtro LIKE e paginação
        @Query(value = "SELECT c.* FROM fin_cp_fornecedores c WHERE " +
                        "unaccent(c.nome) LIKE CONCAT('%', :termo, '%') " +
                        "AND c.id_empresa = :idEmpresa ", nativeQuery = true)
        Page<Fornecedor> findByTermo(@Param("termo") String termo, Long idEmpresa, Pageable pageable);

}
