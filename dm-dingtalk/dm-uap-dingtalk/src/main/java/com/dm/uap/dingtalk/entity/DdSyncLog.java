package com.dm.uap.dingtalk.entity;

import java.time.ZonedDateTime;

import com.dm.common.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 同步日志
 * 
 * @author ldwqh0@outlook.com
 */
@Getter
@Setter
public class DdSyncLog extends AbstractEntity {

    private String user;

    private ZonedDateTime start;

    private ZonedDateTime end;

    private String result;

}
