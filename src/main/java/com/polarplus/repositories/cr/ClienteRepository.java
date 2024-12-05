package com.polarplus.repositories.cr;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cr.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

        Optional<Cliente> findByIdAndEmpresa(Long id, Empresa empresa);

        boolean existsByNumero(String numero);

        boolean existsByCnpj(String cnpj);

        boolean existsByEndereco(String endereco);

        void deleteByIdAndEmpresa(Long id, Empresa empresa);

        // Consulta personalizada com filtro LIKE e paginação
        @Query(value = "SELECT c.* FROM fin_cr_clientes c WHERE " +
                        "(unaccent(c.nome) LIKE CONCAT('%', :termo, '%') OR " +
                        "c.numero LIKE CONCAT('%', :termo, '%') OR " +
                        "c.cnpj LIKE CONCAT('%', :termo, '%'))" +
                        "AND c.id_empresa = :idEmpresa ", nativeQuery = true)
        Page<Cliente> findByTermo(@Param("termo") String termo, Long idEmpresa, Pageable pageable);

}
