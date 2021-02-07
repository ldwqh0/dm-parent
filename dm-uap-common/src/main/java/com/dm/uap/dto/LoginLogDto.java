package com.dm.uap.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
public class LoginLogDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String loginName;

    private String ip;

    private String type;

    private String result;

    private ZonedDateTime time;

}
