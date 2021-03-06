package co.edu.uniandes.cloud.web.rest;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.service.ApplicationService;
import co.edu.uniandes.cloud.service.dto.VoiceFileData;
import co.edu.uniandes.cloud.web.rest.errors.BadRequestAlertException;
import co.edu.uniandes.cloud.web.rest.util.HeaderUtil;
import co.edu.uniandes.cloud.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Application.
 */
@RestController
@RequestMapping("/api")
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    private static final String ENTITY_NAME = "application";

    private final ApplicationService applicationService;

    public ApplicationResource(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * POST  /applications : Create a new application.
     *
     * @param application the application to create
     * @return the ResponseEntity with status 201 (Created) and with body the new application, or with status 400 (Bad Request) if the application has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/applications")
    @Timed
    public ResponseEntity<Application> createApplication(@Valid @RequestBody Application application) throws URISyntaxException {
        log.debug("REST request to save Application : {}", application);
        if (application.getId() != null) {
            throw new BadRequestAlertException("A new application cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (application.getContest() == null || application.getContest().getId() == null) {
            throw new BadRequestAlertException("A new application requires a contest", ENTITY_NAME, "contestrq");
        }
        Application result = applicationService.save(application);
        return ResponseEntity.created(new URI("/api/applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /applications : Updates an existing application.
     *
     * @param application the application to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated application,
     * or with status 400 (Bad Request) if the application is not valid,
     * or with status 500 (Internal Server Error) if the application couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/applications")
    @Timed
    public ResponseEntity<Application> updateApplication(@Valid @RequestBody Application application) throws URISyntaxException {
        log.debug("REST request to update Application : {}", application);
        if (application.getId() == null) {
            return createApplication(application);
        }
        Application result = applicationService.save(application);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, application.getId().toString()))
            .body(result);
    }

    /**
     * GET  /applications : get all the applications.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of applications in body
     */
    @GetMapping("/applications")
    @Timed
    public ResponseEntity<List<Application>> getAllApplications(Pageable pageable) {
        log.debug("REST request to get a page of Applications");
        Page<Application> page = applicationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applications");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /applications/:id : get the "id" application.
     *
     * @param id the id of the application to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the application, or with status 404 (Not Found)
     */
    @GetMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Application> getApplication(@PathVariable String id) {
        log.debug("REST request to get Application : {}", id);
        Application application = applicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(application));
    }

    /**
     * DELETE  /applications/:id : delete the "id" application.
     *
     * @param id the id of the application to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/applications/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        log.debug("REST request to delete Application : {}", id);
        applicationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * Public
     * @param pageable
     * @param contestId
     * @return
     */
    @GetMapping("/applications/contests/{contestId}")
    @Timed
    public ResponseEntity<List<Application>> getApplicationsByContest(Pageable pageable, @PathVariable String contestId) {
        log.debug("REST request to get a list of Applications by Contest");
        pageable = PaginationUtil.adjustPageable(pageable);
        Page<Application> page = applicationService.findConvertedByContest(pageable, contestId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applications/contests/" + contestId);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Private
     * @param pageable
     * @param contestId
     * @return
     */
    @GetMapping("/applications/contests/{contestId}/principal")
    @Timed
    public ResponseEntity<List<Application>> getApplicationsByContestPrincipal(Pageable pageable, @PathVariable String contestId) {
        log.debug("REST request to get a page of Applications by Contest principal");
        pageable = PaginationUtil.adjustPageable(pageable);
        Page<Application> page = applicationService.findByContest(pageable, contestId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/applications/contests/" + contestId + "/principal");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/applications/{id}/voice/original")
    @Timed
    public ResponseEntity<Resource> downloadOriginalVoice(@PathVariable String id) throws IOException {
        log.debug("REST request to get original voice for Application : {}", id);
        try {
            final VoiceFileData voiceFileData = applicationService.fileOriginalVoice(id);
            return ResponseEntity.ok()
                .contentLength(voiceFileData.getContent().contentLength())
                .contentType(MediaType.parseMediaType(voiceFileData.getContentType()))
                .body(voiceFileData.getContent());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/applications/{id}/voice/converted")
    @Timed
    public ResponseEntity<Resource> downloadConvertedVoice(@PathVariable String id) {
        log.debug("REST request to get converted voice for Application : {}", id);
        final VoiceFileData voiceFileData;
        try {
            voiceFileData = applicationService.fileConvertedVoice(id);
            return ResponseEntity.ok()
                .contentLength(voiceFileData.getContent().contentLength())
                .contentType(MediaType.parseMediaType(voiceFileData.getContentType()))
                .body(voiceFileData.getContent());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

    }
}
