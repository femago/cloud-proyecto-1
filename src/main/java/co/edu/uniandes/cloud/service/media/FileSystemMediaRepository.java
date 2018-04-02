package co.edu.uniandes.cloud.service.media;

import co.edu.uniandes.cloud.config.ApplicationProperties;
import co.edu.uniandes.cloud.domain.Application;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static co.edu.uniandes.cloud.config.ApplicationProperties.CLOICE_PROFILE_NO_S3;

@Service
@Profile(CLOICE_PROFILE_NO_S3)
public class FileSystemMediaRepository implements VoicesMediaRepository {

    private final Logger log = LoggerFactory.getLogger(FileSystemMediaRepository.class);
    private final ApplicationProperties applicationProperties;

    public FileSystemMediaRepository(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        log.info("File System Media Repository Loaded");
    }

    @Override
    public Path retrieveOriginalRecordTbp(Application tbpApp) {
        return Paths.get(applicationProperties.getFolder().getVoicesTbp().toString(), tbpApp.getOriginalRecordLocation());
    }

    @Override
    public void archiveOriginalRecord(Application processedApp) throws IOException {
        final File file = this.retrieveOriginalRecordTbp(processedApp)
            .toFile().getCanonicalFile();
        Files.move(file, Paths.get(applicationProperties.getFolder().getVoicesArchive().toString(),
            file.getName().toString()).toFile());
        log.info("Original record Archived {}", processedApp.getId());
    }

    @Override
    public void archiveConvertedRecord(Application processedApp, File converted) {
        //Empty for file system, files already saved in expected folder
        log.info("Converted record Archived {}", processedApp.getId());

    }

}
