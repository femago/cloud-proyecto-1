package co.edu.uniandes.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Properties specific to Cloice.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    public static final String CLOICE_PROFILE_WORKER = "worker";

    private final Folder folder = new Folder();

    public Folder getFolder() {
        return folder;
    }

    public static class Folder {
        private final Logger log = LoggerFactory.getLogger(Folder.class);

        private Path voicesTbp;
        private Path voicesArchive;

        public Path getVoicesTbp() {
            return voicesTbp;
        }

        public void setVoicesTbp(String voicesTbp) throws IOException {
            this.voicesTbp = Paths.get(voicesTbp);
        }

        public Path getVoicesArchive() {
            return voicesArchive;
        }

        public void setVoicesArchive(String voicesArchive) throws IOException {
            this.voicesArchive = Paths.get(voicesArchive);
        }
    }
}
