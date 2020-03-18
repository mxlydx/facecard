package cc.gooa.facecard.init;

import cc.gooa.facecard.service.CustomerBasicModelService;
import cc.gooa.facecard.service.FaceServerLinkService;
import cc.gooa.facecard.utils.MqttUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * mqtt启动器，想mqtt服务器发布数据
 */
@Component
public class ApplicationMqttDataLoader implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    Environment environment;

    @Autowired
    private CustomerBasicModelService customerBasicModelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String deviceIds = environment.getProperty("facedevice.deviceIds", String.class);
        if (deviceIds != null) {
            String[] devices = deviceIds.split(",");
            for (String deviceId : devices) {
                customerBasicModelService.synoData(false, deviceId);
            }
        }

    }
}
