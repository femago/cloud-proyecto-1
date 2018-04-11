package co.edu.uniandes.cloud.service.impl;

import co.edu.uniandes.cloud.config.ApplicationProperties;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import co.edu.uniandes.cloud.service.ApplicationService;
import co.edu.uniandes.cloud.service.dto.VoiceFileData;
import co.edu.uniandes.cloud.service.media.VoicesMediaRepository;
import co.edu.uniandes.cloud.service.worker.ApplicationEventEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import static co.edu.uniandes.cloud.service.media.AwsS3MediaRepository.S3_FILE_PREFIX;


/**
 * Service Implementation for managing Application.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    public static final String ORIGINAL_VOICE_MARKER = "_orig_";
    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationProperties applicationProperties;
    private final VoicesMediaRepository mediaRepository;
    private final ApplicationEventEmitter eventEmitter;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  ApplicationProperties applicationProperties,
                                  VoicesMediaRepository mediaRepository,
                                  ApplicationEventEmitter eventEmitter) {
        this.applicationRepository = applicationRepository;
        this.applicationProperties = applicationProperties;
        this.mediaRepository = mediaRepository;
        this.eventEmitter = eventEmitter;
    }

    /**
     * Save a application.
     *
     * @param application the entity to save
     * @return the persisted entity
     */
    @Override
    public Application save(Application application) {
        application.setCreateDate(Instant.now());
        application.setStatus(ApplicationState.IN_PROCESS);

        final byte[] originalRecordCopy = application.getOriginalRecord();
        application.setOriginalRecord(new byte[]{1});

        log.debug("Request to save Application : {}", application);
        Application save = applicationRepository.save(application);

        storeVoiceTBP(application, originalRecordCopy);
        eventEmitter.onSaved(save);
        resolveMediaLocation(save);
        return save;
    }

    private void storeVoiceTBP(Application application, byte[] originalRecordCopy) {

        final String suffix = ORIGINAL_VOICE_MARKER + application.getId() + "." + extractExtFromContentType(application.getOriginalRecordContentType());
        final String recordId = mediaRepository.storeOriginalRecordTbp(originalRecordCopy, suffix);
        application.setOriginalRecordLocation(recordId);
        applicationRepository.save(application);
    }

    private String extractExtFromContentType(String contentType) {
        return contentType.split("/")[1];
    }

    /**
     * Get all the applications.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Application> findAll(Pageable pageable) {
        log.debug("Request to get all Applications");
        return resolveMediaLocation(applicationRepository.findAll(pageable));
    }

    /**
     * Get one application by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Application findOne(String id) {
        log.debug("Request to get Application : {}", id);
        return resolveMediaLocation(applicationRepository.findOne(id));
    }

    /**
     * Delete the application by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Application : {}", id);
        applicationRepository.delete(id);
    }

    /**
     * List converted applications of a contest
     *
     * @param pageable
     * @param contestId
     * @return
     */
    @Override
    public Page<Application> findConvertedByContest(Pageable pageable, String contestId) {
        log.debug("Request to get Converted Applications by Contest");
        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
        return resolveMediaLocation(applicationRepository.findByStatusAndContest(pageRequest, ApplicationState.CONVERTED, new Contest(contestId)));
    }

    /**
     * List all applications of a contest
     *
     * @param pageable
     * @param contestId
     * @return
     */
    @Override
    public Page<Application> findByContest(Pageable pageable, String contestId) {
        log.debug("Request to get Applications by Contest");
        return resolveMediaLocation(applicationRepository.findByContest(pageable, new Contest(contestId)));
    }

    @Override
    public VoiceFileData fileConvertedVoice(String id) throws IOException {
        final Application one = applicationRepository.findOne(id);
        final Path path = Paths.get(applicationProperties.getFolder().getVoicesArchive().toString(), one.getConvertedRecordLocation());
        final ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
        return new VoiceFileData("audio/mp3", byteArrayResource);
    }

    @Override
    public VoiceFileData fileOriginalVoice(String id) throws IOException {
        final Application one = applicationRepository.findOne(id);
        String actualLocation = "";
        if (one.getStatus().equals(ApplicationState.IN_PROCESS)) {
            actualLocation = applicationProperties.getFolder().getVoicesTbp().toString();
        } else {
            actualLocation = applicationProperties.getFolder().getVoicesArchive().toString();
        }

        final Path path = Paths.get(actualLocation, one.getOriginalRecordLocation());
        final ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));
        return new VoiceFileData(one.getOriginalRecordContentType(), byteArrayResource);
    }

    private Page<Application> resolveMediaLocation(Page<Application> applications) {
        applications.forEach(this::resolveMediaLocation);
        return applications;
    }

    private Application resolveMediaLocation(Application application) {
        final boolean isS3 = nullLocation(application) ? false : application.getOriginalRecordLocation().startsWith(S3_FILE_PREFIX);
        if (isS3) {
            if (ApplicationState.IN_PROCESS.equals(application.getStatus())) {
                application.setOriginalRecordURL("https://s3.amazonaws.com/uniandes-cloud-cloice/voices/tbp/" + application.getOriginalRecordLocation());
            } else {
                application.setOriginalRecordURL("https://s3.amazonaws.com/uniandes-cloud-cloice/voices/archive/" + application.getOriginalRecordLocation());
            }
            application.setConvertedRecordURL("https://s3.amazonaws.com/uniandes-cloud-cloice/voices/archive/" + application.getConvertedRecordLocation());
        } else {
            application.setOriginalRecordURL(application.getId() + "/voice/original");
            application.setConvertedRecordURL(application.getId() + "/voice/converted");
        }
        return application;
    }

    private boolean nullLocation(Application application) {
        return application.getOriginalRecordLocation() == null;
    }

}
