package com.ippon.boardatjob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ippon.boardatjob.domain.JobApplication;

/**
 * Spring Data JPA repository for the JobApplication entity.
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

	@Query("SELECT ja FROM JobApplication ja, Job j, UserProfile p "
			+ "WHERE ja.userProfile.id = p.id"
			+ "	AND ja.job.id = j.id"
			+ " AND p.login = :login"
			+ " AND j.id = :jobId"
			)
	public JobApplication findByJobAndUserLogin(@Param("jobId") Long jobId, @Param("login") String login);
	
	
	@Query("SELECT ja FROM JobApplication ja left join fetch ja.userProfile p"
			+ " left join ja.job j "
			+ " WHERE j.company.id = :companyId"
			)
	public List<JobApplication> findAllByCompany(@Param("companyId") Long companyId);
}
