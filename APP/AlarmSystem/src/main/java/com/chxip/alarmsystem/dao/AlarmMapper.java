package com.chxip.alarmsystem.dao;

import com.chxip.alarmsystem.entity.Alarm;
import com.chxip.alarmsystem.entity.Notification;

import java.util.List;
import java.util.Map;

public interface AlarmMapper {
    int deleteByPrimaryKey(Integer alarmId);

    int insert(Alarm record);

    int insertSelective(Alarm record);

    Alarm selectByPrimaryKey(Integer alarmId);

    int updateByPrimaryKeySelective(Alarm record);

    int updateByPrimaryKeyWithBLOBs(Alarm record);

    int updateByPrimaryKey(Alarm record);


    List<Alarm> selectAll(Map<String,Object> map);

    Integer selectAllCount(Map<String,Object> map);

    List<Alarm> selectByStatus(int status);

    List<Alarm> selectByUserId(Map<String,Object> map);

    int updateAlarmUserId(Integer userId,Integer alarmId);

    int updateStateById(Integer state,Integer id,String alarmRemark);
}