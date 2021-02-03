package com.dm.file.service.impl;

import com.dm.file.dto.PackageFileDto;
import com.dm.file.service.PackageFileService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapPackageFileServiceImpl implements PackageFileService {

    private final Map<String, PackageFileDto> requests = new ConcurrentHashMap<>();


    @Override
    @Transactional
    public PackageFileDto save(PackageFileDto request) {
        String id = UUID.randomUUID().toString();
        request.setId(id);
        requests.put(id, request);
        return request;
    }

    @Override
    public Optional<PackageFileDto> findById(String id) {
        return Optional.ofNullable(requests.get(id));
    }

    @Override
    public Optional<PackageFileDto> findAndRemoveById(String id) {
        PackageFileDto result = requests.remove(id);
        return Optional.ofNullable(result);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        requests.remove(id);
    }
}
