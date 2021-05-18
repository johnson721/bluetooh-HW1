package com.example.B10830229_HomeWork1;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
public class DetailActivity extends AppCompatActivity {
    public static final String DEVICE_NAME = "";
    public static final String DEVICE_RSSI = "";
    public static final String DEVICE_ADDRESS = "";
    public static final String DEVICE_BYTE = "";
    public static final String THE_KEY = "";
    public ScanActivity selectedDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView NAME = findViewById(R.id.DEVICE_NAME);
        TextView RSSI = findViewById(R.id.DEVICE_RSSI);
        TextView ADDRESS = findViewById(R.id.DEVICE_ADDRESS);
        TextView BYTE = findViewById(R.id.DEVICE_BYTE);
        selectedDevice = (ScanActivity) getIntent().getSerializableExtra(THE_KEY);
        NAME.setText(selectedDevice.getDeviceName());
        RSSI.setText(selectedDevice.getRssi());
        ADDRESS.setText(selectedDevice.getAddress());
        BYTE.setText(selectedDevice.getDeviceByteInfo());
        Button Back = findViewById(R.id.Back);
        Back.setOnClickListener(v -> DetailActivity.this.finish());
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR){
                event.startTracking();
            }else {
                onBackPressed();
            }}
        return false;
    }
}
