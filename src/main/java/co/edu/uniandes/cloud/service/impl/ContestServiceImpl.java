package co.edu.uniandes.cloud.service.impl;

import co.edu.uniandes.cloud.domain.User;
import co.edu.uniandes.cloud.repository.UserRepository;
import co.edu.uniandes.cloud.security.SecurityUtils;
import co.edu.uniandes.cloud.service.ContestService;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.repository.ContestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;


/**
 * Service Implementation for managing Contest.
 */
@Service
@Transactional
public class ContestServiceImpl implements ContestService {

    private final Logger log = LoggerFactory.getLogger(ContestServiceImpl.class);

    private final ContestRepository contestRepository;
    private final UserRepository userRepository;

    public ContestServiceImpl(ContestRepository contestRepository, UserRepository userRepository) {
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a contest.
     *
     * @param contest the entity to save
     * @return the persisted entity
     */
    @Override
    public Contest save(Contest contest) {
        contest.setCreateDate(Instant.now());
        Optional<User> oneByLogin = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get());
        contest.setUser(oneByLogin.get());
        log.debug("Request to save Contest : {}", contest);
        return contestRepository.save(contest);
    }

    /**
     * Get all the contests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Contest> findAll(Pageable pageable) {
        log.debug("Request to get all Contests");
        return contestRepository.findAll(pageable);
    }

    /**
     * Get one contest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Contest findOne(Long id) {
        log.debug("Request to get Contest : {}", id);
        return contestRepository.findOne(id);
    }

    /**
     * Delete the contest by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Contest : {}", id);
        contestRepository.delete(id);
    }
}
