package com.dastsaz.dastsaz.utility;

/**
 * contain client information such as BASE_URL || client information
 */
public class ClientConfigs {
    //TODO: should get network ip address http://192.168.1.3:8080/blog/public/ or http://test.ictplus.ir/
    // and replace inside REST_API_BASE_URL
    public static final String REST_API_BASE_URL ="http://192.168.1.200/DASTSAZ1/public/";
            //"http://test.ictplus.ir/";
    //TODO: create new Client with postman in http://localshot:/api/v1/client with body {"name":"android client app"} and set these values with client_id and client_key
    public static final String CLIENT_ID = "390e6431-033b-32e0-a674-ace20462ec0c";
    public static final String CLIENT_KEY = "11";
}