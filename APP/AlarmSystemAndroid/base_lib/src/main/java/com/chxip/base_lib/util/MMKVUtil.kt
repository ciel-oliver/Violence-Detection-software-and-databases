package com.chxip.base_lib.util

import android.os.Parcelable
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV


/**
 * @ClassName: MMKVUtil
 * @Description: MMKV
 * @Author: chxip
 * @CreateDate: 2021/6/9 16:47
 */
object MMKVUtil {

    private lateinit var mmkv: MMKV

    init {
        mmkv = MMKV.defaultMMKV()!!
    }

    /**
     * 保存信息
     */
    fun <T> save(key: String, value: T) {
        when (value) {
            is String -> {
                mmkv.encode(key, value)
            }
            is Int -> {
                mmkv.encode(key, value)
            }
            is Boolean -> {
                mmkv.encode(key, value)
            }
            is Double -> {
                mmkv.encode(key, value)
            }
            is Parcelable -> {
                mmkv.encode(key, value)
            }

        }
    }

    /**
     * 保存对象信息
     */
    fun <T> saveObject(key: String, value: T) {
        if (value != null) {
            mmkv.encode(key, GsonUtil.GSON.toJson(value))
        }
    }


    /**
     * 获取信息
     */
    fun <T> get(key: String, defaultValue: T): T {
        // 获取数据
        when (defaultValue) {
            is String -> {
                return mmkv.decodeString(key, defaultValue) as T
            }
            is Int -> {
                return mmkv.decodeInt(key, defaultValue) as T
            }
            is Boolean -> {
                return mmkv.decodeBool(key, defaultValue) as T
            }
            is Double -> {
                return mmkv.decodeDouble(key, defaultValue) as T
            }
            else -> {
                return defaultValue
            }
        }
    }

    /**
     * 获取 对象
     */
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val objectString = mmkv.decodeString(key, "")
        if (objectString.equals("")) {
            return null
        } else {
            return GsonUtil.GSON.fromJson(objectString, clazz)
        }

    }

    /**
     * 获取 对象
     */
    fun <T> getList(key: String, t: T): T {
        val objectString = mmkv.decodeString(key, "")
        if (objectString.equals("")) {
            return t
        } else {
            return GsonUtil.GSON.fromJson(objectString, object : TypeToken<ArrayList<T>>() {}.type)
        }

    }

    /**
     * 删除数据
     */
    fun remove(key: String) {
        mmkv.removeValueForKey(key);
    }

}

