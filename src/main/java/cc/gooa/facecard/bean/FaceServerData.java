package cc.gooa.facecard.bean;

import com.alibaba.fastjson.JSONObject;

public class FaceServerData {
    private String operator;
    private JSONObject info;

    public FaceServerData (String operator, JSONObject info) {
        this.operator = operator;
        this.info = info;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public JSONObject getInfo() {
        return info;
    }

    public void setInfo(JSONObject info) {
        this.info = info;
    }
}
