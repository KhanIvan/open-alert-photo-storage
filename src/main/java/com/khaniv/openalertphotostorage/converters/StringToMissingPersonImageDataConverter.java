package com.khaniv.openalertphotostorage.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khaniv.openalertphotostorage.dto.MissingPersonImageData;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMissingPersonImageDataConverter implements Converter<String, MissingPersonImageData> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public MissingPersonImageData convert(String source) {
        return objectMapper.readValue(source, MissingPersonImageData.class);
    }
}
