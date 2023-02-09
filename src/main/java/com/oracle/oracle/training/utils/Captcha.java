package com.oracle.oracle.training.utils;

import static com.oracle.oracle.training.constants.Constants.CAPTCHA_VALIDITY_TIME;

public class Captcha {
    private String id;
    private String CAPTCHA;
    private final long TIMESTAMP;

    public Captcha(String id, String captcha, long timestamp) {
        this.id = id;
        CAPTCHA = captcha;
        TIMESTAMP = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCAPTCHA() {
        return CAPTCHA;
    }

    public void setCAPTCHA(String CAPTCHA) {
        this.CAPTCHA = CAPTCHA;
    }

    public long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public boolean isExpired(){
        return (System.currentTimeMillis() - TIMESTAMP) > CAPTCHA_VALIDITY_TIME;
    }
}
