package com.ippon.boardatjob.repository;

import com.ippon.boardatjob.domain.UserProfile;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

}
