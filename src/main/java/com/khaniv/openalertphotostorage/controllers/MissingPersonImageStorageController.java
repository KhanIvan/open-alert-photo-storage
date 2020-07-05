package com.khaniv.openalertphotostorage.controllers;

import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.khaniv.openalertphotostorage.dto.MissingPersonImageData;
import com.khaniv.openalertphotostorage.services.MissingPersonImageStorageService;
import com.sun.istack.internal.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("v1/storage")
@RequiredArgsConstructor
@Log4j2
public class MissingPersonImageStorageController {
    private final MissingPersonImageStorageService missingPersonImageStorageService;

    @PostMapping
    @ResponseBody
    public PutObjectResult storeLostPersonImage(@RequestParam("image") @NonNull MultipartFile image,
                                                @RequestParam("missingPerson") @NotNull MissingPersonImageData missingPersonImageData) {
        log.info("Store lost person image. ID: " + missingPersonImageData.getImageId() + ", person ID: " +
                missingPersonImageData.getPersonId());
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
        log.info("Delete lost person image. ID: " + missingPersonImageData.getImageId() + ", person ID: " +
                missingPersonImageData.getPersonId());
        return missingPersonImageStorageService.delete(missingPersonImageData);
    }
}
