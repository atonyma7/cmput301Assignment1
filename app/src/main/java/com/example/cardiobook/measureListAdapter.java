package com.example.cardiobook;

import android.content.Context;

import android.icu.util.Measure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class measureListAdapter extends ArrayAdapter<Measurement> {

    private Context mContext;
    private List<Measurement> dataList = new ArrayList<>();

    public measureListAdapter(@NonNull Context context, ArrayList<Measurement> list) {
        super(context, 0 , list);
        mContext = context;
        dataList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.measurement, parent,false);

        Measurement currentMeasurement = dataList.get(position);

        TextView tvDate = (TextView) listItem.findViewById(R.id.textView);
        TextView tvSystolic = (TextView) listItem.findViewById(R.id.textView2);
        TextView tvDiastolic = (TextView) listItem.findViewById(R.id.textView3);
        TextView tvBPM = (TextView) listItem.findViewById(R.id.textView4);

        int systolicInt = Integer.parseInt(currentMeasurement.getSysPressure());
        int diastolicInt = Integer.parseInt(currentMeasurement.getDiaPressure());
        String dateText = "Date: " + currentMeasurement.getDate();
        String systolicText = "Systolic Pressure: " + currentMeasurement.getSysPressure();
        String diastolicText = "Diastolic Pressure: " + currentMeasurement.getDiaPressure();
        String BPMText = "Heart BPM: " + currentMeasurement.getBPM();

        if (systolicInt < 90 || systolicInt > 140){
            tvSystolic.setBackgroundResource(R.color.Red);
        }
        else{
            tvSystolic.setBackgroundResource(R.color.White);
        }

        if (diastolicInt < 60 || diastolicInt > 90){
            tvDiastolic.setBackgroundResource(R.color.Red);
        }
        else{
            tvDiastolic.setBackgroundResource(R.color.White);
        }


        tvDate.setText(dateText);
        tvSystolic.setText(systolicText);
        tvDiastolic.setText(diastolicText);
        tvBPM.setText(BPMText);
        return listItem;
    }
}
