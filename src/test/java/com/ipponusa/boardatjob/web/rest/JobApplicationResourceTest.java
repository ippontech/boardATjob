package com.ipponusa.boardatjob.web.rest;

import com.ipponusa.boardatjob.Application;
import com.ipponusa.boardatjob.domain.JobApplication;
import com.ipponusa.boardatjob.repository.JobApplicationRepository;

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

    private static final String DEFAULT_FIRST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_FIRST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_LAST_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_LAST_NAME = "UPDATED_TEXT";
    private static final String DEFAULT_COUNTRY = "SAMPLE_TEXT";
    private static final String UPDATED_COUNTRY = "UPDATED_TEXT";
    private static final String DEFAULT_ZIP_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_ZIP_CODE = "UPDATED_TEXT";
    private static final String DEFAULT_PHONE_NUMBER = "SAMPLE_TEXT";
    private static final String UPDATED_PHONE_NUMBER = "UPDATED_TEXT";

    private static final Boolean DEFAULT_IS_AUTHORIZED = false;
    private static final Boolean UPDATED_IS_AUTHORIZED = true;

    @Inject
    private JobApplicationRepository jobApplicationRepository;

    private MockMvc restJobApplicationMockMvc;

    private JobApplication jobApplication;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobApplicationResource jobApplicationResource = new JobApplicationResource();
        ReflectionTestUtils.setField(jobApplicationResource, "jobApplicationRepository", jobApplicationRepository);
        this.restJobApplicationMockMvc = MockMvcBuilders.standaloneSetup(jobApplicationResource).build();
    }

    @Before
    public void initTest() {
        jobApplication = new JobApplication();
        jobApplication.setFirstName(DEFAULT_FIRST_NAME);
        jobApplication.setLastName(DEFAULT_LAST_NAME);
        jobApplication.setCountry(DEFAULT_COUNTRY);
        jobApplication.setZipCode(DEFAULT_ZIP_CODE);
        jobApplication.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        jobApplication.setIsAuthorized(DEFAULT_IS_AUTHORIZED);
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
        assertThat(testJobApplication.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testJobApplication.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testJobApplication.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testJobApplication.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testJobApplication.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testJobApplication.getIsAuthorized()).isEqualTo(DEFAULT_IS_AUTHORIZED);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jobApplicationRepository.findAll()).hasSize(0);
        // set the field null
        jobApplication.setFirstName(null);

        // Create the JobApplication, which fails.
        restJobApplicationMockMvc.perform(post("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(0);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jobApplicationRepository.findAll()).hasSize(0);
        // set the field null
        jobApplication.setLastName(null);

        // Create the JobApplication, which fails.
        restJobApplicationMockMvc.perform(post("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(0);
    }

    @Test
    @Transactional
    public void checkCountryIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jobApplicationRepository.findAll()).hasSize(0);
        // set the field null
        jobApplication.setCountry(null);

        // Create the JobApplication, which fails.
        restJobApplicationMockMvc.perform(post("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(0);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jobApplicationRepository.findAll()).hasSize(0);
        // set the field null
        jobApplication.setZipCode(null);

        // Create the JobApplication, which fails.
        restJobApplicationMockMvc.perform(post("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(0);
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
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].isAuthorized").value(hasItem(DEFAULT_IS_AUTHORIZED.booleanValue())));
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
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.isAuthorized").value(DEFAULT_IS_AUTHORIZED.booleanValue()));
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
        jobApplication.setFirstName(UPDATED_FIRST_NAME);
        jobApplication.setLastName(UPDATED_LAST_NAME);
        jobApplication.setCountry(UPDATED_COUNTRY);
        jobApplication.setZipCode(UPDATED_ZIP_CODE);
        jobApplication.setPhoneNumber(UPDATED_PHONE_NUMBER);
        jobApplication.setIsAuthorized(UPDATED_IS_AUTHORIZED);
        restJobApplicationMockMvc.perform(put("/api/jobApplications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobApplication)))
                .andExpect(status().isOk());

        // Validate the JobApplication in the database
        List<JobApplication> jobApplications = jobApplicationRepository.findAll();
        assertThat(jobApplications).hasSize(databaseSizeBeforeUpdate);
        JobApplication testJobApplication = jobApplications.get(jobApplications.size() - 1);
        assertThat(testJobApplication.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testJobApplication.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testJobApplication.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testJobApplication.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testJobApplication.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testJobApplication.getIsAuthorized()).isEqualTo(UPDATED_IS_AUTHORIZED);
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
