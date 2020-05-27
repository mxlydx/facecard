package cc.gooa.facecard.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class OkHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    /**
     * xml格式post请求接口调用
     * @param url 接口地址
     * @param xmlStr xml格式请求参数体
     * @return
     */
    public static String postXml(String url, String xmlStr) throws Exception {
        RequestBody body=RequestBody.create(MediaType.parse("application/xml"),xmlStr);
        Request requestOk = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response;
        response = new OkHttpClient().newCall(requestOk).execute();
        String jsonString = response.body().string();
        logger.error("-----response start -----");
        logger.error(jsonString);
        logger.error("-----response end -----");
        if(response.isSuccessful()){
            return jsonString;
        }
        return "";
    }

    /**
     * get请求接口，返回json
     * @param url 接口地址
     * @return
     */
    public static String getJson(String url){
// RequestBody body=RequestBody.create(MediaType.parse("application/json"),"");
        Request requestOk = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response;
        try {
            response = new OkHttpClient().newCall(requestOk).execute();
            String jsonString = response.body().string();
            if(response.isSuccessful()){
                return jsonString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    /**
     * 发送post请求
     * @param url
     * @param json
     */
    public static String postJson(String url, String json) {
        //申明给服务端传递一个json串
        MediaType JSON = MediaType.parse("application/json;charset=UTF-8");
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        Response response= null;
        try {
            response = okHttpClient.newCall(request).execute();
            String jsonString = response.body().string();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                return jsonString;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }

    /**
     * 发送文件
     * @param url 请求接口地址
     * @param uploadDir 参数上传目录
     * @param baseFileUrl 文件保存基准路径
     * @param relativeUrl 文件保存的相对路径
     * @return 接口返回值
     * 该方法前端以formData格式传入，包括文件和参数，可一起传入。
     */
    public static String uploadFilePost(String url, String uploadDir, String baseFileUrl, String relativeUrl){

        File temporaryFile = new File(baseFileUrl+relativeUrl);
        if(!temporaryFile.exists()){
            return "";
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("uploadDir", uploadDir) //参数一，可注释掉
                .addFormDataPart("fileUrl", relativeUrl) //参数二，可注释掉
                .addFormDataPart("file", temporaryFile.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),temporaryFile)) //文件一
                .build();
        Request requestOk = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response;
        try {
            response = new OkHttpClient().newCall(requestOk).execute();
            String jsonString = response.body().string();
// temporaryFile.delete();
            if(response.isSuccessful()){
                return jsonString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
