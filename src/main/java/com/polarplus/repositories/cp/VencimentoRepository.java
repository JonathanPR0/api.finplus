package com.polarplus.repositories.cp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cp.Vencimento;

@Repository
public interface VencimentoRepository extends JpaRepository<Vencimento, Long> {

}
