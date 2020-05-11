package cc.gooa.facecard.base;

import com.alibaba.fastjson.JSON;

public class BadRequestResponse {
   private Object data;
   private int code = 400;

    public BadRequestResponse(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
