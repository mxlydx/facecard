package cc.gooa.facecard.init;

import cc.gooa.facecard.service.CustomerBasicModelService;
import cc.gooa.facecard.service.FaceServerLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;



@Component
public class InitConfigLoader implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FaceServerLinkService faceServerLinkService;
    @Autowired
    private CustomerBasicModelService customerBasicModelService;
    @Autowired
    Environment env;

    @Override

    public void run(ApplicationArguments args) throws Exception {
        String faceServers = env.getProperty("facedevice.server");
        if (faceServers !=null) {
            String[] faceServerUrls = faceServers.split(",");
            for (String faceServerUrl : faceServerUrls) {
                connectDevice(faceServerUrl);
            }
        }
    }

    /**
     * 并发执行同步、订阅等操作
     * @param faceServer
     */
    public void connectDevice(String faceServer) {
        String deviceId =  faceServerLinkService.connect(faceServer);
        if (deviceId != null) {
            faceServerLinkService.subscribe(faceServer, deviceId);
            customerBasicModelService.synoData(faceServer, deviceId);
        } else {
            logger.info("failed to connect");
        }
    }
}
