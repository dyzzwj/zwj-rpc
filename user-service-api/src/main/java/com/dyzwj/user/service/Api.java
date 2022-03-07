package com.dyzwj.user.service;

import com.dyzwj.user.service.bean.InfoUser;

import java.util.List;
import java.util.Map;

public interface Api {

    List<InfoUser> insertInfoUser(InfoUser infoUser);
    InfoUser getInfoUserById(String id);
    void deleteInfoUserById(String id);
    String getNameById(String id);
    Map<String,InfoUser> getAllUser();
}
