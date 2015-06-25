package com.ipponusa.boardatjob.repository;

import com.ipponusa.boardatjob.domain.Company;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Company entity.
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {

}
