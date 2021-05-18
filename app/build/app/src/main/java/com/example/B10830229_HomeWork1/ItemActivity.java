package com.example.B10830229_HomeWork1;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
class ItemActivity extends RecyclerView.Adapter<ItemActivity.ViewHolder> {
private OnItemClick onItemClick;
private List<ScanActivity> arrayList = new ArrayList<>();
final Activity activity;
public ItemActivity(Activity activity) {
        this.activity = activity;
        }
public void OnItemClick(OnItemClick onItemClick){
        this.onItemClick = onItemClick;
        }
public void clear(){
        this.arrayList.clear();
        notifyDataSetChanged();
        }
public void addDevice(List<ScanActivity> arrayList){
        this.arrayList = arrayList;
        notifyDataSetChanged();
        }
public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView deviceName,deviceAddress,deviceRssi;
    Button deviceDetail;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        deviceName = itemView.findViewById(R.id.DeviceName);
        deviceAddress = itemView.findViewById(R.id.DeviceAddress);
        deviceRssi = itemView.findViewById(R.id.DeviceRssi);
        deviceDetail = itemView.findViewById(R.id.DeviceDetail);
    }
}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false);
        return new ViewHolder(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.deviceName.setText(arrayList.get(position).getDeviceName());
        holder.deviceAddress.setText("Address："+arrayList.get(position).getAddress());
        holder.deviceRssi.setText("Rssi："+arrayList.get(position).getRssi());
        holder.deviceDetail.setOnClickListener(v -> onItemClick.onItemClick(arrayList.get(position)));
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    interface OnItemClick{
    void onItemClick(ScanActivity selectedDevice);
}
}