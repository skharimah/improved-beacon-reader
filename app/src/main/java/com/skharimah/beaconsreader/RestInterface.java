package com.skharimah.beaconsreader;

/**
 * Created by Khuram on 5/9/2016.
 */
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.Call;

public interface RestInterface {
    @POST("beacon")
    Call<BeaconResponse> sendBeaconInfo(@Body String beaconInfo);

    @POST("organizations/{urlext}")
    Call<BeaconInfoArray> sendOrganization(@Path("urlext") String urlext,@Body String beaconInfo);
}