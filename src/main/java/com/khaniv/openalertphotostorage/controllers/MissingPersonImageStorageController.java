package com.khaniv.openalertphotostorage.controllers;

import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.khaniv.openalertphotostorage.dto.MissingPersonImageData;
import com.khaniv.openalertphotostorage.services.MissingPersonImageStorageService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("v1/storage")
@RequiredArgsConstructor
@Log4j2
public class MissingPersonImageStorageController {
    private final MissingPersonImageStorageService missingPersonImageStorageService;

    @GetMapping
    public ResponseEntity<byte[]> findLostPersonImage(@RequestBody @NonNull MissingPersonImageData missingPersonImageData)
            throws IOException {
        log.info("Find missing person image. " + requestInfo(missingPersonImageData));
        byte[] content = missingPersonImageStorageService.findMissingPersonImage(missingPersonImageData);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(missingPersonImageData.getContentType());
        return new ResponseEntity<>(content, headers, HttpStatus.CREATED);
    }

    @PostMapping
    @ResponseBody
    public PutObjectResult storeLostPersonImage(@RequestParam("image") @NonNull MultipartFile image,
                                                @RequestParam("missingPerson") @NotNull MissingPersonImageData missingPersonImageData) {
        log.info("Store lost person image. " + requestInfo(missingPersonImageData));
        return missingPersonImageStorageService.uploadImage(missingPersonImageData, image);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public DeleteObjectsResult deleteAllByMissingPersonId(@PathVariable(value = "id") @NotNull UUID id) {
        log.info("Delete all images for person ID: " + id);
        return missingPersonImageStorageService.deleteAllByMissingPersonId(id);
    }

    @DeleteMapping
    @ResponseBody
    public DeleteObjectsResult delete(@RequestBody @NotNull MissingPersonImageData missingPersonImageData) {
        log.info("Delete lost person image. " + requestInfo(missingPersonImageData));
        return missingPersonImageStorageService.delete(missingPersonImageData);
    }

    private String requestInfo(MissingPersonImageData missingPersonImageData) {
        return "ID: " + missingPersonImageData.getImageId() + ", personID: " + missingPersonImageData.getPersonId();
    }
}
