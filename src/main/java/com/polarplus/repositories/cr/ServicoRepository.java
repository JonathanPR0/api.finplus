package com.polarplus.repositories.cr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cr.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

}
