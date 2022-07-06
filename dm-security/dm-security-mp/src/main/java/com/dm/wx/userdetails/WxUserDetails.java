package com.dm.wx.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

public interface WxUserDetails extends UserDetails {
//    WxMpUser getWxMpUser();

    String getOpenid();

    String getUnionid();
}
