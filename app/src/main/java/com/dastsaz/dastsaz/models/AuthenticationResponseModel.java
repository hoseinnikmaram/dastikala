package com.dastsaz.dastsaz.models;

/**
 * Authentication response Model used as response model in sign in and sign up request
 */
public class AuthenticationResponseModel {
    public TokenModel token;
    public UserModel user_profile;
    public String type;
    public String description;
}
