package com.ippon.boardatjob.repository;

import com.ippon.boardatjob.domain.Company;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {

}
