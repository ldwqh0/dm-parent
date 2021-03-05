package com.dm.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.io.Serializable;

/**
 * 表示移动操作的移动方式
 *
 * @author LiDong
 */
@Data
@JsonInclude(Include.NON_ABSENT)
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -6845318546617903755L;

    /**
     * 移动的方向
     *
     * @author LiDong
     */
    public enum Position {
        /**
         * 向上移动
         */
        UP,
        /**
         * 向下移动
         */
        DOWN
    }

    /**
     * 移动的方向
     */
    private Position position;
}
