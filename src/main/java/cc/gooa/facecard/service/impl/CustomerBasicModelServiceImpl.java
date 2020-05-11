package cc.gooa.facecard.service.impl;

import cc.gooa.facecard.base.FaceServerData;
import cc.gooa.facecard.base.MqttMessage;
import cc.gooa.facecard.base.MqttUserInfo;
import cc.gooa.facecard.base.RedisKey;
import cc.gooa.facecard.mapper.CustomerBasicModelMapper;
import cc.gooa.facecard.model.CustomerBasicModel;
import cc.gooa.facecard.service.CustomerBasicModelService;
import cc.gooa.facecard.service.FaceServerLinkService;
import cc.gooa.facecard.utils.MqttUtil;
import cc.gooa.facecard.utils.RedisUtil;
import cc.gooa.facecard.utils.RequestFaceServer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.RedisCommandTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.apache.commons.codec.binary.Base64;

import java.util.List;


@Service
public class CustomerBasicModelServiceImpl implements CustomerBasicModelService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerBasicModelMapper customerBasicModelMapper;
    @Autowired
    private Environment env;
    @Autowired
    private RequestFaceServer requestFaceServer;
    @Autowired
    private FaceServerLinkService faceServerLinkService;


    @Override
    @Async("asyncExecutor")
    public void pushDataBySchool(int schoolId, String deviceId) {
        long start = System.currentTimeMillis();
        int all = customerBasicModelMapper.selectCountAll();
        long countEnd = System.currentTimeMillis();
        logger.info("计数用时：" + (countEnd - start) / 1000 + "s");
        int pageSize = Integer.parseInt(env.getProperty("facecard.synoNum")); // 单页数据量
        int batch = all / pageSize + 1;
        String topic = env.getProperty("mqtt.topic");
        logger.info("共【" + all + "】条数据" + "分【" + batch + "】次同步；每次同步【" + pageSize + "】条");
//        for (int i = 0; i < 1; i++) {
        for (int i = 0; i < batch; i++) {
            logger.info("开始同步第【" + (i + 1) + "】次");
            long batchStart = System.currentTimeMillis();
            List<CustomerBasicModel> customerBasicModelList = customerBasicModelMapper.selectAll(pageSize, i * pageSize, schoolId);
//            List<CustomerBasicModel> customerBasicModelList = customerBasicModelMapper.selectMan();
            long batchQueryEnd = System.currentTimeMillis();
            logger.info("查询第" + (i + 1) + " 批用时：" + (batchQueryEnd - batchStart) / 1000 + "s");
            for (CustomerBasicModel model : customerBasicModelList) {
                try {
                    String key = RedisKey.SYNOED_IDS.getKey() + deviceId + ":" + model.getCode();
                    Object value = RedisUtil.getValue(key);
                    if ( value == null) {
                        logger.info("同步id：【" + model.getId() + "】——【" + model.getName() + "】ing...");
                        boolean online = RequestFaceServer.isHostConnectable("192.168.2.10", 80);
                        if (online) {
                            MqttUtil.publish(topic + deviceId, calData(model, "EditPerson"));
                        } else {
                            while (!online) {
                                logger.info(deviceId + "is offline, waiting it restart");
                                Thread.sleep(20000);
                                online = RequestFaceServer.isReachable("192.168.2.10");
                            }
                            MqttUtil.publish(topic + deviceId, calData(model, "EditPerson"));
                        }

                    } else {
                        logger.info("id：【" + model.getId() + "】——【" + model.getName() + "】has posted now skip!!! ");
                    }
                } catch (RedisCommandTimeoutException exception) {
                    exception.printStackTrace();
                    logger.info("Can't connect to Redis...");
                }catch (InterruptedException iException) {
                    iException.printStackTrace();
                    logger.info("Thread was Interrupted");
                }
            }
        }
    }

    /**
     * 订阅回传信息，并向apollo发布人员名单信息
     * @param reset 是否重新同步？是：不重复订阅
     * @param schoolId 学校id
     * @param deviceId 学校的设备id
     */
    @Override
    public void synoData(boolean reset, int schoolId, String deviceId) {
        if (!reset) {
            faceServerLinkService.subscribeFaceAck(deviceId);
        }
        this.pushDataBySchool(schoolId, deviceId);
    }

    private String calData(CustomerBasicModel model, String operator) {
        MqttMessage message = new MqttMessage();
        int gender = 2;
        if ("男".equals(model.getSex())) {
            gender = 0;
        }
        if ("女".equals(model.getSex())) {
            gender = 1;
        }
        if ("Man".equals(model.getName())) {
            model.setCard("0000000");
        }
        MqttUserInfo user = null;
        switch (operator) {
            case "EditPerson":
                user = new MqttUserInfo(model.getCode(), model.getName(), gender, Base64.encodeBase64String(model.getPhoto()), model.getCard());
                break;
            case "DelPerson":
                user = new MqttUserInfo();
                user.setCustomId(model.getCode());
                break;
            case "DeleteAllPerson":
                user = new MqttUserInfo();
                user.setDeleteall("1");
        }
        message.setInfo(user);
        message.setOperator(operator);
        return JSONObject.toJSONString(message);
    }




    @Override
    @Async("asyncExecutor")
    public void pushData(String faceServer, String deviceId) {
        long start = System.currentTimeMillis();
        int all = customerBasicModelMapper.selectCountAll();
        int schoolId = Integer.parseInt(env.getProperty("facecard.schoolId"));
        long countEnd = System.currentTimeMillis();
        logger.info("计数用时：" + (countEnd - start) / 1000 + "s");
        int pageSize = Integer.parseInt(env.getProperty("facecard.synoNum")); // 单页数据量
        int batch = all / pageSize + 1;
        logger.info("共【" + all + "】条数据" + "分【" + batch + "】次同步；每次同步【" + pageSize + "】条");
        for (int i = 0; i < batch; i++) {
            logger.info("开始同步第【" + (i + 1) + "】次");
            long batchStart = System.currentTimeMillis();
            List<CustomerBasicModel> customerBasicModelList = customerBasicModelMapper.selectAll(pageSize, i * pageSize, schoolId);
            long batchQueryEnd = System.currentTimeMillis();
            logger.info("查询第" + (i + 1) + " 批用时：" + (batchQueryEnd - batchStart) / 1000 + "s");
            for (CustomerBasicModel model : customerBasicModelList) {
                try {
                    if (RedisUtil.getValue(RedisKey.SYNOED_IDS.getKey() + model.getId()) == null) {
                        logger.info("同步id：【" + model.getId() + "】——【" + model.getName() + "】ing...");
                        this.postData2FaceDevice(faceServer,model, deviceId);
                    } else {
                        logger.info("id：【" + model.getId() + "】——【" + model.getName() + "】has posted now skip!!! ");
                    }
                } catch (RedisCommandTimeoutException exception) {
                    logger.info("redis 连接失败！ 请检查redis运行是否正常...");
                }

            }
            long batchEnd = System.currentTimeMillis();
            logger.info("第【" + (i + 1) + "】次 同步完成，用时: " + (batchEnd - batchStart) / 1000 + "s");
        }
        long end = System.currentTimeMillis();
        logger.info("共计用时: " + (end - start) / 1000 + "s");
    }


    public void postData2FaceDevice(String faceServer, CustomerBasicModel model, String deviceId) {
        String method = env.getProperty("facedevice.method.addperson");
        // 组织请求信息
        JSONObject info = new JSONObject();
        info.put("DeviceID", deviceId);
        // 名单类型： 0: 白名单
        info.put("PersonType", 0);
        // 姓名
        info.put("Name", model.getName());
        // 性别
        int gender = 2;
        if ("男".equals(model.getSex())) {
            gender = 0;
        }
        if ("女".equals(model.getSex())) {
            gender = 1;
        }
        info.put("Gender", gender);
        // 主键类型 2： uuid
        info.put("IdType", 2);
        // uuid
        info.put("PersonUUID", model.getCode());
        // 手机号
        info.put("Telnum", model.getPhone());
        // 考勤图片
        info.put("picinfo", Base64.encodeBase64String(model.getPhoto()));
        // 证件类型
        info.put("CardType", 0);
        // 证件号 暂存卡号
        info.put("IdCard", model.getCard());
        //请求数据
        FaceServerData postData = new FaceServerData(method, info);
        //
        long requestStart = System.currentTimeMillis();
        try {
            JSONObject json = requestFaceServer.request(faceServer,method, JSON.toJSONString(postData));
            if (json != null && "200".equals(json.getString("code"))) {
                logger.info("同步成功，用时" + (System.currentTimeMillis() - requestStart) / 1000 + "s");
                // 设置同步成功标识，下次不在同步
                RedisUtil.setValue(RedisKey.SYNOED_IDS.getKey() + model.getId(), 1);
            } else {
                logger.info("请求失败，用时" + (System.currentTimeMillis() - requestStart) / 1000 + "s");
            }
        } catch (Exception e) {
            logger.info("请求失败，用时" + (System.currentTimeMillis() - requestStart) / 1000 + "s");
            e.printStackTrace();
        } finally {
            logger.info("finished request" + method);
        }
    }

    /**
     * 指定用户加入
     * @param id
     */
    @Override
    public void addUser(int id, String deviceIds) {
        CustomerBasicModel model = customerBasicModelMapper.selectByPrimaryKey(id);
        String topic = env.getProperty("mqtt.topic");
        if (deviceIds != null && deviceIds.split(",").length > 0) {
            String[] devices = deviceIds.split(",");
            for (String deviceId: devices) {
                MqttUtil.publish(topic + deviceId, calData(model, "EditPerson"));
            }
        }


    }

    @Override
    public void deleteUser(int id, String deviceIds) {
        CustomerBasicModel model = customerBasicModelMapper.selectByPrimaryKey(id);
        String topic = env.getProperty("mqtt.topic");
        if (deviceIds != null && deviceIds.split(",").length > 0) {
            String[] devices = deviceIds.split(",");
            for (String deviceId: devices) {
                MqttUtil.publish(topic + deviceId, calData(model, "DelPerson"));
            }
        }
    }

    @Override
    public void deleteAllUser(String deviceIds) {
        CustomerBasicModel model = new CustomerBasicModel();
        String topic = env.getProperty("mqtt.topic");
        if (deviceIds != null && deviceIds.split(",").length > 0) {
            String[] devices = deviceIds.split(",");
            for (String deviceId: devices) {
                MqttUtil.publish(topic + deviceId, calData(model, "DeleteAllPerson"));
            }
        }
    }
}
