package com.polarplus.repositories.cr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cr.PagamentoCR;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoCR, Long> {

}
