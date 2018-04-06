package co.edu.uniandes.cloud.service;

import co.edu.uniandes.cloud.domain.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Contest.
 */
public interface ContestService {

    /**
     * Save a contest.
     *
     * @param contest the entity to save
     * @return the persisted entity
     */
    Contest save(Contest contest);

    /**
     * Get all the contests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Contest> findAll(Pageable pageable);

    /**
     * Get the "id" contest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Contest findOne(String id);

    /**
     * Delete the "id" contest.
     *
     * @param id the id of the entity
     */
    void delete(String id);

    Page<Contest> findByCurrentUser(Pageable pageable);

    Contest findByUUID(String uuid);
}
