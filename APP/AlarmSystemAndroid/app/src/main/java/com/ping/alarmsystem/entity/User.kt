package com.ping.alarmsystem.entity

import java.io.Serializable

/**
 * @Description: 用户信息
 */
class User:Serializable {
    var id: Int = 0;

    var account: String = "";

    var password: String = "";

    var type: Int = 0;

    var name: String = "";

    var imageurl: String = "";

    var phone: String = "";

    var sex: String = "";

    var email: String = "";

    var state: Int = 0;

    var birthday: String = "";

    var userTypeMsg: String = "";
 

}