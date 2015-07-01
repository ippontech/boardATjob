package com.ipponusa.boardatjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipponusa.boardatjob.domain.Job;

/**
 * Spring Data JPA repository for the Job entity.
 */
public interface JobRepository extends JpaRepository<Job,Long> {

}
