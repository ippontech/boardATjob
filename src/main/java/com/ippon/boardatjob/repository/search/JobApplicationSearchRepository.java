package com.ippon.boardatjob.repository.search;

import com.ippon.boardatjob.domain.JobApplication;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the JobApplication entity.
 */
public interface JobApplicationSearchRepository extends ElasticsearchRepository<JobApplication, Long> {
}
