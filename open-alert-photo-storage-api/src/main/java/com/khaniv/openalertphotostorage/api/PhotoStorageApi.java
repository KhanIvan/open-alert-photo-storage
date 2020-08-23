package com.khaniv.openalertphotostorage.api;

import com.khaniv.openalertimagesmanager.dto.MissingPersonImageDto;
import com.khaniv.openalertphotostorage.constants.PhotoStoragePaths;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient("open-alert-photo-storage")
@Service
@SuppressWarnings("unused")
public interface PhotoStorageApi {
    @PostMapping(PhotoStoragePaths.STORAGE_CONTROLLER + PhotoStoragePaths.FIND)
    MissingPersonImageDto findLostPersonImage(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto);

    @PostMapping(PhotoStoragePaths.STORAGE_CONTROLLER + PhotoStoragePaths.STORE)
    void storeLostPersonImage(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto);

    @DeleteMapping(PhotoStoragePaths.STORAGE_CONTROLLER + PhotoStoragePaths.ID)
    void deleteAllByMissingPersonId(@PathVariable(value = "id") @NonNull UUID id);

    @PostMapping(PhotoStoragePaths.STORAGE_CONTROLLER + PhotoStoragePaths.DELETE)
    void delete(@RequestBody @NonNull MissingPersonImageDto missingPersonImageDto);
}
