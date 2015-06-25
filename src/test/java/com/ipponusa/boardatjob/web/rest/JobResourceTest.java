package com.ipponusa.boardatjob.web.rest;

import com.ipponusa.boardatjob.Application;
import com.ipponusa.boardatjob.domain.Job;
import com.ipponusa.boardatjob.repository.JobRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the JobResource REST controller.
 *
 * @see JobResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class JobResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    private static final String DEFAULT_RESPONSIBILITIES = "SAMPLE_TEXT";
    private static final String UPDATED_RESPONSIBILITIES = "UPDATED_TEXT";
    private static final String DEFAULT_REQUIREMENTS = "SAMPLE_TEXT";
    private static final String UPDATED_REQUIREMENTS = "UPDATED_TEXT";

    private static final DateTime DEFAULT_POST_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_POST_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_POST_DATE_STR = dateTimeFormatter.print(DEFAULT_POST_DATE);

    @Inject
    private JobRepository jobRepository;

    private MockMvc restJobMockMvc;

    private Job job;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobResource jobResource = new JobResource();
        ReflectionTestUtils.setField(jobResource, "jobRepository", jobRepository);
        this.restJobMockMvc = MockMvcBuilders.standaloneSetup(jobResource).build();
    }

    @Before
    public void initTest() {
        job = new Job();
        job.setTitle(DEFAULT_TITLE);
        job.setDescription(DEFAULT_DESCRIPTION);
        job.setResponsibilities(DEFAULT_RESPONSIBILITIES);
        job.setRequirements(DEFAULT_REQUIREMENTS);
        job.setPostDate(DEFAULT_POST_DATE);
    }

    @Test
    @Transactional
    public void createJob() throws Exception {
        int databaseSizeBeforeCreate = jobRepository.findAll().size();

        // Create the Job
        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isCreated());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeCreate + 1);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJob.getResponsibilities()).isEqualTo(DEFAULT_RESPONSIBILITIES);
        assertThat(testJob.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
        assertThat(testJob.getPostDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_POST_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(jobRepository.findAll()).hasSize(0);
        // set the field null
        job.setTitle(null);

        // Create the Job, which fails.
        restJobMockMvc.perform(post("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllJobs() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get all the jobs
        restJobMockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(job.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].responsibilities").value(hasItem(DEFAULT_RESPONSIBILITIES.toString())))
                .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())))
                .andExpect(jsonPath("$.[*].postDate").value(hasItem(DEFAULT_POST_DATE_STR)));
    }

    @Test
    @Transactional
    public void getJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", job.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(job.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.responsibilities").value(DEFAULT_RESPONSIBILITIES.toString()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()))
            .andExpect(jsonPath("$.postDate").value(DEFAULT_POST_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingJob() throws Exception {
        // Get the job
        restJobMockMvc.perform(get("/api/jobs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

		int databaseSizeBeforeUpdate = jobRepository.findAll().size();

        // Update the job
        job.setTitle(UPDATED_TITLE);
        job.setDescription(UPDATED_DESCRIPTION);
        job.setResponsibilities(UPDATED_RESPONSIBILITIES);
        job.setRequirements(UPDATED_REQUIREMENTS);
        job.setPostDate(UPDATED_POST_DATE);
        restJobMockMvc.perform(put("/api/jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(job)))
                .andExpect(status().isOk());

        // Validate the Job in the database
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeUpdate);
        Job testJob = jobs.get(jobs.size() - 1);
        assertThat(testJob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJob.getResponsibilities()).isEqualTo(UPDATED_RESPONSIBILITIES);
        assertThat(testJob.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
        assertThat(testJob.getPostDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_POST_DATE);
    }

    @Test
    @Transactional
    public void deleteJob() throws Exception {
        // Initialize the database
        jobRepository.saveAndFlush(job);

		int databaseSizeBeforeDelete = jobRepository.findAll().size();

        // Get the job
        restJobMockMvc.perform(delete("/api/jobs/{id}", job.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Job> jobs = jobRepository.findAll();
        assertThat(jobs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
