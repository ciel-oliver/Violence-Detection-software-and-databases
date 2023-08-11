package com.chxip.alarmsystem.controller;
 
import com.chxip.alarmsystem.dao.NotificationMapper;
import com.chxip.alarmsystem.entity.Notification;
import com.chxip.alarmsystem.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    final NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }




    @GetMapping(value = "/toNotificationList")
    public String toNotificationList(Model model){
        return "notificationlist";
    }

    @GetMapping(value = "/toAddNotification")
    public String toAddNotification(Model model){
        model.addAttribute("notification",new Notification());
        return "addNotification";
    }

    @GetMapping(value = "/toUpdateNotification")
    public String toUpdateNotification(Integer notificationId, Model model){
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        model.addAttribute("notification",notification);
        return "addNotification";
    }


    /**
     * 获取通知
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getNotification")
    public Map<String, Object> getNotification(Integer page, @RequestParam(defaultValue = "20") int size,
                                                 @RequestParam(defaultValue = "") String keyWord ) {
        int page1 = (page - 1) * size;
        Map<String, Object> map = new HashMap<>();
        map.put("page", page1);
        map.put("size", size);
        map.put("keyWord", keyWord);
        //返回的信息
        List<Notification> notificationList = notificationMapper.selectAll(map);
        int countNumber = notificationMapper.selectAllCount(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("data", notificationList);
        resultMap.put("count", countNumber);

        return resultMap;
    }

    @ResponseBody
    @GetMapping(value = "/api/getNotification")
    public Response getNotificationApi(Integer page, @RequestParam(defaultValue = "20") int size) {
        Response response = new Response();
        int page1 = (page - 1) * size;
        Map<String, Object> map = new HashMap<>();
        map.put("page", page1);
        map.put("size", size);
        map.put("keyWord", "");
        //返回的信息
        List<Notification> notificationList = notificationMapper.selectAll(map);
        response.setData(notificationList);
        return response;
    }


    @ResponseBody
    @GetMapping(value = "/api/getNewNotification")
    public Response getNewNotification(String date) {
        Response response = new Response();
        //返回的信息
        int number = notificationMapper.getNewNotification(date);
        response.setData(number);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/deleteById")
    public Response updateState(Integer notificationId) {
        Response response = new Response();
        int result = notificationMapper.deleteByPrimaryKey(notificationId);
        if (result <= 0) {
            response.setErrorMsg("删除失败!");
            response.setErrorId(1002);
        } else {
            response.setData("删除成功");
        }
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/addNotification")
    public  Response addNotification(@RequestBody Notification notification) {
        Response response = new Response();
        if(notification != null){
            if(notification.getNotificationId() == null || notification.getNotificationId() == 0){
                int result = notificationMapper.insertSelective(notification);
                if (result != 0) {
                    response.setData("添加成功");
                } else {
                    response.setErrorMsg("添加失败，请重试!");
                    response.setErrorId(1002);
                }
            }else{
                int result = notificationMapper.updateByPrimaryKeySelective(notification);
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

}
