package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.bean.DeviceInfo;
import cc.gooa.facecard.bean.FaceServerData;
import cc.gooa.facecard.service.FaceServerLinkService;
import cc.gooa.facecard.utils.RequestFaceServer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class FaceServerLinkServiceImpl  implements FaceServerLinkService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;
    @Autowired
    private RequestFaceServer requestFaceServer;

    @Override
    public boolean connect() {
        String faceServer = env.getProperty("facedevice.server");
        logger.info("start connect to facedevice.server：" + faceServer);
        OkHttpClient client = requestFaceServer.buildBasicAuthClient();
        RequestBody requestBody  = RequestBody.create(RequestFaceServer.MediaJSON, "");
        // 从env中获取连接方法，此处配置的是GetSysParam方法获取设备信息，可以更换
        Request req = new Request.Builder().url(faceServer + env.getProperty("facedevice.method.connect")).post(requestBody).build();
        Call call = client.newCall(req);
        try {
            Response res = call.execute();
            String resBody = res.body().string();
            if (res.code() == 200) {
                JSONObject json = JSON.parseObject(resBody);
                DeviceInfo deviceInfo = DeviceInfo.getInstance();
                JSONObject info = JSON.parseObject(json.getString("info"));
                deviceInfo.setDeviceId(info.getString("DeviceID"));
                deviceInfo.setName(info.getString("Name"));
                deviceInfo.setVersion(info.getString("Version"));
                logger.info("Success connected to " + faceServer);
                logger.info("----------Device Info----------");
                logger.info(json.toJSONString());
                logger.info("----------Device Info----------");
                return true;
            } else {
                logger.error("Failed to connect facedevice.server because => " + resBody);
                return false;
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return false;
    }

    @Override
    public void subscribe() {
        logger.info(" try to subscribe heartbeat and verify data");
        // 订阅方法
        String method = env.getProperty("facedevice.method.subscribe");
        JSONObject info =  new JSONObject();
        // 获取设备信息
        DeviceInfo deviceInfo = DeviceInfo.getInstance();
        // 组织info查询信息
        info.put("DeviceID", deviceInfo.getDeviceId());
        info.put("Num", 1);
        JSONArray topics = new JSONArray();
        // 订阅认证信息
        topics.add("Verify");
        info.put("Topics", topics);
        // 组织回传信息
        String server  = env.getProperty("facecard.server");
        String port = env.getProperty("server.port");
        // 回传服务ip+port
        info.put("SubscribeAddr", server + ":" + port + "/");
        // 具体服务地址
        JSONObject subscribeUrl = new JSONObject();
        // 回传心跳服务地址
        String heartBeat = env.getProperty("facecard.subscribe.heartbeat");
        // 回传认证结果地址
        String verify = env.getProperty("facecard.subscribe.verify");
        subscribeUrl.put("Verify", verify);
        subscribeUrl.put("HeartBeat", heartBeat);
        info.put("SubscribeUrl", subscribeUrl);
        // 提交上报信息时是否要进行认证
        info.put("Auth", "none");
        JSONObject postData = new JSONObject();
        postData.put("operator", method);
        postData.put("info", info);

        FaceServerData data  = new FaceServerData(method, postData);
        try {
            JSONObject json =  requestFaceServer.request(method, JSON.toJSONString(data));
            if (json !=null && "200".equals(json.getString("code"))) {
                logger.info("success subscribed!");
            } else {
                logger.info("failed subscribed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }


    }
}