package cc.gooa.facecard.controller;

import cc.gooa.facecard.base.SuccessResponse;
import cc.gooa.facecard.service.CustomerBasicModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PushUserInfoController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Environment environment;
    @Autowired
    CustomerBasicModelService customerBasicModelService;

    /**
     * 按照id下发名单
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    @ResponseBody
    public String addUser(@RequestParam int id, HttpServletRequest request, HttpServletResponse response) {
        logger.info("addUser request start");
        customerBasicModelService.addUser(id);
        return new SuccessResponse("OK").toString();
    }

    /**
     * 重新同步用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/synoUser", method = RequestMethod.GET)
    @ResponseBody
    public String pushUserInfo(HttpServletRequest request, HttpServletResponse response) {
        logger.info("synoUser request start");
        String deviceIds = environment.getProperty("facedevice.deviceIds", String.class);
        if (deviceIds != null) {
            String[] devices = deviceIds.split(",");
            for (String deviceId : devices) {
                customerBasicModelService.synoData(true,deviceId);
            }
        }
        return new SuccessResponse("OK").toString();
    }
}
