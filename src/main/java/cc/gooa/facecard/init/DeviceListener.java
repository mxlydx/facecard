package cc.gooa.facecard.init;

import com.tone.cache.liver.recycle.ReclaimerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 设备在线监控器
 */
@Component
public class DeviceListener implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(ReclaimerFactory.getReclaimer()).start();
    }
}
