package com.ipponusa.boardatjob.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ipponusa.boardatjob.domain.JobPost;
import com.ipponusa.boardatjob.repository.JobPostRepository;
import com.ipponusa.boardatjob.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing JobPost.
 */
@RestController
@RequestMapping("/api")
public class JobPostResource {

    private final Logger log = LoggerFactory.getLogger(JobPostResource.class);

    @Inject
    private JobPostRepository jobPostRepository;

    /**
     * POST  /jobPosts -> Create a new jobPost.
     */
    @RequestMapping(value = "/jobPosts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody JobPost jobPost) throws URISyntaxException {
        log.debug("REST request to save JobPost : {}", jobPost);
        if (jobPost.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new jobPost cannot already have an ID").build();
        }
        jobPostRepository.save(jobPost);
        return ResponseEntity.created(new URI("/api/jobPosts/" + jobPost.getId())).build();
    }

    /**
     * PUT  /jobPosts -> Updates an existing jobPost.
     */
    @RequestMapping(value = "/jobPosts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody JobPost jobPost) throws URISyntaxException {
        log.debug("REST request to update JobPost : {}", jobPost);
        if (jobPost.getId() == null) {
            return create(jobPost);
        }
        jobPostRepository.save(jobPost);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /jobPosts -> get all the jobPosts.
     */
    @RequestMapping(value = "/jobPosts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<JobPost>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<JobPost> page = jobPostRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobPosts", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobPosts/:id -> get the "id" jobPost.
     */
    @RequestMapping(value = "/jobPosts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobPost> get(@PathVariable Long id) {
        log.debug("REST request to get JobPost : {}", id);
        return Optional.ofNullable(jobPostRepository.findOne(id))
            .map(jobPost -> new ResponseEntity<>(
                jobPost,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /jobPosts/:id -> delete the "id" jobPost.
     */
    @RequestMapping(value = "/jobPosts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete JobPost : {}", id);
        jobPostRepository.delete(id);
    }
}
