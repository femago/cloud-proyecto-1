package co.edu.uniandes.cloud.service.media;

import co.edu.uniandes.cloud.domain.Application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface VoicesMediaRepository {

    String storeOriginalRecordTbp(byte[] originalRecord, String nameSuffix);

    Path retrieveOriginalRecordTbp(Application tbpApp);

    void archiveOriginalRecord(Application processedApp) throws IOException;

    void archiveConvertedRecord(File converted);
}
