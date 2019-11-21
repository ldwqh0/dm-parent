package com.dm.common.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.dm.common.dto.IdentifiableDto;

public interface IdentifiableDtoRepository<T, ID extends Serializable> {

    public default List<T> getById(List<ID> uuids) {
        if (CollectionUtils.isEmpty(uuids)) {
            return Collections.emptyList();
        } else {
            return uuids.stream().map(this::getOne).collect(Collectors.toList());
        }
    }

    public default Stream<T> getById(Stream<ID> uuids) {
        return uuids.map(this::getOne);
    }

    public default T getByDto(IdentifiableDto<ID> file) {
        if (Objects.isNull(file)) {
            return null;
        } else {
            return getOne(file.getId());
        }
    }

    public default T getByDto(Optional<? extends IdentifiableDto<ID>> file) {
        return file.map(IdentifiableDto::getId).map(this::getOne).orElse(null);
    }

    public default List<T> getByDto(List<? extends IdentifiableDto<ID>> files) {
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        } else {
            return files.stream().map(IdentifiableDto::getId).map(this::getOne).collect(Collectors.toList());
        }
    }

    public T getOne(ID id);
}
