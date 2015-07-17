package com.ippon.boardatjob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippon.boardatjob.domain.Job;

/**
 * Spring Data JPA repository for the Job entity.
 */
public interface JobRepository extends JpaRepository<Job,Long> {

	public List<Job> findAllByCompanyId(Long companyId);
}
