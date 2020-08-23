package com.khaniv.openalertphotostorage.converters;

import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertphotostorage.utils.S3ObjectUtils;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

@Component
public class MissingPersonImageToFileConverter implements Converter<MissingPersonImageDto, File> {
    @Override
    @SneakyThrows
    public File convert(MissingPersonImageDto image) {
        File file = new File(Objects.requireNonNull(S3ObjectUtils.getImageName(image)));
        byte[] bytes = Objects.requireNonNull(image.getImage());
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(bytes);
        stream.close();
        return file;
    }
}
