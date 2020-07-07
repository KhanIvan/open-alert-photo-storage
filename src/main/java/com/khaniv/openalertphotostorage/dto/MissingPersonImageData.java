package com.khaniv.openalertphotostorage.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.http.MediaType;

import java.util.UUID;

@Data
public class MissingPersonImageData {
    public final static String SLASH = "/";
    public final static String POINT = ".";

    @NonNull
    private long imageId;
    @NonNull
    private UUID personId;
    @NonNull
    private MissingPersonImageType type;

    @Override
    public String toString() {
        return personId + SLASH + imageId + POINT + type.name();
    }

    public String toShortString() {
        return imageId + POINT + type.name();
    }

    public MediaType getContentType() {
        switch (type) {
            case GIF:
                return MediaType.IMAGE_GIF;
            case JPG:
                return MediaType.IMAGE_JPEG;
            case PNG:
                return MediaType.IMAGE_PNG;
            default:
                throw new IllegalArgumentException("Incorrect image type!");
        }
    }
}
