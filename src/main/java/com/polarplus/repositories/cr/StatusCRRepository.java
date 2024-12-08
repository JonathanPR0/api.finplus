package com.polarplus.repositories.cr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.polarplus.domain.cr.StatusCR;

@Repository
public interface StatusCRRepository extends JpaRepository<StatusCR, Long> {

}
