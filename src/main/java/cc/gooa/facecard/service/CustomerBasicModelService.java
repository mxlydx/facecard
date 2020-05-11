package cc.gooa.facecard.service;

public interface CustomerBasicModelService {
    void pushData(String faceServer, String deviceId);
    void pushDataBySchool(int schoolId, String deviceId);
    void synoData(boolean reset, int schoolId, String deviceId);
    void addUser(int id, String deviceIds);
    void deleteUser(int id, String deviceIds);
    void deleteAllUser(String deviceIds);
}
