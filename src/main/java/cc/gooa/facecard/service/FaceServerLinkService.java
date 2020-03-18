package cc.gooa.facecard.service;

public interface FaceServerLinkService {
    String connect(String faceServerUrl);
    void subscribe(String faceServerUrl, String deviceId);
    void subscribe(String deviceId);
}
