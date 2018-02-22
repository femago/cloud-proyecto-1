package co.edu.uniandes.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Configuration
public class CloiceConfiguration {

    private final Logger log = LoggerFactory.getLogger(CloiceConfiguration.class);

    private final ApplicationProperties applicationProperties;

    public CloiceConfiguration(ApplicationProperties applicationProperties) throws IOException {
        this.applicationProperties = applicationProperties;
        initFolders();
    }

    private void initFolders() throws IOException {
        final String msg = "Resource folder checked -";
        materializeFolder(msg + " application.folder.voices-tbp={}", applicationProperties.getFolder().getVoicesTbp());
        materializeFolder(msg + " application.folder.voices-archive={}", applicationProperties.getFolder().getVoicesArchive());
    }

    private void materializeFolder(String concept, Path resourceFolder) throws IOException {
        File asFile = resourceFolder.toFile();
        if (!asFile.exists())
            asFile.mkdirs();
        log.info(concept, asFile.getCanonicalPath());
    }
}
