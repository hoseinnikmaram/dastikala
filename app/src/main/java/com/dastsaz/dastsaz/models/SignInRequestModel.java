package com.dastsaz.dastsaz.models;

import com.dastsaz.dastsaz.utility.ClientConfigs;

/**
 * Sgin in Requst model used in sign in request
 */
public class SignInRequestModel {
    public String client_id;
    public String client_key;
    public String phone;
    public String password;

    public SignInRequestModel() {
        this.client_id = ClientConfigs.CLIENT_ID;
        this.client_key = ClientConfigs.CLIENT_KEY;
    }
}
