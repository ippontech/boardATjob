package com.ippon.boardatjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippon.boardatjob.domain.Company;

/**
 * Spring Data JPA repository for the Company entity.
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
