package cc.gooa.facecard.controller;

import cc.gooa.facecard.utils.RequestFaceServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HeartBeatController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RequestFaceServer requestFaceServer;

    @RequestMapping(value = "/heartbeat", method = RequestMethod.POST)
    @ResponseBody
    public String beat(@RequestBody Object data,  HttpServletRequest request, HttpServletResponse response) {
        logger.info("Heart receive ping");
        logger.info("receive data : " + data);
//        requestFaceServer.request(method, data.toString());
        logger.info("Heart response pong");
        return data.toString();
    }
}
