package com.ippon.boardatjob.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.boardatjob.domain.Job;
import com.ippon.boardatjob.repository.JobRepository;
import com.ippon.boardatjob.repository.search.JobSearchRepository;
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
 * REST controller for managing Job.
 */
@RestController
@RequestMapping("/api")
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    @Inject
    private JobRepository jobRepository;

    @Inject
    private JobSearchRepository jobSearchRepository;

    /**
     * POST  /jobs -> Create a new job.
     */
    @RequestMapping(value = "/jobs",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (job.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new job cannot already have an ID").build();
        }
        jobRepository.save(job);
        jobSearchRepository.save(job);
        return ResponseEntity.created(new URI("/api/jobs/" + job.getId())).build();
    }

    /**
     * PUT  /jobs -> Updates an existing job.
     */
    @RequestMapping(value = "/jobs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to update Job : {}", job);
        if (job.getId() == null) {
            return create(job);
        }
        jobRepository.save(job);
        jobSearchRepository.save(job);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jobs -> get all the jobs.
     */
    @RequestMapping(value = "/jobs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Job> getAll() {
        log.debug("REST request to get all Jobs");
        return jobRepository.findAll();
    }

    /**
     * GET  /jobs/:id -> get the "id" job.
     */
    @RequestMapping(value = "/jobs/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Job> get(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);
        return Optional.ofNullable(jobRepository.findOne(id))
            .map(job -> new ResponseEntity<>(
                job,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobs/:id -> delete the "id" job.
     */
    @RequestMapping(value = "/jobs/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobRepository.delete(id);
        jobSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/jobs/:query -> search for the job corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/jobs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Job> search(@PathVariable String query) {
        return StreamSupport
            .stream(jobSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
