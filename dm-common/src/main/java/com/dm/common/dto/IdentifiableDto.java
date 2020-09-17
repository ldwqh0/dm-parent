package com.dm.common.dto;

import java.io.Serializable;

/**
 * 标记一个DTO是可被唯一标识的
 * 
 * @param <ID> 主键的类型
 * @author ldwqh0@outlook.com
 * @since 0.2.3
 *
 */
public interface IdentifiableDto<ID extends Serializable> extends Serializable {

    ID getId();

}
