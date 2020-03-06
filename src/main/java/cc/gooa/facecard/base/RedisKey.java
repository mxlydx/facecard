package cc.gooa.facecard.base;

public enum RedisKey {

    SYNOED_IDS("synoed_ids:");

    private String key;

    RedisKey (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
