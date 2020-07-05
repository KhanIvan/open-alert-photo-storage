package com.khaniv.openalertphotostorage.dto;

import lombok.Data;
import lombok.NonNull;

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
}
