package com.gwghk.crm.check.vo;

public class ReturnVO {
    private String token;
    private String track;

    public ReturnVO(String token, String track) {
        this.token = token;
        this.track = track;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
