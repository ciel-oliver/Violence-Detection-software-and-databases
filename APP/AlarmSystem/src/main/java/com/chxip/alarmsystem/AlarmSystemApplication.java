package com.chxip.alarmsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching//开启缓存
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.chxip.alarmsystem.dao")
public class AlarmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlarmSystemApplication.class, args);
    }

}
