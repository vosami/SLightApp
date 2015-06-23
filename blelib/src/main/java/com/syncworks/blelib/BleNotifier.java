package com.syncworks.blelib;

/**
 * Created by vosami on 2015-03-19.
 * 블루투스 연결 메시지 전달 인터페이스
 */
public interface BleNotifier {
    /**
     * 연결 완료 메시지
     */
    public void bleConnected();

    /**
     * 연결 종료 메시지
     */
    public void bleDisconnected();

    /**
     * 블루투스 서비스 검색 완료
     */
    public void bleServiceDiscovered();

    /**
     * 데이터 수신 완료
     */
    public void bleDataAvailable();

    /**
     * 데이터 송신 완료
     */
    public void bleDataWriteComplete();
}
