package com.skharimah.beaconsreader;

/**
 * Created by Khuram on 5/10/2016.
 */


import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class BeaconInfo {
    @SerializedName("UserId") String UserId;
    @SerializedName("UUID")  String UUID;
    @SerializedName("Major") int Major;
    @SerializedName("Minor") int Minor;

    public BeaconInfo (String userId, String uuid, int major, int minor){
        this.UserId = userId;
        this.UUID = uuid;
        this.Major = major;
        this.Minor = minor;
    }
    public void onFailure(Throwable t) {
        Log.d("Error", t.getMessage());
    }
}


