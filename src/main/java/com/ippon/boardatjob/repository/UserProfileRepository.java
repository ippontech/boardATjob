package com.ippon.boardatjob.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ippon.boardatjob.domain.UserProfile;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

	public UserProfile findOneByLogin(String login);
}
