package com.chxip.alarmsystem.dao;

import com.chxip.alarmsystem.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByPhone(String phone);

    User selectByPhoneAndAccount(String phone,String account);

    User selectByPassword(Map<String,String> map);

    List<User> selectAll(Map<String,Object> map);

    Integer selectAllCount(Map<String,Object> map);

    Integer updateStateById(Map<String,Object> map);
}