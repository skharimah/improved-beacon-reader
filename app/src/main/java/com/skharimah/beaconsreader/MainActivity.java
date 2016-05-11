package com.skharimah.beaconsreader;

import android.bluetooth.le.BluetoothLeScanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 1;
    // Get an instance of device's BT adapter
    private BluetoothAdapter mBluetoothAdapter;
    // Get user email address
    public EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void onClickLogin(View view) {
        if(mBluetoothAdapter == null) {
            // Device doesn't support BT
            Toast.makeText(this, "Bluetooth not supported!",
                    Toast.LENGTH_SHORT).show();
        } else if(!mBluetoothAdapter.isEnabled()) {
            // BT adapter is not enabled
            // Request BT to be turned on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            username = (EditText)findViewById(R.id.user_email);
            // BT exists and is enabled
            Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
            // Pass the user email information to next activity
            String userEmailAddress = username.getText().toString();
            intent.putExtra(BluetoothActivity.EXTRA_MESSAGE, userEmailAddress);
            startActivity(intent);
        }
    }
}
