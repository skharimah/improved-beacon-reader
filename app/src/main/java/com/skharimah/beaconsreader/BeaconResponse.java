package com.skharimah.beaconsreader;

/**
 * Created by Khuram on 5/10/2016.
 */
/**
 * Created by Khuram on 5/10/2016.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BeaconResponse {

    @SerializedName("URL Extensions")
    public List<String> URLExtensions = new ArrayList<String>();
    @SerializedName("Major")
    public int Major;
    @SerializedName("UUID")
    public String UUID;
    @SerializedName("Minor")
    public int Minor;

    public BeaconResponse (List<String> urlext, String uuid, int major, int minor){
        //this.UserId = userId;
        this.URLExtensions = urlext;
        this.UUID = uuid;
        this.Major = major;
        this.Minor = minor;
    }
}