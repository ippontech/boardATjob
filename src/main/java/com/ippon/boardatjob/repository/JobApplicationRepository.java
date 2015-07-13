package com.ippon.boardatjob.repository;

import com.ippon.boardatjob.domain.JobApplication;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobApplication entity.
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

}
