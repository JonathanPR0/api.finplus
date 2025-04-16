package com.polarplus.repositories.cp;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Empresa;
import com.polarplus.domain.cp.TituloCP;

import jakarta.persistence.Tuple;

@Repository
public interface TituloCPRepository extends JpaRepository<TituloCP, Long> {
        Optional<TituloCP> findByIdAndEmpresa(Long id, Empresa empresa);

        @Query(value = """
                        SELECT
                                t.id,
                                t.descricao AS descricao,
                                c.nome AS categoria,
                                st.status AS status,
                                t.valor AS valor,
                                c.cor AS cor,
                                t.created_at AS dataTitulo
                        FROM fin_cp_titulos t
                        JOIN fin_cp_titulos_status st ON t.id_status = st.id
                        JOIN fin_categorias c ON t.id_categoria = c.id
                        WHERE t.id_empresa = :empresaId
                        AND (:idFormaPagamento IS NULL OR t.id_forma_pagamento = :idFormaPagamento)
                        AND (:idStatus IS NULL OR t.id_status = :idStatus)
                        AND (:idFornecedor IS NULL OR t.id_fornecedor = :idFornecedor)
                        AND (:idCategoria IS NULL OR t.id_categoria = :idCategoria)
                        AND (:idContaBancaria IS NULL OR t.id_conta_bancaria = :idContaBancaria)
                        """, countQuery = """
                        SELECT COUNT(*)
                        FROM fin_cp_titulos t
                        JOIN fin_cp_titulos_status st ON t.id_status = st.id
                        JOIN fin_categorias c ON t.id_categoria = c.id
                        WHERE t.id_empresa = :empresaId
                        AND (:idFormaPagamento IS NULL OR t.id_forma_pagamento = :idFormaPagamento)
                        AND (:idStatus IS NULL OR t.id_status = :idStatus)
                        AND (:idFornecedor IS NULL OR t.id_fornecedor = :idFornecedor)
                        AND (:idCategoria IS NULL OR t.id_categoria = :idCategoria)
                        AND (:idContaBancaria IS NULL OR t.id_conta_bancaria = :idContaBancaria)
                        """, nativeQuery = true)
        Page<Tuple> getAllByEmpresa(
                        @Param("empresaId") Long empresaId,
                        @Param("idFormaPagamento") Long idFormaPagamento,
                        @Param("idStatus") Long idStatus,
                        @Param("idFornecedor") Long idFornecedor,
                        @Param("idCategoria") Long idCategoria,
                        @Param("idContaBancaria") Long idContaBancaria,
                        Pageable pageable);

}
