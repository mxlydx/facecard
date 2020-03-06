package cc.gooa.facecard.base;

public class DeviceInfo {
    private String name;
    private String deviceId;
    private String version;
    private static DeviceInfo deviceInfo = new DeviceInfo();

    private DeviceInfo() {

    }

    public static DeviceInfo getInstance() {
        return deviceInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
