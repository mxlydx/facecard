package cc.gooa.facecard.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


/**
 * @author BillM
 * 请求人脸识别服务工具类
 */
@Component
public class RequestFaceServer {

    private static Logger logger = LoggerFactory.getLogger(RequestFaceServer.class);
    public static final MediaType MediaJSON = MediaType.parse("application/json; charset=utf-8");
    @Autowired
    private  Environment env;

    public JSONObject request(String method, String data) {
        logger.info("start to request：" + method);
        String faceServer = env.getProperty("facedevice.server");
        OkHttpClient client = buildBasicAuthClient();
        RequestBody requestBody;
        if (data != null) {
            requestBody = RequestBody.create(MediaJSON, data);
        } else {
            requestBody = RequestBody.create(MediaJSON, "");
        }
        Request req = new Request.Builder().url(faceServer + method).post(requestBody).build();
        Call call = client.newCall(req);
        try {
            Response res = call.execute();
            String resBody = res.body().string();
            if (res.code() == 200) {
                JSONObject json = JSON.parseObject(resBody);
                logger.info("Success response from  " + method);
                logger.info("----------Response Info----------");
                logger.info(json.toJSONString());
                logger.info("----------Response Info----------");
                return json;
            } else {
                logger.error(method + "failed because => " + resBody);
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return null;
    }

    public  OkHttpClient buildBasicAuthClient() {
        String user = env.getProperty("facedevice.user");
        String pass = env.getProperty("facedevice.password");
        return new OkHttpClient.Builder().authenticator(
                (Route route, Response response) -> {
                    String credential = Credentials.basic(user, pass);
                    return response.request().newBuilder().header("Authorization", credential).build();
                }).build();
    }
}
