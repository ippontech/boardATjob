package com.ippon.boardatjob.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.ippon.boardatjob.domain.Job;
import com.ippon.boardatjob.repository.JobRepository;
import com.ippon.boardatjob.repository.search.JobSearchRepository;
import com.ipponusa.boardatjob.web.rest.util.PaginationUtil;

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

	@Inject
    private ElasticsearchTemplate elasticsearchTemplate;

	/**
	 * POST /jobs -> Create a new job.
	 */
	@RequestMapping(value = "/jobs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> create(@RequestBody Job job)
			throws URISyntaxException {
		log.debug("REST request to save Job : {}", job);
		if (job.getId() != null) {
			return ResponseEntity.badRequest()
					.header("Failure", "A new job cannot already have an ID")
					.build();
		}
		job.setDate(new DateTime());
		jobRepository.save(job);
		jobSearchRepository.save(job);
		return ResponseEntity.created(new URI("/api/jobs/" + job.getId()))
				.build();
	}

	/**
	 * PUT /jobs -> Updates an existing job.
	 */
	@RequestMapping(value = "/jobs", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> update(@RequestBody Job job)
			throws URISyntaxException {
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
    public ResponseEntity<List<Job>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
    	Pageable pageRequest = PaginationUtil.generatePageRequest(offset, limit, sortByPostDateDesc());
    	Page<Job> page = jobRepository.findAll(pageRequest);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

	/**
	 * GET /jobs/:id -> get the "id" job.
	 */
	@RequestMapping(value = "/jobs/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Job> get(@PathVariable Long id) {
		log.debug("REST request to get Job : {}", id);
		return Optional.ofNullable(jobRepository.findOne(id))
				.map(job -> new ResponseEntity<>(job, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /jobs/:id -> delete the "id" job.
	 */
	@RequestMapping(value = "/jobs/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete Job : {}", id);
		jobRepository.delete(id);
		jobSearchRepository.delete(id);
	}

	/**
	 * SEARCH /_search/jobs/:query -> search for the job corresponding to the
	 * query.
	 */
	@RequestMapping(value = "/_search/jobs/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Job> search(@PathVariable String query) {
		return StreamSupport.stream(
				jobSearchRepository.search(queryString(query)).spliterator(),
				false).collect(Collectors.toList());
	}

	private Sort sortByPostDateDesc() {
		return new Sort(Sort.Direction.DESC, "date");
	}
}
