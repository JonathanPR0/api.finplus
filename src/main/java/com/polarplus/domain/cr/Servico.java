package com.polarplus.domain.cr;

import java.io.Serializable;
import java.time.LocalDate;

import com.polarplus.domain.Categoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cr_servicos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

}
