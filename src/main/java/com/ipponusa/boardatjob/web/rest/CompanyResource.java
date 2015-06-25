package com.ipponusa.boardatjob.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ipponusa.boardatjob.domain.Company;
import com.ipponusa.boardatjob.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    @Inject
    private CompanyRepository companyRepository;

    /**
     * POST  /companys -> Create a new company.
     */
    @RequestMapping(value = "/companys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new company cannot already have an ID").build();
        }
        companyRepository.save(company);
        return ResponseEntity.created(new URI("/api/companys/" + company.getId())).build();
    }

    /**
     * PUT  /companys -> Updates an existing company.
     */
    @RequestMapping(value = "/companys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to update Company : {}", company);
        if (company.getId() == null) {
            return create(company);
        }
        companyRepository.save(company);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /companys -> get all the companys.
     */
    @RequestMapping(value = "/companys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Company> getAll() {
        log.debug("REST request to get all Companys");
        return companyRepository.findAll();
    }

    /**
     * GET  /companys/:id -> get the "id" company.
     */
    @RequestMapping(value = "/companys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Company> get(@PathVariable Long id) {
        log.debug("REST request to get Company : {}", id);
        return Optional.ofNullable(companyRepository.findOne(id))
            .map(company -> new ResponseEntity<>(
                company,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /companys/:id -> delete the "id" company.
     */
    @RequestMapping(value = "/companys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyRepository.delete(id);
    }
}
