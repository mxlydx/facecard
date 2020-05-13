package cc.gooa.facecard.base;

import com.alibaba.fastjson.annotation.JSONField;

public class MqttUserInfo {
    /**
     * 一体机上的 ID，区分每一个人员
     */
    private String customId;

    /**
     * 0 永久 1 临时卡类型 1(时间段) 2 临时卡类型 2(每天时间段) 3 临时卡类型 3(次数)
     */
    private int tempCardType = 0;


    /**
     * 临时卡类型 3 的次数
     */
    private int EffectNumber;


    /**
     * 人物类别 :0: 白名单 1: 黑名单 Y
     */
    private int personType = 0;


    /**
     * 人员图片(base64 编码，不超过 1M)，和 picURI 2选1
     */
    private String pic;


    /**
     * 人员姓名
     */
    private String name;

    /**
     * 性别
     */
    private int gender;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * RF卡号
     */

    @JSONField(name = "RFIDCard")
    private  String RFIDCard;
    /**
     * 删除所有用户标志
     */
    private String deleteall;

    public String getDeleteall() {
        return deleteall;
    }

    public void setDeleteall(String deleteall) {
        this.deleteall = deleteall;
    }

    /**
     *
     * @param customId
     * @param name
     * @param gender
     * @param pic
     * @param card
     */

    public MqttUserInfo (String customId, String name, int gender, String pic, String card) {
        this.customId = customId;
        this.name = name;
        this.gender = gender;
        this.pic = pic;
        this.RFIDCard = card;
    }

    public MqttUserInfo() {
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public int getTempCardType() {
        return tempCardType;
    }

    public void setTempCardType(int tempCardType) {
        this.tempCardType = tempCardType;
    }

    public int getEffectNumber() {
        return EffectNumber;
    }

    public void setEffectNumber(int effectNumber) {
        EffectNumber = effectNumber;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRFIDCard() {
        return RFIDCard;
    }

    public void setRFIDCard(String RFIDCard) {
        this.RFIDCard = RFIDCard;
    }
}
