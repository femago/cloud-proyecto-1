package co.edu.uniandes.cloud.service;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.service.dto.VoiceFileData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * Service Interface for managing Application.
 */
public interface ApplicationService {

    /**
     * Save a application.
     *
     * @param application the entity to save
     * @return the persisted entity
     */
    Application save(Application application);

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Application> findAll(Pageable pageable);

    /**
     * Get the "id" application.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Application findOne(Long id);

    /**
     * Delete the "id" application.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Page<Application> findConvertedByContest(Pageable pageable, Long contestId);

    Page<Application> findByContest(Pageable pageable, Long contestId);

    VoiceFileData fileConvertedVoice(Long id) throws IOException;

    VoiceFileData fileOriginalVoice(Long id) throws IOException;
}
