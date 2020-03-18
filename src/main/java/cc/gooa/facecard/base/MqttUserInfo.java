package cc.gooa.facecard.base;

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

    public MqttUserInfo (String customId, String name, int gender, String pic) {
        this.customId = customId;
        this.name = name;
        this.gender = gender;
        this.pic = pic;
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
}
