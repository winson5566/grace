package com.awinson.valid;

import javax.validation.constraints.NotNull;

/**
 * Created by winson on 2016/12/10.
 */
public class ApiKeyValid {
    @NotNull
    private String apiKey;
    @NotNull
    private String secretKey;
    @NotNull
    private String platform;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
