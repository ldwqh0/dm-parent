package com.dm.auth.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 表示移动操作的移动方式
 * 
 * @author LiDong
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -6845318546617903755L;

    /**
     * 移动的顺序
     * 
     * @author LiDong
     *
     */
    public enum Position {
        UP, DOWN
    }

    private Position position;
}
