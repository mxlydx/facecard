package cc.gooa.facecard.controller;

import cc.gooa.facecard.base.BadRequestResponse;
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
    public Object addUser(@RequestParam int id, @RequestParam String deviceIds,HttpServletRequest request, HttpServletResponse response) {
        logger.info("addUser request start");
        customerBasicModelService.addUser(id, deviceIds);
        return new SuccessResponse("OK");
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteUser(@RequestParam int id, @RequestParam String deviceIds,HttpServletRequest request, HttpServletResponse response) {
        logger.info("delete request start");
        customerBasicModelService.deleteUser(id, deviceIds);
        return new SuccessResponse("OK");
    }

    /**
     * 同步用户
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/synoUser", method = RequestMethod.GET)
    @ResponseBody
    public Object pushUserInfo(@RequestParam int schoolId, @RequestParam String deviceIds, HttpServletRequest request, HttpServletResponse response) {
        logger.info("synoUser request start");
        if (deviceIds != null) {
            String[] devices = deviceIds.split(",");
            for (String deviceId : devices) {
                customerBasicModelService.synoData(false, schoolId, deviceId);
            }
        } else {
            return new BadRequestResponse("Param Error").toString();
        }
        return new SuccessResponse("OK");
    }

    /**
     *
     * @param deviceIds
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/deleteAllUser", method = RequestMethod.GET)
    @ResponseBody
    public Object deleteAllUser(@RequestParam String deviceIds,HttpServletRequest request, HttpServletResponse response) {
        logger.info("delete All request start");
        customerBasicModelService.deleteAllUser(deviceIds);
        return new SuccessResponse("OK");
    }
}
