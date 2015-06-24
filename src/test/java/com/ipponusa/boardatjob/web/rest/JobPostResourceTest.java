package com.ipponusa.boardatjob.web.rest;

import com.ipponusa.boardatjob.Application;
import com.ipponusa.boardatjob.domain.JobPost;
import com.ipponusa.boardatjob.repository.JobPostRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobPostResource REST controller.
 *
 * @see JobPostResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobPostResourceTest {

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_REQUIREMENTS = "SAMPLE_TEXT";
    private static final String UPDATED_REQUIREMENTS = "UPDATED_TEXT";

    @Inject
    private JobPostRepository jobPostRepository;

    private MockMvc restJobPostMockMvc;

    private JobPost jobPost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobPostResource jobPostResource = new JobPostResource();
        ReflectionTestUtils.setField(jobPostResource, "jobPostRepository", jobPostRepository);
        this.restJobPostMockMvc = MockMvcBuilders.standaloneSetup(jobPostResource).build();
    }

    @Before
    public void initTest() {
        jobPost = new JobPost();
        jobPost.setTitle(DEFAULT_TITLE);
        jobPost.setDescription(DEFAULT_DESCRIPTION);
        jobPost.setRequirements(DEFAULT_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void createJobPost() throws Exception {
        int databaseSizeBeforeCreate = jobPostRepository.findAll().size();

        // Create the JobPost
        restJobPostMockMvc.perform(post("/api/jobPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobPost)))
                .andExpect(status().isCreated());

        // Validate the JobPost in the database
        List<JobPost> jobPosts = jobPostRepository.findAll();
        assertThat(jobPosts).hasSize(databaseSizeBeforeCreate + 1);
        JobPost testJobPost = jobPosts.get(jobPosts.size() - 1);
        assertThat(testJobPost.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobPost.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJobPost.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void getAllJobPosts() throws Exception {
        // Initialize the database
        jobPostRepository.saveAndFlush(jobPost);

        // Get all the jobPosts
        restJobPostMockMvc.perform(get("/api/jobPosts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobPost.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())));
    }

    @Test
    @Transactional
    public void getJobPost() throws Exception {
        // Initialize the database
        jobPostRepository.saveAndFlush(jobPost);

        // Get the jobPost
        restJobPostMockMvc.perform(get("/api/jobPosts/{id}", jobPost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobPost.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobPost() throws Exception {
        // Get the jobPost
        restJobPostMockMvc.perform(get("/api/jobPosts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobPost() throws Exception {
        // Initialize the database
        jobPostRepository.saveAndFlush(jobPost);

		int databaseSizeBeforeUpdate = jobPostRepository.findAll().size();

        // Update the jobPost
        jobPost.setTitle(UPDATED_TITLE);
        jobPost.setDescription(UPDATED_DESCRIPTION);
        jobPost.setRequirements(UPDATED_REQUIREMENTS);
        restJobPostMockMvc.perform(put("/api/jobPosts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobPost)))
                .andExpect(status().isOk());

        // Validate the JobPost in the database
        List<JobPost> jobPosts = jobPostRepository.findAll();
        assertThat(jobPosts).hasSize(databaseSizeBeforeUpdate);
        JobPost testJobPost = jobPosts.get(jobPosts.size() - 1);
        assertThat(testJobPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobPost.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJobPost.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void deleteJobPost() throws Exception {
        // Initialize the database
        jobPostRepository.saveAndFlush(jobPost);

		int databaseSizeBeforeDelete = jobPostRepository.findAll().size();

        // Get the jobPost
        restJobPostMockMvc.perform(delete("/api/jobPosts/{id}", jobPost.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobPost> jobPosts = jobPostRepository.findAll();
        assertThat(jobPosts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
