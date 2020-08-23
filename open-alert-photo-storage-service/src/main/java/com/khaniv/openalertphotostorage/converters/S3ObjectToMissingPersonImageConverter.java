package com.khaniv.openalertphotostorage.converters;

import com.amazonaws.services.s3.model.S3Object;
import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertimagesmanager.dto.enums.ImageType;
import com.khaniv.openalertphotostorage.utils.S3ObjectUtils;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class S3ObjectToMissingPersonImageConverter implements Converter<S3Object, MissingPersonImageDto> {

    @Override
    @SneakyThrows
    public MissingPersonImageDto convert(S3Object s3Object) {
        String[] keys = S3ObjectUtils.parseS3ObjectKey(s3Object);
        return MissingPersonImageDto.builder()
                .id(Long.parseLong(keys[1]))
                .personId(UUID.fromString(keys[0]))
                .type(ImageType.valueOf(keys[2].toUpperCase()))
                .image(S3ObjectUtils.findS3ObjectContent(s3Object))
                .build();
    }
}
