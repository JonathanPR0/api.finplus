package com.polarplus.domain.cr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.polarplus.domain.enums.StatusVencimentoCP;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cp_titulos_pagamentos", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_titulo", "data_vencimento" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoCR implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_titulo", nullable = false)
    private TituloCR tituloCP;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "data_prevista", nullable = false)
    private LocalDate dataPrevista;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "valor_pago", nullable = false)
    private BigDecimal valorPago;

    @Enumerated(EnumType.STRING) // Usando EnumType.STRING para armazenar o nome do enum no banco
    @Column(nullable = false)
    private StatusVencimentoCP status;
}
