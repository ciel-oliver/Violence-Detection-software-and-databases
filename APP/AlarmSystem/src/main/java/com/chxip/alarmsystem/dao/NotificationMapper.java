package com.chxip.alarmsystem.dao;

import com.chxip.alarmsystem.entity.Notification;
import com.chxip.alarmsystem.entity.User;

import java.util.List;
import java.util.Map;

public interface NotificationMapper {
    int deleteByPrimaryKey(Integer notificationId);

    int insert(Notification record);

    int insertSelective(Notification record);

    Notification selectByPrimaryKey(Integer notificationId);

    int updateByPrimaryKeySelective(Notification record);

    int updateByPrimaryKeyWithBLOBs(Notification record);

    int updateByPrimaryKey(Notification record);


    List<Notification> selectAll(Map<String,Object> map);

    Integer selectAllCount(Map<String,Object> map);

    int getNewNotification(String date);

}