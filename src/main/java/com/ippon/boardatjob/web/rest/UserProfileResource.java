package com.ippon.boardatjob.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.boardatjob.domain.UserProfile;
import com.ippon.boardatjob.repository.UserProfileRepository;
import com.ippon.boardatjob.repository.search.UserProfileSearchRepository;
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
 * REST controller for managing UserProfile.
 */
@RestController
@RequestMapping("/api")
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private UserProfileSearchRepository userProfileSearchRepository;

    /**
     * POST  /userProfiles -> Create a new userProfile.
     */
    @RequestMapping(value = "/userProfiles",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userProfile cannot already have an ID").build();
        }
        userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(userProfile);
        return ResponseEntity.created(new URI("/api/userProfiles/" + userProfile.getId())).build();
    }

    /**
     * PUT  /userProfiles -> Updates an existing userProfile.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.getId() == null) {
            return create(userProfile);
        }
        userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(userProfile);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /userProfiles -> get all the userProfiles.
     */
    @RequestMapping(value = "/userProfiles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserProfile> getAll() {
        log.debug("REST request to get all UserProfiles");
        return userProfileRepository.findAll();
    }

    /**
     * GET  /userProfiles/:id -> get the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> get(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        return Optional.ofNullable(userProfileRepository.findOne(id))
            .map(userProfile -> new ResponseEntity<>(
                userProfile,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userProfiles/:id -> delete the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileRepository.delete(id);
        userProfileSearchRepository.delete(id);
    }

    /**
     * SEARCH  /_search/userProfiles/:query -> search for the userProfile corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userProfiles/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserProfile> search(@PathVariable String query) {
        return StreamSupport
            .stream(userProfileSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
