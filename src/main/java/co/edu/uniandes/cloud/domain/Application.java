package co.edu.uniandes.cloud.domain;


import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.dynamo.DynamoConverters;
import co.edu.uniandes.cloud.repository.jpa.JpaConverters;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Application.
 */
@Entity
@Table(name = Application.TABLE_NAME)
@DynamoDBTable(tableName = Application.TABLE_NAME)
@ToString
public class Application implements Serializable {
    public static final String TABLE_NAME = "application";
    private static final long serialVersionUID = 1L;

    public Application() {
    }

    public Application(String id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Convert(converter = JpaConverters.IdStringConverter.class)
    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @Column(name = "create_date", nullable = false)
    @DynamoDBTypeConverted(converter = DynamoConverters.InstantConverter.class)
    private Instant createDate;

    @NotNull
    @Column(name = "name", nullable = false)
    @DynamoDBAttribute
    private String name;

    @NotNull
    @Column(name = "lastname", nullable = false)
    @DynamoDBAttribute
    private String lastname;

    @NotNull
    @Column(name = "email", nullable = false)
    @DynamoDBAttribute
    private String email;

    @NotNull
    @Lob
    @Column(name = "original_record", nullable = false)
    @DynamoDBIgnore
    private byte[] originalRecord;

    @Column(name = "original_record_content_type", nullable = false)
    @DynamoDBAttribute
    private String originalRecordContentType;

    @Column(name = "notes")
    @DynamoDBAttribute
    private String notes;

    @Column(name = "original_record_location")
    @DynamoDBAttribute
    private String originalRecordLocation;

    @Column(name = "converted_record_location")
    @DynamoDBAttribute
    private String convertedRecordLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @DynamoDBMarshalling(marshallerClass = DynamoConverters.ApplicationStateConverter.class)
    private ApplicationState status;

    @ManyToOne
    @DynamoDBMarshalling(marshallerClass = DynamoConverters.ContestConverter.class)
    private Contest contest;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
