package com.ippon.boardatjob.repository.search;

import com.ippon.boardatjob.domain.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserProfile entity.
 */
public interface UserProfileSearchRepository extends ElasticsearchRepository<UserProfile, Long> {
}
