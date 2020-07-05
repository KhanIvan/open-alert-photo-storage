package com.khaniv.openalertphotostorage.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

@Component
public class MultipartFileToFileConverter implements Converter<MultipartFile, File> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public File convert(MultipartFile multipartFile) {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        byte[] bytes = multipartFile.getBytes();
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(bytes);
        stream.close();
        return file;
    }
}
