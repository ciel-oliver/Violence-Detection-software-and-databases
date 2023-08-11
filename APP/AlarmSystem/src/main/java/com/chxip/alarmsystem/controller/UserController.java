package com.chxip.alarmsystem.controller;

import com.chxip.alarmsystem.dao.UserMapper;
import com.chxip.alarmsystem.entity.Alarm;
import com.chxip.alarmsystem.entity.Notification;
import com.chxip.alarmsystem.entity.Response;
import com.chxip.alarmsystem.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper ) {
        this.userMapper = userMapper;
    }


    @GetMapping(value = "/toUserList")
    public String toUserList(Integer userType,Model model){
        model.addAttribute("userType",userType);
        return "userlist";
    }
    @GetMapping(value = "/toUpdateUser")
    public String toUpdateUser(Integer userId, Model model){
        User user = userMapper.selectByPrimaryKey(userId);
        model.addAttribute("user",user);
        return "addUser";
    }
    @GetMapping(value = "/toAddUser")
    public String toAddUser(Integer userType, Model model){
        User user = new User();
        user.setType(userType);
        model.addAttribute("user",user);
        return "addUser";
    }


    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param model
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public Response login(String userName, String password, Model model, HttpServletRequest request) {
        Response response = new Response();

        Map<String, String> map = new HashMap<>();
        map.put("username", userName);
        map.put("password", password);

        User user = userMapper.selectByPassword(map);
        if (user == null) {
            response.setErrorMsg("账号密码错误!");
            response.setErrorId(1002);
        } else {
            response.setData(user);
            model.addAttribute("USER", user);
            request.getSession().setAttribute("USER", user);
        }
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/loginout")
    public Response loginout( HttpServletRequest request) {
        Response response = new Response();
        response.setData("退出成功");
        request.getSession().removeAttribute("USER");
        return response;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param model
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/api/login")
    public Response loginApi(String username, String password, Model model, HttpServletRequest request) {
        Response response = new Response();

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        User user = userMapper.selectByPassword(map);
        if (user == null) {
            response.setErrorMsg("账号密码错误!");
            response.setErrorId(1002);
        } else {
            response.setData(user);
        }
        return response;
    }
    /**
     * 根据手机号获取用户信息
     *
     * @param phone
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getUserByPhone")
    public Response getUserByPhone(String phone) {
        Response response = new Response();
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            response.setErrorMsg("手机号不存在!");
            response.setErrorId(1002);
        } else {
            response.setData(user);
        }
        return response;
    }



    /**
     * 添加用户
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/api/addUser")
    public Response addUser(@RequestBody User user) {
        Response response = new Response();
        user.setState(1);
        User selectUser = userMapper.selectByPhoneAndAccount(user.getPhone(),user.getAccount());
        if (selectUser == null) {
            int result = userMapper.insertSelective(user);
            if (result <= 0) {
                response.setErrorMsg("添加失败!");
                response.setErrorId(1002);
            } else {
                response.setData(user);
            }
        } else {
            response.setErrorMsg("手机号已存在!");
            response.setErrorId(1002);
        }

        return response;
    }

    @ResponseBody
    @PostMapping(value = "/addUser")
    public  Response addUserWeb(@RequestBody User user) {
        Response response = new Response();
        if(user != null){
            if(user.getId() == null || user.getId() == 0){
                int result = userMapper.insertSelective(user);
                if (result != 0) {
                    response.setData("添加成功");
                } else {
                    response.setErrorMsg("添加失败，请重试!");
                    response.setErrorId(1002);
                }
            }else{
                int result = userMapper.updateByPrimaryKeySelective(user);
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



    /**
     * 修改
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/api/updateUser")
    public Response updateUser(@RequestBody User user) {
        Response response = new Response();
        int result = userMapper.updateByPrimaryKey(user);
        if (result <= 0) {
            response.setErrorMsg("修改失败!");
            response.setErrorId(1002);
        } else {
            response.setData(user);
        }
        return response;
    }

    /**
     * 修改密码
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/api/updatePassword")
    public Response updatePassword(String newPassword,String oldPassword,int userId) {
        Response response = new Response();
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            int result = userMapper.updateByPrimaryKey(user);
            if (result <= 0) {
                response.setErrorMsg("修改失败!");
                response.setErrorId(1002);
            } else {
                response.setData(user);
            }
        }else{
            response.setErrorMsg("旧密码错误!");
            response.setErrorId(1002);
        }

        return response;
    }
    /**
     * 获取用户列表
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/getUserAll")
    public  Map<String, Object> getUserAll(Integer page, @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "") String userName,
                               @RequestParam(defaultValue = "") String phone,Integer userType) {
        int page1 = (page - 1) * size;
        Map<String, Object> map = new HashMap<>();
        map.put("page", page1);
        map.put("size", size);
        map.put("userName", userName);
        map.put("phone", phone);
        map.put("userType", userType);
        //返回的信息
        List<User> userList = userMapper.selectAll(map);
        int countNumber = userMapper.selectAllCount(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("data", userList);
        resultMap.put("count", countNumber);

        return resultMap;
    }



    @ResponseBody
    @PostMapping(value = "/updateState")
    public Response updateState(Integer userId, Integer state) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("id", userId);
        map.put("state", state);
        int result = userMapper.updateStateById(map);
        if (result <= 0) {
            response.setErrorMsg("修改失败!");
            response.setErrorId(1002);
        } else {
            response.setData("修改成功");
        }
        return response;
    }
}
