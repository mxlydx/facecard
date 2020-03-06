package cc.gooa.facecard.service;

public interface FaceServerLinkService {
    boolean connect(String faceServerUrl);
    void subscribe(String faceServerUrl);
}
