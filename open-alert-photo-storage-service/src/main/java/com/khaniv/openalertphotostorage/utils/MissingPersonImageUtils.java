package com.khaniv.openalertphotostorage.utils;

import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MissingPersonImageUtils {
    public final String SLASH = "/";
    public final String DOT = ".";

    public String getShortName(MissingPersonImageDto missingPersonImageDto) {
        return missingPersonImageDto.getId() + DOT + missingPersonImageDto.getType().name();
    }

    public String getFullName(MissingPersonImageDto missingPersonImageDto) {
        return missingPersonImageDto.getPersonId() + SLASH + getShortName(missingPersonImageDto);
    }
}
