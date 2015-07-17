package com.ippon.boardatjob.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ippon.boardatjob.domain.util.CustomDateTimeDeserializer;
import com.ippon.boardatjob.domain.util.CustomDateTimeSerializer;

/**
 * A JobApplication.
 */
@Entity
@Table(name = "JOBAPPLICATION")
@Document(indexName="jobapplication")
public class JobApplication implements Serializable {

	private static final long serialVersionUID = -138045489239648154L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cover_letter")
    private String coverLetter;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "application_date")
    private DateTime applicationDate;
    
    @ManyToOne
    @Field( type = FieldType.Nested)
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN) 
    @Field( type = FieldType.Nested)
    private UserProfile userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public DateTime getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(DateTime applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobApplication jobApplication = (JobApplication) o;

        if ( ! Objects.equals(id, jobApplication.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "id=" + id +
                ", coverLetter='" + coverLetter + "'" +
                '}';
    }
}
