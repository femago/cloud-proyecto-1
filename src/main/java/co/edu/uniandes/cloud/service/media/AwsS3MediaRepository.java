package co.edu.uniandes.cloud.service.media;

import co.edu.uniandes.cloud.config.ApplicationProperties;
import co.edu.uniandes.cloud.domain.Application;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static co.edu.uniandes.cloud.config.Constants.CLOICE_PROFILE_S3;

@Service
@Profile(CLOICE_PROFILE_S3)
public class AwsS3MediaRepository implements VoicesMediaRepository {

    public static final String S3_BUCKET_NAME = "uniandes-cloud-cloice";
    public static final String S3_FILE_PREFIX = "S3_";
    public static final String TBP_PREFIX = "voices/tbp/";
    public static final String ARCHIVE_PREFIX = "voices/archive/";

    private final Logger log = LoggerFactory.getLogger(AwsS3MediaRepository.class);
    private final ApplicationProperties applicationProperties;
    private final AmazonS3 s3;

    public AwsS3MediaRepository(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials("access_key_id", "secret_key_id");
//        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//            .build();
        s3 = AmazonS3ClientBuilder.defaultClient();
        log.info("AWS S3 Media Repository Loaded");
    }

    @Override
    public String storeOriginalRecordTbp(byte[] originalRecord, String nameSuffix) {
        final String recordName = S3_FILE_PREFIX + UUID.randomUUID().toString() + nameSuffix;
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(originalRecord.length);
        s3.putObject(S3_BUCKET_NAME, TBP_PREFIX + recordName, new ByteArrayInputStream(originalRecord), metadata);
        log.info("Object Pushed to S3 {}", TBP_PREFIX + recordName);
        return recordName;
    }

    @Override
    public Path retrieveOriginalRecordTbp(Application tbpApp) {
        final Path path = Paths.get(applicationProperties.getFolder().getVoicesTbp().toString(),
            tbpApp.getOriginalRecordLocation());
        S3Object o = s3.getObject(S3_BUCKET_NAME, TBP_PREFIX + tbpApp.getOriginalRecordLocation());
        try (final S3ObjectInputStream s3is = o.getObjectContent();) {
            java.nio.file.Files.copy(s3is, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        log.info("Object Pulled from S3 {}", o.getKey());
        return path;
    }

    @Override
    public void archiveOriginalRecord(Application processedApp) {
        final String key = TBP_PREFIX + processedApp.getOriginalRecordLocation();
        s3.copyObject(S3_BUCKET_NAME, key, S3_BUCKET_NAME,
            ARCHIVE_PREFIX + processedApp.getOriginalRecordLocation());
        s3.deleteObject(S3_BUCKET_NAME, key);
        log.info("Moved S3 object to archive {}", key);
    }

    @Override
    public void archiveConvertedRecord(File converted) {
        final String key = ARCHIVE_PREFIX + converted.getName();
        s3.putObject(S3_BUCKET_NAME, key, converted);
        log.info("Object Pushed to S3 {}", key);
    }
}
