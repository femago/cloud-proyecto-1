package co.edu.uniandes.cloud.service.media;

import co.edu.uniandes.cloud.CloiceApp;
import co.edu.uniandes.cloud.domain.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static co.edu.uniandes.cloud.config.ApplicationProperties.CLOICE_PROFILE_S3;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloiceApp.class)
@ActiveProfiles(CLOICE_PROFILE_S3)
public class AwsS3MediaRepositoryTest {

    @Autowired
    private AwsS3MediaRepository s3;

    @Test
    public void retrieveOriginalRecordTbp() {
        Assert.assertNotNull(s3);

        s3.retrieveOriginalRecordTbp(new Application().originalRecordLocation("voice_1100817470534726259_orig_481.mp3"));
    }

    @Test
    public void dummy() throws IOException {
        InputStream initialStream = new FileInputStream(
            new File("./shared/cloice/voices/tbp/voice_1100817470534726259_orig_481.mp3"));
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);
        s3.storeOriginalRecordTbp(buffer, "_orig_789.mp3");
    }
}
