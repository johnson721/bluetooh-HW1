package com.example.B10830229_HomeWork1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;
class ScanActivity implements Serializable {
    private final String NAME,RSSI,BYTE,ADDRESS;
    public ScanActivity(String NAME, String RSSI, String BYTE, String ADDRESS) {
        this.NAME = NAME;
        this.RSSI = RSSI;
        this.BYTE = BYTE;
        this.ADDRESS = ADDRESS;
    }
    public String getAddress() {
        return ADDRESS;
    }
    public String getRssi() {
        return RSSI;
    }
    public String getDeviceByteInfo() {
        return BYTE;
    }
    public String getDeviceName() {
        return NAME;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        ScanActivity p = (ScanActivity)obj;
        return this.ADDRESS.equals(Objects.requireNonNull(p).ADDRESS);
    }
    @NonNull
    @Override
    public String toString() {
        return this.ADDRESS;
    }
}