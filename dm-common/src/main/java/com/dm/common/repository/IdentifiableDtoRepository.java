package com.dm.common.repository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.common.dto.IdentifiableDto;

public interface IdentifiableDtoRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    public default List<T> getById(List<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        } else {
            return ids.stream().map(this::getOne).collect(Collectors.toList());
        }
    }

    public default Stream<T> getById(Stream<ID> ids) {
        return ids.map(this::getOne);
    }

    public default T getByDto(IdentifiableDto<ID> element) {
        if (Objects.isNull(element)) {
            return null;
        } else {
            return getOne(element.getId());
        }
    }

    public default List<T> getByDto(List<? extends IdentifiableDto<ID>> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        } else {
            return elements.stream().map(IdentifiableDto::getId).map(this::getOne).collect(Collectors.toList());
        }
    }

    public default Set<T> getByDto(Set<? extends IdentifiableDto<ID>> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptySet();
        } else {
            return elements.stream().map(IdentifiableDto::getId).map(this::getOne).collect(Collectors.toSet());
        }
    }

}
