package com.dm.common.converter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.IterableUtils;

import com.dm.common.exception.DataNotExistException;

public abstract class AbstractConverter<M, DTO> {
	/**
	 * 将实体模型转换为DTO
	 * 
	 * @param model
	 * @return
	 */
	public final DTO toDto(M model) {
		if (Objects.isNull(model)) {
			throw new DataNotExistException();
		} else {
			return toDtoActual(model);
		}
	}

	public final DTO toDto(Optional<M> model) {
		if (model.isPresent()) {
			return toDtoActual(model.get());
		} else {
			throw new DataNotExistException();
		}
	}

	/**
	 * 将实体模型转换为DTO的实际动作，模型参数不会为空<br />
	 * 所有的converter应该实现该方法以完成转换
	 * 
	 * @param model 待转换的模型，当子类重写该方法时，model不会为空
	 * 
	 * @return
	 */
	protected abstract DTO toDtoActual(M model);

	/**
	 * 将实体模型列表转换为DTO列表
	 * 
	 * @param models
	 * @return
	 */
	public List<DTO> toDto(Iterable<M> models) {
		if (IterableUtils.isEmpty(models)) {
			return Collections.emptyList();
		} else {
			return StreamSupport.stream(models.spliterator(), false).map(this::toDto).collect(Collectors.toList());
		}
	}

	/**
	 * 将DTO中的数据复制到实体模型<br />
	 * 
	 * 所有的Converter必须实现该方法<br />
	 * 注意在复制数据的时候，如果存在关联关系，复制不应该在converter进行<br />
	 * converter不应该实现任何关联Respository的操做，它仅仅只作为一个复制数据的工具出现的
	 * 
	 * @param model
	 * @param dto
	 */
	public abstract void copyProperties(M model, DTO dto);

}
