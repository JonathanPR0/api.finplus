package com.polarplus.repositories.cr;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cr.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
        boolean existsByNumero(String numero);

        boolean existsByCnpj(String cnpj);

        boolean existsByEndereco(String endereco);

        // Consulta personalizada com filtro LIKE e paginação
        @Query("SELECT c FROM Cliente c WHERE " +
                        "(c.nome LIKE CONCAT('%', :termo, '%') OR " +
                        "c.numero LIKE CONCAT('%', :termo, '%') OR " +
                        "c.cnpj LIKE CONCAT('%', :termo, '%'))")
        Page<Cliente> findByTermo(@Param("termo") String termo, Pageable pageable);

}
