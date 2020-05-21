package com.dastsaz.dastsaz.models;

import com.dastsaz.dastsaz.utility.ClientConfigs;

/**
 * Sign up Request model used in sign up Request
 */
public class SignUpRequestModel {
    public String client_id;
    public String client_key;
    public String id;
    public String email;
    public String name;
    public String password;
    public int active;
    public String phone;

    public SignUpRequestModel() {
        this.client_id = ClientConfigs.CLIENT_ID;
        this.client_key = ClientConfigs.CLIENT_KEY;
    }
}
