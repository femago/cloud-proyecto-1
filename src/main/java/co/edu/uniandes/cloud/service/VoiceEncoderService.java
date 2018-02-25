package co.edu.uniandes.cloud.service;

import co.edu.uniandes.cloud.config.ApplicationProperties;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.ApplicationRepository;
import co.edu.uniandes.cloud.service.impl.ApplicationServiceImpl;
import com.google.common.io.Files;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class VoiceEncoderService {

    public static final String CONVERTED_VOICE_MARKER = "_cnvrt_";

    private final Logger log = LoggerFactory.getLogger(VoiceEncoderService.class);

    private final ApplicationRepository applicationRepository;
    private final ApplicationProperties applicationProperties;

    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public VoiceEncoderService(ApplicationRepository applicationRepository, ApplicationProperties applicationProperties) throws IOException {
        this.applicationRepository = applicationRepository;
        this.applicationProperties = applicationProperties;
        this.ffmpeg = new FFmpeg("C:/dev/ffmpeg-3.4.2/bin/ffmpeg.exe");
        this.ffprobe = new FFprobe("C:/dev/ffmpeg-3.4.2/bin/ffprobe.exe");
    }

    public void processAppOriginalRecord(Application app) {
        final Path path = Paths.get(applicationProperties.getFolder().getVoicesTbp().toString(), app.getOriginalRecordLocation());

        try {
            final File originalRecord = path.toFile().getCanonicalFile();
            if (originalRecord.exists()) {
                log.info("Voice to process {}", originalRecord.getPath());
                final File convertedRecord = encodeVoice(originalRecord);
                markApplicationConverted(app, convertedRecord.getName());
                archiveOriginalFile(originalRecord);
                log.info("Voice converted for application {}", app.getId());
            } else {
                resetInconsistentApplication(app);
                log.error("Pending application voice {} couldn't be processed, file doesn't exist {}", app.getId(), originalRecord.getPath());
            }
        } catch (IOException e) {
            log.error("Unexpected error", e);
            throw new UncheckedIOException(e);
        }

    }

    private void archiveOriginalFile(File app) throws IOException {
        Files.move(app, Paths.get(applicationProperties.getFolder().getVoicesArchive().toString(), app.getName().toString()).toFile());
    }

    private void resetInconsistentApplication(Application app) {
        app.setOriginalRecordLocation(null);
        applicationRepository.save(app);
    }

    private void markApplicationConverted(Application app, String convertedFileName) {
        app.setConvertedRecordLocation(convertedFileName);
        app.setStatus(ApplicationState.CONVERTED);
        applicationRepository.save(app);
    }

    private File encodeVoice(File file) {
        final String nameWithoutExtension = Files.getNameWithoutExtension(file.getPath());
        final Path convertedPath = Paths.get(applicationProperties.getFolder().getVoicesArchive().toString(), nameWithoutExtension + ".mp3");

        final String outputFilename = convertedPath.toString().replace(ApplicationServiceImpl.ORIGINAL_VOICE_MARKER, CONVERTED_VOICE_MARKER);

        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(file.getPath())
            .overrideOutputFiles(true)

            .addOutput(outputFilename)
            .setFormat("mp3")
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();
        return convertedPath.toFile();
    }
}
