package cc.gooa.facecard.service;

public interface CustomerBasicModelService {
    void pushData(String faceServer, String deviceId);
    void pushData(String deviceId);
    void synoData(boolean reset,String deviceId);
    void addUser(int id);
}
