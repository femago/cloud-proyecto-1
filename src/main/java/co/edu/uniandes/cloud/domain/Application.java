package co.edu.uniandes.cloud.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;

/**
 * A Application.
 */
@Entity
@Table(name = "application")
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Lob
    @Column(name = "original_record", nullable = false)
    private byte[] originalRecord;

    @Column(name = "original_record_content_type", nullable = false)
    private String originalRecordContentType;

    @Column(name = "notes")
    private String notes;

    @Column(name = "original_record_location")
    private String originalRecordLocation;

    @Column(name = "converted_record_location")
    private String convertedRecordLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationState status;

    @ManyToOne
    private Contest contest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Application createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public Application name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public Application lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public Application email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getOriginalRecord() {
        return originalRecord;
    }

    public Application originalRecord(byte[] originalRecord) {
        this.originalRecord = originalRecord;
        return this;
    }

    public void setOriginalRecord(byte[] originalRecord) {
        this.originalRecord = originalRecord;
    }

    public String getOriginalRecordContentType() {
        return originalRecordContentType;
    }

    public Application originalRecordContentType(String originalRecordContentType) {
        this.originalRecordContentType = originalRecordContentType;
        return this;
    }

    public void setOriginalRecordContentType(String originalRecordContentType) {
        this.originalRecordContentType = originalRecordContentType;
    }

    public String getNotes() {
        return notes;
    }

    public Application notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOriginalRecordLocation() {
        return originalRecordLocation;
    }

    public Application originalRecordLocation(String originalRecordLocation) {
        this.originalRecordLocation = originalRecordLocation;
        return this;
    }

    public void setOriginalRecordLocation(String originalRecordLocation) {
        this.originalRecordLocation = originalRecordLocation;
    }

    public String getConvertedRecordLocation() {
        return convertedRecordLocation;
    }

    public Application convertedRecordLocation(String convertedRecordLocation) {
        this.convertedRecordLocation = convertedRecordLocation;
        return this;
    }

    public void setConvertedRecordLocation(String convertedRecordLocation) {
        this.convertedRecordLocation = convertedRecordLocation;
    }

    public ApplicationState getStatus() {
        return status;
    }

    public Application status(ApplicationState status) {
        this.status = status;
        return this;
    }

    public void setStatus(ApplicationState status) {
        this.status = status;
    }

    public Contest getContest() {
        return contest;
    }

    public Application contest(Contest contest) {
        this.contest = contest;
        return this;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Application application = (Application) o;
        if (application.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), application.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Application{" +
            "id=" + getId() +
            ", createDate='" + getCreateDate() + "'" +
            ", name='" + getName() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", originalRecord='" + getOriginalRecord() + "'" +
            ", originalRecordContentType='" + getOriginalRecordContentType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", originalRecordLocation='" + getOriginalRecordLocation() + "'" +
            ", convertedRecordLocation='" + getConvertedRecordLocation() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
