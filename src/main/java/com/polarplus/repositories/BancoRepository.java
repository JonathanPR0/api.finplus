package com.polarplus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Banco;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    boolean existsByNome(String nome);

    boolean existsByCodigo(String codCodigo);
}
