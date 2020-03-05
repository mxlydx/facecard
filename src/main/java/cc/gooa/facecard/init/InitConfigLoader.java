package cc.gooa.facecard.init;

import cc.gooa.facecard.service.CustomerBasicModelService;
import cc.gooa.facecard.service.FaceServerLinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;



@Component
public class InitConfigLoader implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FaceServerLinkService faceServerLinkService;
    @Autowired
    private CustomerBasicModelService customerBasicModelService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
      boolean success =  faceServerLinkService.connect();
      if (success) {
          faceServerLinkService.subscribe();
          customerBasicModelService.synoData();
      } else {
          logger.info("failed to connect");
      }
    }
}
