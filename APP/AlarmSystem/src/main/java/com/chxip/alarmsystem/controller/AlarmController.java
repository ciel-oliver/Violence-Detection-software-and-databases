package com.chxip.alarmsystem.controller;
 
import com.chxip.alarmsystem.dao.AlarmMapper;
import com.chxip.alarmsystem.entity.Alarm;
import com.chxip.alarmsystem.entity.Response;
import com.chxip.alarmsystem.utils.DataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alarm")
public class AlarmController {

    final AlarmMapper alarmMapper;

    @Autowired
    public AlarmController(AlarmMapper alarmMapper) {
        this.alarmMapper = alarmMapper;
    }




    @GetMapping(value = "/toAlarmList")
    public String toAlarmList(Model model){
        return "alarmlist";
    }

    @GetMapping(value = "/toAddAlarm")
    public String toAddAlarm(Model model){
        model.addAttribute("alarm",new Alarm());
        return "addAlarm";
    }

    @GetMapping(value = "/toUpdateAlarm")
    public String toUpdateAlarm(Integer alarmId, Model model){
        Alarm alarm = alarmMapper.selectByPrimaryKey(alarmId);
        model.addAttribute("alarm",alarm);
        return "addAlarm";
    }

    @GetMapping(value = "/toShowImageAlarm")
    public String toShowImageAlarm(Integer alarmId, Model model){
        Alarm alarm = alarmMapper.selectByPrimaryKey(alarmId);
        model.addAttribute("imagedata",alarm.getAlarmImage());
        return "showimage";
    }


    /**
     * 获取报警信息
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getAlarm")
    public Map<String, Object> getAlarm(Integer page, @RequestParam(defaultValue = "20") int size,
                                                 @RequestParam(defaultValue = "") String keyWord ) {
        int page1 = (page - 1) * size;
        Map<String, Object> map = new HashMap<>();
        map.put("page", page1);
        map.put("size", size);
        map.put("keyWord", keyWord);
        //返回的信息
        List<Alarm> alarmList = alarmMapper.selectAll(map);
        int countNumber = alarmMapper.selectAllCount(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("data", alarmList);
        resultMap.put("count", countNumber);

        return resultMap;
    }

    /**
     * APP获取报警信息
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/api/getAlarm")
    public Response getAlarmApi(Integer alarmType) {
        Response response = new Response();
        //返回的信息
        List<Alarm> alarmList = new ArrayList<>();
        for (Alarm alarm : DataCache.alarmListCache) {
            if(alarm.getAlarmType() == alarmType ){
                alarmList.add(alarm);
            }
        }
        response.setData(alarmList);
        return response;
    }

    /**
     * APP获取报警信息
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/api/getAlarmByUserId")
    public Response getAlarmByUserId(Integer page, @RequestParam(defaultValue = "20") int size,Integer userId) {
        Response response = new Response();
        int page1 = (page - 1) * size;
        Map<String, Object> map = new HashMap<>();
        map.put("page", page1);
        map.put("size", size);
        map.put("userId", userId);
        //返回的信息
        List<Alarm> alarmList = alarmMapper.selectByUserId(map);
        response.setData(alarmList);
        return response;
    }


    @ResponseBody
    @PostMapping(value = "/deleteById")
    public Response updateState(Integer alarmId) {
        Response response = new Response();
        int result = alarmMapper.deleteByPrimaryKey(alarmId);
        if (result <= 0) {
            response.setErrorMsg("删除失败!");
            response.setErrorId(1002);
        } else {
            response.setData("删除成功");
        }
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/addAlarm")
    public  Response addAlarm(@RequestBody Alarm alarm) {
        Response response = new Response();
        if(alarm != null){
            if(alarm.getAlarmId() == null || alarm.getAlarmId() == 0){
                int result = alarmMapper.insertSelective(alarm);
                if (result != 0) {
                    response.setData("添加成功");
                } else {
                    response.setErrorMsg("添加失败，请重试!");
                    response.setErrorId(1002);
                }
            }else{
                int result = alarmMapper.updateByPrimaryKeySelective(alarm);
                if (result != 0) {

                    response.setData("修改成功");
                } else {
                    response.setErrorMsg("修改失败，请重试!");
                    response.setErrorId(1002);
                }
            }

        }else{
            response.setErrorMsg("数据不能为空!");
            response.setErrorId(1002);
        }


        return response;
    }


    @ResponseBody
    @PostMapping(value = "/api/updateAlarmUserId")
    public  Response updateAlarmUserId(Integer userId,Integer alarmId) {
        Response response = new Response();
        //先查询有没有userId,如果没有则修改
        Alarm alarm = alarmMapper.selectByPrimaryKey(alarmId);
        if(alarm.getUserId() == null || alarm.getUserId() == 0){
            int result = alarmMapper.updateAlarmUserId(userId,alarmId);
            if (result != 0) {
                response.setData("接受成功");
            } else {
                response.setErrorMsg("接受失败，请重试!");
                response.setErrorId(1002);
            }
        }else{
            response.setErrorMsg("已有其他人处理");
            response.setErrorId(1002);
        }
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/api/updateAlarmStatus")
    public  Response updateAlarmStatus(Integer alarmId,Integer status,String alarmRemark) {
        Response response = new Response();
        int result = alarmMapper.updateStateById(status,alarmId,alarmRemark);
        if (result != 0) {
            response.setData("处理成功");
        } else {
            response.setErrorMsg("处理成功，请重试!");
            response.setErrorId(1002);
        }
        return response;
    }
}
