package com.example.cardiobook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.time.LocalDate;


public class MainActivity extends AppCompatActivity {
    private  measureListAdapter mAdapter;
    public SharedPreferences shared;
    private measurementList dataList = new measurementList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shared = getSharedPreferences("App_settings", MODE_PRIVATE);

        ListView measurementListView = findViewById(R.id.measure_list);
        Button addButton = findViewById(R.id.add_button);

        LocalDate myObj = LocalDate.now(); // Create a date object
        System.out.println(myObj);

        loadData();

        mAdapter = new measureListAdapter(this, dataList.getMeasurementList());

        measurementListView.setAdapter(mAdapter);

        measurementListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           final int position, long arg3) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Measurement?")
                        .setMessage("Are you sure you want to delete this measurement?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {


                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                mAdapter.remove(dataList.getMeasurementList().get(position));
                                saveData();
                                mAdapter.notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
                return true;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addMeasurement.class);
                startActivityForResult(intent, 1);
            }
        });

        measurementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editMeasurement(position);
            }
        });


        //listen for incoming data


    }

    private void editMeasurement(int position){
        Intent i = new Intent (getApplicationContext(), addMeasurement.class);
        Measurement clickedItem = dataList.getMeasurementList().get(position);
        i.putExtra("EDITED", position);
        i.putExtra("DATE", clickedItem.getDate());
        i.putExtra("SYSTOLIC", clickedItem.getSysPressure());
        i.putExtra("DIASTOLIC", clickedItem.getDiaPressure());
        i.putExtra("BPM", clickedItem.getBPM());
        i.putExtra("COMMENT", clickedItem.getComment());
        i.putExtra("TIME", clickedItem.getTime());
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ok", "ok");
        super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Bundle incomingData = data.getExtras();

                String newSystolic = data.getStringExtra("SYSTOLIC");
                String newDiastolic = data.getStringExtra("DIASTOLIC");
                String newBPM = data.getStringExtra("BPM");
                String newDate = data.getStringExtra("DATE");
                String newComment = data.getStringExtra("COMMENT");
                String newTime = data.getStringExtra("TIME");
                int editedPosition = incomingData.getInt("EDITED");

                final Measurement toAdd = new Measurement(newDate, newTime, newSystolic, newDiastolic, newBPM, newComment);

                if (editedPosition > -1){
                    dataList.getMeasurementList().remove(editedPosition);
                    dataList.getMeasurementList().add(editedPosition, toAdd);
                    mAdapter.notifyDataSetChanged();

                }
                else {
                    dataList.getMeasurementList().add(toAdd);
                    mAdapter.notifyDataSetChanged();
                }
                saveData();
            }
        }
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataList.getMeasurementList());
        editor.putString("task list", json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Measurement>>() {}.getType();
        ArrayList<Measurement> temp = gson.fromJson(json, type);
        dataList.setMeasurementList(temp);
        if (dataList.getMeasurementList() == null){
            dataList = new measurementList();
        }
    }

}