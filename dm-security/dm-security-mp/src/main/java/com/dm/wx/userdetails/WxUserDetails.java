package com.dm.wx.userdetails;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.core.userdetails.UserDetails;

public interface WxUserDetails extends UserDetails {
    WxMpUser getWxMpUser();

    String getOpenId();

    String getUnionId();
}
