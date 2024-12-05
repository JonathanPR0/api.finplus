package com.polarplus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
