package com.example.B10830229_HomeWork1;
import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothLeScanner mBluetoothLeScanner = null;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    private static final int REQUEST_ENABLE_BT = 2;
    public boolean Scanning = false;
    ArrayList<ScanActivity> findDevice = new ArrayList<>();
    ItemActivity Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        bluetoothScan();
        Button Enter = findViewById(R.id.Enter);
        Enter.setOnClickListener(btnListener1);
        Adapter.OnItemClick(itemClick);
    }
    private final View.OnClickListener btnListener1 = v -> change();
    public void change(){
           Button btn1 = findViewById(R.id.Enter);
           Button btn2 = findViewById(R.id.Scan);
           findViewById(R.id.Welcome).setVisibility(View.GONE);
           findViewById(R.id.UpBar).setVisibility(View.VISIBLE);
           findViewById(R.id.DownBar).setVisibility(View.VISIBLE);
           findViewById(R.id.DeviceView).setVisibility(View.VISIBLE);
           btn2.setVisibility(View.VISIBLE);
           btn1.setVisibility(View.GONE);
        }
    private final static String[] permissionsWeNeed = new String[]{
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int PERMISSION_REQUEST_CODE = 500;
    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int OK = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (OK != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_PERMISSION);
                requestPermissions(permissionsWeNeed,PERMISSION_REQUEST_CODE);
            }
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                Toast.makeText(this,"Not support Bluetooth", Toast.LENGTH_SHORT).show();
                finish();
            }
            if(!mBluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            }
        }else finish();
    }
    @SuppressLint("SetTextI18n")
    private void bluetoothScan() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothManager != null){
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null){
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                Toast.makeText(this,"Bluetooth function start",Toast.LENGTH_SHORT).show();
            }
        }
        RecyclerView recyclerView = findViewById(R.id.DeviceView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter = new ItemActivity(this);
        recyclerView.setAdapter(Adapter);
        final Button btScan = findViewById(R.id.Scan);
        btScan.setText("Start");
        btScan.setOnClickListener((v)-> {
            if (Scanning) {
                Scanning = false;
                btScan.setText("Start");
                mBluetoothLeScanner.stopScan(StartScanCallback);
            }else{
                Scanning = true;
                btScan.setText("Stop");
                findDevice.clear();
                mBluetoothLeScanner.startScan(StartScanCallback);
                Adapter.clear();
            }
        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        final Button btScan = findViewById(R.id.Scan);
        Scanning = false;
        btScan.setText("Start");
        mBluetoothLeScanner.stopScan(StartScanCallback);
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStop() {
        super.onStop();
        final Button btScan = findViewById(R.id.Scan);
        Scanning = true;
        btScan.setText("Stop");
        mBluetoothLeScanner.startScan(StartScanCallback);
    }
    private final ScanCallback StartScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            ScanRecord scanRecord = result.getScanRecord();
            String address = device.getAddress();
            byte[] content = scanRecord.getBytes();
            new Thread(()->{
                if (address!= null){
                    findDevice.add(new ScanActivity(device.getName()
                            , String.valueOf(result.getRssi())
                            , byteArrayToHexStr(content)
                            , device.getAddress()));
                    ArrayList newList = getSingle(findDevice);
                    runOnUiThread(()-> Adapter.addDevice(newList));
                }
            }).start();
        }
    };
    private ArrayList<Object> getSingle(ArrayList<ScanActivity> list) {
        ArrayList<Object> tempList = new ArrayList<>();
        try {
            for (ScanActivity obj : list) {
                if (!tempList.contains(obj)) {
                    tempList.add(obj);
                } else {
                    tempList.set(getIndex(tempList, obj), obj);
                }
            }
            return tempList;
        } catch (ConcurrentModificationException e) {
            return tempList;
        }
    }
    private int getIndex(ArrayList<Object> temp, ScanActivity obj) {
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).toString().contains(obj.toString())) {
                return i;
            }
        }
        return -1;
    }
    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        StringBuilder hex = new StringBuilder(byteArray.length * 2);
        for (byte aData : byteArray) {
            hex.append(String.format("%02X", aData));
        }
        return hex.toString();
    }
    private final ItemActivity.OnItemClick itemClick = selectedDevice -> {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.DEVICE_NAME,selectedDevice.getDeviceName());
        intent.putExtra(DetailActivity.DEVICE_RSSI,selectedDevice.getRssi());
        intent.putExtra(DetailActivity.DEVICE_ADDRESS,selectedDevice.getAddress());
        intent.putExtra(DetailActivity.DEVICE_BYTE,selectedDevice.getDeviceByteInfo());
        intent.putExtra(DetailActivity.THE_KEY,selectedDevice);
        startActivity(intent);
    };
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