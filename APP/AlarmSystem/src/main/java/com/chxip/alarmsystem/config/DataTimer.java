package com.chxip.alarmsystem.config;

import com.chxip.alarmsystem.dao.AlarmMapper;
import com.chxip.alarmsystem.utils.DataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 报警信息读取定时器
 */
@Component
@EnableScheduling
public class DataTimer {


    @Autowired
    private AlarmMapper alarmMapper;

    //@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(fixedDelay = 1000)
    public void updateOnlineStatus(){
        System.out.println("查询出所有未分配 处理人员的报警信息");
        //查询出所有未分配 处理人员的报警信息
        DataCache.alarmListCache = alarmMapper.selectByStatus(1);
    }
}
