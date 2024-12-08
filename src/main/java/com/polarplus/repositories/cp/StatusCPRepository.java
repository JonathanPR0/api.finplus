package com.polarplus.repositories.cp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cp.StatusCP;

@Repository
public interface StatusCPRepository extends JpaRepository<StatusCP, Long> {

}
