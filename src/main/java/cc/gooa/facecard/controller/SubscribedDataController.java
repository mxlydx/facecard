package cc.gooa.facecard.controller;

import cc.gooa.facecard.base.SuccessResponse;
import cc.gooa.facecard.model.BusCreditLog;
import cc.gooa.facecard.service.BusCreditLogService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SubscribedDataController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BusCreditLogService busCreditLogService;

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    public String receiveLogData(@RequestBody Object data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = (JSONObject) JSON.toJSON(data);
        // 组织入库bean
        BusCreditLog bean = new BusCreditLog();
        bean.setCustomercode(json.getString("PersonUUID"));
        bean.setCard(json.getString("IdCard"));
        bean.setDevicecode(json.getString("DeviceID"));
        String base64 = json.getString("SanpPic");
        if (base64 != null) {
            bean.setPict(Base64.getDecoder().decode(base64));
        }
        Date creditTime = new Date();
        try {
            creditTime =  json.getDate("ValidBegin");
        } catch (Exception e) {
            logger.error("get ValidBegin to date error ");
        }
        bean.setCredittime(creditTime);
        bean.setUploadtime(new Date());
        bean.setStatus(1);
        JSONObject res = new JSONObject();
        try {
            busCreditLogService.insert(bean);
            res.put("desc", "OK");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("desc", "FAILED");
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {

        }
        return new SuccessResponse(res).toString();
    }

}
