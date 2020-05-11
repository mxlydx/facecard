package cc.gooa.facecard.base;

public enum RedisKey {

    SYNOED_IDS("synchronized:id:"),
    CACHE_DEVICE_ID("cache:device:id:"),
    CACHE_DEVICE_ONLINE("cache:device:online:id:");

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
