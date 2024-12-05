package com.polarplus.domain.cr;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polarplus.domain.Empresa;
import com.polarplus.domain.enums.TipoCliente;

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
@Table(name = "fin_cr_clientes", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "nome", "numero", "endereco", "cnpj" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, length = 15)
    private String numero;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String endereco;

    @Column(unique = true, length = 14)
    private String cnpj;

    @Enumerated(EnumType.STRING) // Usando EnumType.STRING para armazenar o nome do enum no banco
    @Column(nullable = false)
    private TipoCliente tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;
}
