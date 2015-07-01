package com.ipponusa.boardatjob.repository;

import com.ipponusa.boardatjob.domain.JobApplication;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobApplication entity.
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

    @Query("select jobApplication from JobApplication jobApplication where jobApplication.createdBy.login = ?#{principal.username}")
    List<JobApplication> findAllForCurrentUser();

}
