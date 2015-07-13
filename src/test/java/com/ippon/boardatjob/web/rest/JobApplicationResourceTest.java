package com.ippon.boardatjob.web.rest;

import com.ippon.boardatjob.Application;
import com.ippon.boardatjob.domain.JobApplication;
import com.ippon.boardatjob.repository.JobApplicationRepository;
import com.ippon.boardatjob.repository.search.JobApplicationSearchRepository;

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
 * Test class for the JobApplicationResource REST controller.
 *
 * @see JobApplicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobApplicationResourceTest {

    private static final String DEFAULT_COVER_LETTER = "SAMPLE_TEXT";
    private static final String UPDATED_COVER_LETTER = "UPDATED_TEXT";

    @Inject
    private JobApplicationRepository jobApplicationRepository;

    @Inject
    private JobApplicationSearchRepository jobApplicationSearchRepository;

    private MockMvc restJobApplicationMockMvc;

    private JobApplication jobApplication;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobApplicationResource jobApplicationResource = new JobApplicationResource();
        ReflectionTestUtils.setField(jobApplicationResource, "jobApplicationRepository", jobApplicationRepository);
        ReflectionTestUtils.setField(jobApplicationResource, "jobApplicationSearchRepository", jobApplicationSearchRepository);
        this.restJobApplicationMockMvc = MockMvcBuilders.standaloneSetup(jobApplicationResource).build();
    }

    @Before
    public void initTest() {
        jobApplication = new JobApplication();
        jobApplication.setCoverLetter(DEFAULT_COVER_LETTER);
    }

    @Test
    @Transactional
    public void createJobApplication() throws Exception {
        int databaseSizeBeforeCreate = jobApplicationRepository.findAll().size();

        // Create the JobApplication
        restJobApplicationMockMvc.perform(post("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isCreated());

        // Validate the JobApplication in the database
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(databaseSizeBeforeCreate + 1);
        JobApplication testJobApplication = jobApplications.get(jobApplications.size() - 1);
        assertThat(testJobApplication.getCoverLetter()).isEqualTo(DEFAULT_COVER_LETTER);
    }

    @Test
    @Transactional
    public void getAllJobApplications() throws Exception {
        // Initialize the database
        jobApplicationRepository.saveAndFlush(jobApplication);

        // Get all the jobApplications
        restJobApplicationMockMvc.perform(get("/api/jobApplications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobApplication.getId().intValue())))
                .andExpect(jsonPath("$.[*].coverLetter").value(hasItem(DEFAULT_COVER_LETTER.toString())));
    }

    @Test
    @Transactional
    public void getJobApplication() throws Exception {
        // Initialize the database
        jobApplicationRepository.saveAndFlush(jobApplication);

        // Get the jobApplication
        restJobApplicationMockMvc.perform(get("/api/jobApplications/{id}", jobApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(jobApplication.getId().intValue()))
            .andExpect(jsonPath("$.coverLetter").value(DEFAULT_COVER_LETTER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobApplication() throws Exception {
        // Get the jobApplication
        restJobApplicationMockMvc.perform(get("/api/jobApplications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobApplication() throws Exception {
        // Initialize the database
        jobApplicationRepository.saveAndFlush(jobApplication);

		int databaseSizeBeforeUpdate = jobApplicationRepository.findAll().size();

        // Update the jobApplication
        jobApplication.setCoverLetter(UPDATED_COVER_LETTER);
        restJobApplicationMockMvc.perform(put("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isOk());

        // Validate the JobApplication in the database
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(databaseSizeBeforeUpdate);
        JobApplication testJobApplication = jobApplications.get(jobApplications.size() - 1);
        assertThat(testJobApplication.getCoverLetter()).isEqualTo(UPDATED_COVER_LETTER);
    }

    @Test
    @Transactional
    public void deleteJobApplication() throws Exception {
        // Initialize the database
        jobApplicationRepository.saveAndFlush(jobApplication);

		int databaseSizeBeforeDelete = jobApplicationRepository.findAll().size();

        // Get the jobApplication
        restJobApplicationMockMvc.perform(delete("/api/jobApplications/{id}", jobApplication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
