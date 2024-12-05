package com.polarplus.domain.cr;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarplus.domain.Categoria;
import com.polarplus.domain.Empresa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fin_cr_servicos", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "descricao", "data_servico" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_servico", nullable = false)
    private LocalDate dataServico;

    @Column(name = "data_servico_futuro", nullable = false)
    private LocalDate dataServicoFuturo;

    @Column(name = "data_expiracao_garantia")
    private LocalDate dataExpiracaoGarantia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

}
