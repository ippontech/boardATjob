package com.ippon.boardatjob.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.boardatjob.domain.JobApplication;
import com.ippon.boardatjob.repository.JobApplicationRepository;
import com.ippon.boardatjob.repository.search.JobApplicationSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing JobApplication.
 */
@RestController
@RequestMapping("/api")
public class JobApplicationResource {

    private final Logger log = LoggerFactory.getLogger(JobApplicationResource.class);

    @Inject
    private JobApplicationRepository jobApplicationRepository;

    @Inject
    private JobApplicationSearchRepository jobApplicationSearchRepository;

    /**
     * POST  /jobApplications -> Create a new jobApplication.
     */
    @RequestMapping(value = "/jobApplications",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody JobApplication jobApplication) throws URISyntaxException {
        log.debug("REST request to save JobApplication : {}", jobApplication);
        if (jobApplication.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jobApplication cannot already have an ID").build();
        }
        jobApplicationRepository.save(jobApplication);
        jobApplicationSearchRepository.save(jobApplication);
        return ResponseEntity.created(new URI("/api/jobApplications/" + jobApplication.getId())).build();
    }

    /**
     * PUT  /jobApplications -> Updates an existing jobApplication.
     */
    @RequestMapping(value = "/jobApplications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody JobApplication jobApplication) throws URISyntaxException {
        log.debug("REST request to update JobApplication : {}", jobApplication);
        if (jobApplication.getId() == null) {
            return create(jobApplication);
        }
        jobApplicationRepository.save(jobApplication);
        jobApplicationSearchRepository.save(jobApplication);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jobApplications -> get all the jobApplications.
     */
    @RequestMapping(value = "/jobApplications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobApplication> getAll() {
        log.debug("REST request to get all JobApplications");
        return jobApplicationRepository.findAll();
    }

    /**
     * GET  /jobApplications/:id -> get the "id" jobApplication.
     */
    @RequestMapping(value = "/jobApplications/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobApplication> get(@PathVariable Long id) {
        log.debug("REST request to get JobApplication : {}", id);
        return Optional.ofNullable(jobApplicationRepository.findOne(id))
            .map(jobApplication -> new ResponseEntity<>(
                jobApplication,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobApplications/:id -> delete the "id" jobApplication.
     */
    @RequestMapping(value = "/jobApplications/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete JobApplication : {}", id);
        jobApplicationRepository.delete(id);
        jobApplicationSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/jobApplications/:query -> search for the jobApplication corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jobApplications/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<JobApplication> search(@PathVariable String query) {
        return StreamSupport
            .stream(jobApplicationSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
