package com.xhr.mca.common;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Message类，可以增加字段提高安全性，例如时间戳、url签名
 * 
 * @author Huang Sheng
 * @date 2018/8/28.
 */
@Data
public class Message {

    /** 返回状态吗 **/
    private Integer code;
    /** 返回消息 **/
    private String msg;
    /** 返回数据 **/
    private JSONObject data;

    /**  返回boolean数据  */
    private String geetestFail;
}
