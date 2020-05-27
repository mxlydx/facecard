package cc.gooa.facecard.init;

import cc.gooa.facecard.service.CustomerBasicModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * mqtt启动器，想mqtt服务器发布数据
 */
@Component
public class ApplicationMqttDataLoader {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Environment environment;

    @Autowired
    private CustomerBasicModelService customerBasicModelService;

//    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取设备id
        String deviceIds = environment.getProperty("facedevice.deviceIds", String.class);
        // 获取学校id
        String schoolId = environment.getProperty("facecard.schoolId", String.class);
        if (deviceIds != null) {
            String[] devices = deviceIds.split(",");
            for (String deviceId : devices) {
                customerBasicModelService.synoData(false, Integer.parseInt(schoolId), deviceId);
            }
        }

    }
}
