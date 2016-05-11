package com.skharimah.beaconsreader;

/**
 * Created by Khuram on 5/9/2016.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeaconInfoArray {

    @SerializedName("response")
    @Expose
    public List<String> response = new ArrayList<String>();

    public BeaconInfoArray (List<String> response1){
        this.response = response1;
    }

}