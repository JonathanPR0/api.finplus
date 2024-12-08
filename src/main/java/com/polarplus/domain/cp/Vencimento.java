package com.polarplus.domain.cp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarplus.domain.Empresa;
import com.polarplus.domain.enums.StatusVencimento;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fin_cp_titulos_vencimentos", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "id_titulo", "data_vencimento" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vencimento implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_titulo", nullable = false)
    private TituloCP titulo;

    @Column(name = "data_vencimento", nullable = false)
    @JsonProperty("data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    @JsonProperty("data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "valor_pago", nullable = false)
    @JsonProperty("valor_pago")
    private BigDecimal valorPago = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusVencimento status = StatusVencimento.pendente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @PrePersist
    public void prePersist() {
        if (valorPago == null) {
            valorPago = BigDecimal.ZERO;
        }
        if (status == null) {
            status = StatusVencimento.pendente;
        }
    }
}
