package com.ping.alarmsystem.util

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.RandomAccessFile
import java.lang.Exception

/**
 * @ClassName: FileUtils
 * @Description: java类作用描述
 */
object FileUtils {

    fun writeData(fileName:String,content:String) {
        val filePath = Environment.getExternalStorageDirectory().absolutePath + "/智控/Data/"
        writeTxtToFile(content, filePath, fileName)
    }

    // 将字符串写入到文本文件中
    private fun writeTxtToFile(strcontent: String, filePath: String, fileName: String) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName)
        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        try {
            val file = File(strFilePath)
            if(file.exists()){
                file.delete()
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strcontent.toByteArray())
            raf.close()
        } catch (e: Exception) {
        }
    }

//生成文件

    //生成文件
    private fun makeFilePath(filePath: String, fileName: String): File? {
        var file: File? = null
        makeRootDirectory(filePath)
        try {
            file = File(filePath + fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    //生成文件夹
    private fun makeRootDirectory(filePath: String) {
        var file: File? = null
        try {
            file = File(filePath)
            if (!file.exists()) {
                file.mkdir()
            }
        } catch (e: Exception) {
            Log.i("error:", e.toString() + "")
        }
    }
}