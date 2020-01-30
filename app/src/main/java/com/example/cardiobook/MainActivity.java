package com.example.cardiobook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.time.LocalDate; // import the LocalDate class
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    ListView measurementListView;
    Button addButton;
    Button deleteButton;
    protected  measureListAdapter mAdapter;
    ArrayList<Measurement> dataList;

    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shared = getSharedPreferences("App_settings", MODE_PRIVATE);

        measurementListView = findViewById(R.id.measure_list);
        addButton = findViewById(R.id.add_button);

        LocalDate myObj = LocalDate.now(); // Create a date object
        System.out.println(myObj);

        loadData();

        mAdapter = new measureListAdapter(this, dataList);

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
                                mAdapter.remove(dataList.get(position));
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

    public void editMeasurement(int position){
        Intent i = new Intent (getApplicationContext(), addMeasurement.class);
        Measurement clickedItem = dataList.get(position);
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
                    dataList.remove(editedPosition);
                    dataList.add(editedPosition, toAdd);
                    mAdapter.notifyDataSetChanged();

                }
                else {
                    dataList.add(toAdd);
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
        String json = gson.toJson(dataList);
        editor.putString("task list", json);
        editor.apply();
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Measurement>>() {}.getType();

        dataList = gson.fromJson(json, type);
        if (dataList == null){
            dataList = new ArrayList<>();
            Measurement [] measurements = {};
            dataList.addAll(Arrays.asList(measurements));
        }
    }

}