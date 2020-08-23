package com.khaniv.openalertphotostorage.controllers;

import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertphotostorage.services.MissingPersonImageStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("v1/storage")
@RequiredArgsConstructor
@Log4j2
public class MissingPersonImageStorageController {
    private final MissingPersonImageStorageService missingPersonImageStorageService;

    @PostMapping("/find")
    public MissingPersonImageDto findLostPersonImage(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto)
            throws IOException {
        log.info("Find missing person image. " + requestInfo(missingPersonImageDto));
        return missingPersonImageStorageService.findMissingPersonImage(missingPersonImageDto);
    }

    @PostMapping("/store")
    public void storeLostPersonImage(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto) {
        log.info("Store lost person image. " + requestInfo(missingPersonImageDto));
        missingPersonImageStorageService.uploadImage(missingPersonImageDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAllByMissingPersonId(@PathVariable(value = "id") @NonNull UUID id) {
        log.info("Delete all images for person ID: " + id);
        missingPersonImageStorageService.deleteAllByMissingPersonId(id);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto) {
        log.info("Delete lost person image. " + requestInfo(missingPersonImageDto));
        missingPersonImageStorageService.delete(missingPersonImageDto);
    }

    private String requestInfo(MissingPersonImageDto missingPersonImageData) {
        return "ID: " + missingPersonImageData.getId() + ", personID: " + missingPersonImageData.getPersonId();
    }
}
