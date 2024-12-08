package com.polarplus.repositories.cr;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cr.TituloCR;

import jakarta.persistence.Tuple;

@Repository
public interface TituloCRRepository extends JpaRepository<TituloCR, Long> {
        Optional<TituloCR> findByIdAndEmpresa(Long id, Empresa empresa);

        @Query(value = """
                        SELECT
                                t.descricao AS descricao,
                                c.nome AS categoria,
                                st.status AS status,
                                t.valor AS valor,
                                c.cor AS cor,
                                s.data_servico AS dataServico
                        FROM fin_cr_titulos t
                        JOIN fin_cr_servicos s ON t.id_servico = s.id
                        JOIN fin_cr_titulos_status st ON t.id_status = st.id
                        JOIN fin_categorias c ON s.id_categoria = c.id
                        WHERE t.id_empresa = :empresaId
                        AND (:idFormaRecebimento IS NULL OR t.id_forma_recebimento = :idFormaRecebimento)
                        AND (:idStatus IS NULL OR t.id_status = :idStatus)
                        AND (:idCliente IS NULL OR t.id_cliente = :idCliente)
                        AND (:idCategoria IS NULL OR s.id_categoria = :idCategoria)
                        AND (:idContaBancaria IS NULL OR t.id_conta_bancaria = :idContaBancaria)
                        """, countQuery = """
                        SELECT COUNT(*)
                        FROM fin_cr_titulos t
                        JOIN fin_cr_servicos s ON t.id_servico = s.id
                        JOIN fin_cr_titulos_status st ON t.id_status = st.id
                        JOIN fin_categorias c ON s.id_categoria = c.id
                        WHERE t.id_empresa = :empresaId
                        AND (:idFormaRecebimento IS NULL OR t.id_forma_recebimento = :idFormaRecebimento)
                        AND (:idStatus IS NULL OR t.id_status = :idStatus)
                        AND (:idCliente IS NULL OR t.id_cliente = :idCliente)
                        AND (:idCategoria IS NULL OR s.id_categoria = :idCategoria)
                        AND (:idContaBancaria IS NULL OR t.id_conta_bancaria = :idContaBancaria)
                        """, nativeQuery = true)
        Page<Tuple> getAllByEmpresa(
                        @Param("empresaId") Long empresaId,
                        @Param("idFormaRecebimento") Long idFormaRecebimento,
                        @Param("idStatus") Long idStatus,
                        @Param("idCliente") Long idCliente,
                        @Param("idCategoria") Long idCategoria,
                        @Param("idContaBancaria") Long idContaBancaria,
                        Pageable pageable);

}
