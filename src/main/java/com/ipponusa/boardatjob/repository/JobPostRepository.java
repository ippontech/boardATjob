package com.ipponusa.boardatjob.repository;

import com.ipponusa.boardatjob.domain.JobPost;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the JobPost entity.
 */
public interface JobPostRepository extends JpaRepository<JobPost,Long> {

}
