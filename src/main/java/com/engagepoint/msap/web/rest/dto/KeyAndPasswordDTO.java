package com.engagepoint.msap.web.rest.dto;

public class KeyAndPasswordDTO {

    private String key;
    private String newPassword;

    public KeyAndPasswordDTO() {
        // nothing to do
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
