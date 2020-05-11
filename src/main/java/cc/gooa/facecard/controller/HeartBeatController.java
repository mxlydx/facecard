package cc.gooa.facecard.controller;

import cc.gooa.facecard.base.RedisKey;
import cc.gooa.facecard.model.BusDevices;
import cc.gooa.facecard.service.BusDevicesService;
import cc.gooa.facecard.service.impl.OnOffCache;
import cc.gooa.facecard.utils.RedisUtil;
import cc.gooa.facecard.utils.RequestFaceServer;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HeartBeatController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RequestFaceServer requestFaceServer;
    @Resource
    private OnOffCache onOffCache;
    @Autowired
    private BusDevicesService busDevicesService;

    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    @ResponseBody
    public String beat(@RequestBody Object data,  HttpServletRequest request, HttpServletResponse response) {
        logger.info("Heart receive ping");
        logger.info("receive data : " + data);
        Map receiveData = (Map) data;
        Map info = (Map) receiveData.get("info");
        String deviceId = info.get("DeviceID").toString();
        Object devices = RedisUtil.getValue(RedisKey.CACHE_DEVICE_ID + deviceId);
        // 设备在线封装
        BusDevices online = new BusDevices();
        online.setCode(deviceId);
        online.setStatus(2);
        // -------------
        if (devices == null) {
            // 设备第一次心跳
            // 缓存
            onOffCache.put(deviceId, deviceId);
            // 缓存设备记录
            RedisUtil.setValue(RedisKey.CACHE_DEVICE_ID + deviceId, "1");
            // 缓存设备在线
            RedisUtil.setValue(RedisKey.CACHE_DEVICE_ONLINE + deviceId, "1");
            // 更新设备在线状态
            busDevicesService.updateStatusByCode(online);
        } else {
            // 获取设备是否在线缓存信息
            if (onOffCache.updateAliveTime(deviceId)) {
            } else {
                // 缓存信息
                onOffCache.put(deviceId, deviceId);
            }
            Object onlineStatus = RedisUtil.getValue(RedisKey.CACHE_DEVICE_ONLINE + deviceId);
            if (onlineStatus == null) {
                // --- 不在线时
                // 缓存信息
                onOffCache.put(deviceId, deviceId);
                // redis更新在线状态
                RedisUtil.setValue(RedisKey.CACHE_DEVICE_ONLINE + deviceId, "1");
                // 数据库更新在线状态
                busDevicesService.updateStatusByCode(online);
            }


        }
        logger.info("Heart response pong");
        return data.toString();
    }
}
