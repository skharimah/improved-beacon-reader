package com.skharimah.beaconsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ApiCommunication extends AppCompatActivity {

    private GoogleApiClient client;
    String newurl;
    String serverresponse;
    String toprint2;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://x692mukomi.execute-api.us-east-1.amazonaws.com/test/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RestInterface rest = retrofit.create(RestInterface.class);
    BeaconInfo example = new BeaconInfo("user1@test.com","4152554efaab4a3b86d0947070693a77", 1, 1);


    // Get user information from login page
    protected Intent intent;
    private String userEmailAddress;
    private String beaconUUID, beaconMajor, beaconMinor;
    public static final String USER_EMAIL = "user_email";
    public static final String IBEACON_UUID = "beacon_uuid";
    public static final String IBEACON_MAJOR = "beacon_major";
    public static final String IBEACON_MINOR = "beacon_minor";
    // ListView purposes
    String[] infoArray = {"user email", "uuid", "major", "minor" };
    private ListView listView;
    private ArrayAdapter mArrayAdapter;
    /* Debugging purposes */
    public static final String TAG = ApiCommunication.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //firstcall();
        setContentView(R.layout.activity_api_communication);
        // Get user & beacon information from previous activity
        intent = getIntent();
        userEmailAddress = intent.getStringExtra(USER_EMAIL);
        beaconUUID = intent.getStringExtra(IBEACON_UUID);
        beaconMajor = intent.getStringExtra(IBEACON_MAJOR);
        beaconMinor = intent.getStringExtra(IBEACON_MINOR);
        //firstcall();
        // Replace dashes in the UUID to a single string
        System.out.println("THis is to print after the function runs: " + newurl);
        beaconUUID = beaconUUID.replaceAll("-", "");

        BeaconInfo newexample = new BeaconInfo("user1@test.com",beaconUUID, Integer.parseInt(beaconMajor), Integer.parseInt(beaconMinor));
        //BeaconInfo newexample = new BeaconInfo("user1@test.com","4152554efaab4a3b86d0947070693a77", 1, 1);
        example = newexample;
        Gson gson = new Gson();
        toprint2 = gson.toJson(example);
        firstcall();
        // UI
        infoArray[0] = userEmailAddress;
        infoArray[1] = "uuid: " + beaconUUID;
        infoArray[2] = "major: " + beaconMajor;
        infoArray[3] = "minor:" + beaconMinor;
        listView = (ListView) findViewById(R.id.user_beacon_info);
        System.out.println(infoArray[1]);
        System.out.println(infoArray[2]);
        System.out.println(infoArray[3]);
        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, infoArray);
        listView.setAdapter(mArrayAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //firstcall();
    }

    @Override
    public void onBackPressed() {
        // Enable user-prompted scanning
        Intent intent = new Intent(ApiCommunication.this, BluetoothActivity.class);
        intent.putExtra(BluetoothActivity.EXTRA_MESSAGE, userEmailAddress);
        startActivity(intent);
    }

    void firstcall (){
        Call<BeaconResponse> call = rest.sendBeaconInfo(toprint2);
        call.enqueue(new Callback<BeaconResponse>() {
            @Override
            public void onResponse(Response<BeaconResponse> response, Retrofit retrofit) {
                //String toprint = response.toString();
                Gson newgson = new Gson();
                final String toprint3 = newgson.toJson(response.body());
                System.out.println(toprint2);
                System.out.println("Response From Server");
                System.out.println(toprint3);
                System.out.println("JSON Object Retrevied from the Server " + response.body().URLExtensions);
                System.out.println("Response Code " + response.code());
                System.out.println("Error Body if it exists " + response.errorBody());
                System.out.println("Operation Successful  " + response.isSuccess());
                System.out.println(response.message());

                newurl = response.body().URLExtensions.remove(response.body().URLExtensions.size() - 1);


                secondcall();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    void secondcall (){
        System.out.println("Time For Next Post");
        System.out.println(newurl);
        Call<BeaconInfoArray> newcall = rest.sendOrganization(newurl,toprint2);
        newcall.enqueue(new Callback<BeaconInfoArray>() {
            @Override
            public void onResponse(Response<BeaconInfoArray> response, Retrofit retrofit) {
                Gson newgson = new Gson();
                final String toprint3 = newgson.toJson(response.body());

                System.out.println("Response From Server");
                System.out.println(toprint3);
                System.out.println("Response Code " + response.raw());
                System.out.println("Error Body if it exists " + response.errorBody());
                System.out.println("Operation Successful  " + response.isSuccess());
                System.out.println(response.message());

                setContentView(R.layout.activity_api_communication);
                TextView textView = (TextView) findViewById(R.id.my_edit);
                textView.setText(response.body().response.toString());

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
