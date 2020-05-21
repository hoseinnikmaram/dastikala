package com.dastsaz.dastsaz.models;

import java.util.Date;

/**
 * Tweet Model to send new tweet for request body and get in in response
 * NOTE: all of the attr should define as public and also the name should match in REST API
 */
public class PosterModel {
    public String id_poster;
    public String title;
    public String description;
    public String phone;
    public int sms;
    public int id_city;
    public int id_location;
    public int id_group;
    public int id_sub;
    public int active;
    public String date;
    public String id_user;
    public String src_pic;
    public String id_nazarat;
    public int price;
    public String cityname;
    public String groupname;
    public String subname;
    public String location_name;
    // public UserModel user;
}
