package com.dm.common.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dm.collections.CollectionUtils;
import com.dm.common.dto.IdentifiableDto;

public interface IdentifiableDtoRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    default List<T> getById(Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        } else {
            return this.getById(ids.stream()).collect(Collectors.toList());
        }
    }

    default Stream<T> getById(Stream<ID> ids) {
        if (ids == null) {
            return null;
        } else {
            return ids.map(this::getOne);
        }
    }

    default boolean existsAll(@NotNull Collection<? extends IdentifiableDto<ID>> elements) {
        return elements.stream().map(IdentifiableDto::getId).allMatch(this::existsById);
    }

    default boolean existsAllById(@NotNull Collection<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            // TODO默认返回值待商榷
            return true;
        } else {
            return ids.stream().allMatch(this::existsById);
        }
    }

    default T getByDto(IdentifiableDto<ID> element) {
        if (Objects.isNull(element)) {
            return null;
        } else {
            return getOne(element.getId());
        }
    }

    default List<T> getByDto(Collection<? extends IdentifiableDto<ID>> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return Collections.emptyList();
        } else {
            return elements.stream().map(IdentifiableDto::getId).map(this::getOne).collect(Collectors.toList());
        }
    }

}
