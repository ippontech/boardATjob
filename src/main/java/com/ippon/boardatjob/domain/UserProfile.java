package com.ippon.boardatjob.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ippon.boardatjob.domain.util.CustomDateTimeDeserializer;
import com.ippon.boardatjob.domain.util.CustomDateTimeSerializer;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "USERPROFILE")
@Document(indexName="userprofile")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login")
    private String login;
    
    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "resume")
    @JsonIgnore
    private byte[] resume;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "resume_date")
    private DateTime resumeDate;

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    private Set<JobApplication> jobApplications = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = javax.persistence.CascadeType.ALL)
    @Cascade(CascadeType.ALL)
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getResume() {
        return resume;
    }

    public void setResume(byte[] resume) {
        this.resume = resume;
    }

    public DateTime getResumeDate() {
		return resumeDate;
	}

	public void setResumeDate(DateTime resumeDate) {
		this.resumeDate = resumeDate;
	}

	public Set<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(Set<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProfile userProfile = (UserProfile) o;

        if ( ! Objects.equals(id, userProfile.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", email='" + email + "'" +
                ", name='" + name + "'" +
                ", phoneNumber='" + phoneNumber + "'" +
                ", resume='" + resume + "'" +
                ", resumeDate='" + resumeDate + "'" +
                '}';
    }
}
