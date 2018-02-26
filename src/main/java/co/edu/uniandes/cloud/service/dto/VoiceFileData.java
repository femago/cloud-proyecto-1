package co.edu.uniandes.cloud.service.dto;

import org.springframework.core.io.Resource;

public class VoiceFileData {
    private final String contentType;

    private final Resource content;

    public VoiceFileData(String contentType, Resource content) {
        this.contentType = contentType;
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public Resource getContent() {
        return content;
    }
}
