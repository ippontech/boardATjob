package com.ippon.boardatjob.repository;

import com.ippon.boardatjob.domain.Job;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Job entity.
 */
public interface JobRepository extends JpaRepository<Job,Long> {

}
