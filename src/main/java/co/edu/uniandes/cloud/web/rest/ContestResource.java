package co.edu.uniandes.cloud.web.rest;

import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.service.ContestService;
import co.edu.uniandes.cloud.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.cloud.web.rest.util.HeaderUtil;
import co.edu.uniandes.cloud.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Contest.
 */
@RestController
@RequestMapping("/api")
public class ContestResource {

    private final Logger log = LoggerFactory.getLogger(ContestResource.class);

    private static final String ENTITY_NAME = "contest";

    private final ContestService contestService;

    public ContestResource(ContestService contestService) {
        this.contestService = contestService;
    }

    /**
     * POST  /contests : Create a new contest.
     *
     * @param contest the contest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contest, or with status 400 (Bad Request) if the contest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contests")
    @Timed
    public ResponseEntity<Contest> createContest(@Valid @RequestBody Contest contest) throws URISyntaxException {
        log.debug("REST request to save Contest : {}", contest);
        if (contest.getId() != null) {
            throw new BadRequestAlertException("A new contest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contest result = contestService.save(contest);
        return ResponseEntity.created(new URI("/api/contests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contests : Updates an existing contest.
     *
     * @param contest the contest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contest,
     * or with status 400 (Bad Request) if the contest is not valid,
     * or with status 500 (Internal Server Error) if the contest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contests")
    @Timed
    public ResponseEntity<Contest> updateContest(@Valid @RequestBody Contest contest) throws URISyntaxException {
        log.debug("REST request to update Contest : {}", contest);
        if (contest.getId() == null) {
            return createContest(contest);
        }
        Contest result = contestService.save(contest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contests : get all the contests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests")
    @Timed
    public ResponseEntity<List<Contest>> getAllContests(Pageable pageable) {
        log.debug("REST request to get a page of Contests");
        pageable = PaginationUtil.adjustPageable(pageable);
        Page<Contest> page = contestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contests/principal : get the contest by principal.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contests in body
     */
    @GetMapping("/contests/principal")
    @Timed
    public ResponseEntity<List<Contest>> getByPrincipal(Pageable pageable) {
        log.debug("REST request to get a page of Contests by Principal");
        Page<Contest> page = contestService.findByCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contests/principal");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contests/:id : get the "id" contest.
     *
     * @param id the id of the contest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contest, or with status 404 (Not Found)
     */
    @GetMapping("/contests/{id}")
    @Timed
    public ResponseEntity<Contest> getContest(@PathVariable String id) {
        log.debug("REST request to get Contest : {}", id);
        Contest contest = contestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contest));
    }

    /**
     * GET  /contests/uuid/:uuid : get the "uuid" contest.
     *
     * @param uuid the uuid of the contest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contest, or with status 404 (Not Found)
     */
    @GetMapping("/contests/uuid/{uuid}")
    @Timed
    public ResponseEntity<Contest> getContestByUUID(@PathVariable String uuid) {
        log.debug("REST request to get Contest : {}", uuid);
        Contest contest = contestService.findByUUID(uuid);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contest));
    }

    /**
     * DELETE  /contests/:id : delete the "id" contest.
     *
     * @param id the id of the contest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contests/{id}")
    @Timed
    public ResponseEntity<Void> deleteContest(@PathVariable String id) {
        log.debug("REST request to delete Contest : {}", id);
        contestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
