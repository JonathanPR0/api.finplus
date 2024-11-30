package com.polarplus.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "fin_contas_bancarias", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "descricao", "id_banco" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaBancaria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_banco", nullable = false)
    private Banco banco;

    @Column(nullable = false, length = 20)
    private String agencia;
    @Column(name = "dv_agencia", length = 2)
    private String dvAgencia;

    @Column(nullable = false, length = 20)
    private String conta;
    @Column(name = "dv_conta", length = 2)
    private String dvConta;

    @Column(nullable = false, length = 100, unique = true)
    private String descricao;

}
