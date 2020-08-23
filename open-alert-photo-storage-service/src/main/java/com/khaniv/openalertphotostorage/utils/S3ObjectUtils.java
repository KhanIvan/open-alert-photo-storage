package com.khaniv.openalertphotostorage.utils;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertphotostorage.errors.IncorrectAWS3ObjectNameException;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class S3ObjectUtils {

    public byte[] findS3ObjectContent(S3Object s3Object) throws IOException {
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return IOUtils.toByteArray(inputStream);
    }

    public String getImageName(MissingPersonImageDto missingPersonImageDto) {
        return missingPersonImageDto.getId() + "." + missingPersonImageDto.getType();
    }

    public String[] parseS3ObjectKey(S3Object s3Object) {
        String[] keys = s3Object.getKey().split(MissingPersonImageUtils.SLASH);

        if (keys.length != 2)
            throw new IncorrectAWS3ObjectNameException(s3Object);

        String[] ret = new String[3];
        int dotIndex = keys[1].lastIndexOf(MissingPersonImageUtils.DOT);
        if (dotIndex < 0 || dotIndex >= keys[1].length() - 1)
            throw new IncorrectAWS3ObjectNameException(s3Object);

        ret[0] = keys[0];
        ret[1] = keys[1].substring(0, dotIndex);
        ret[2] = keys[1].substring(dotIndex + 1);
        return ret;
    }
}
