package com.example.cardiobook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class addMeasurement extends AppCompatActivity {
    Button saveButton;
    EditText systolicInput;
    EditText diastolicInput;
    EditText BPMInput;
    EditText commentInput;
    Measurement newMeasurement;
    String date;
    String time;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int positionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_measurement_screen);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        saveButton = findViewById(R.id.save_button);
        systolicInput = findViewById(R.id.edit_systolic);
        diastolicInput = findViewById(R.id.edit_diastolic);
        BPMInput = findViewById(R.id.edit_BPM);
        commentInput = findViewById(R.id.edit_comment);
        time = "11:30";
        mDisplayDate = (TextView) findViewById(R.id.date_tv);
        positionToEdit = -1;
        mDisplayTime = (TextView) findViewById(R.id.time_tv);


        Bundle incomingData = getIntent().getExtras();

        if (incomingData != null){

            String oldSystolic = incomingData.getString("SYSTOLIC");
            String oldDiastolic = incomingData.getString("DIASTOLIC");
            String oldBPM = incomingData.getString("BPM");
            String oldDate = incomingData.getString("DATE");
            String oldComment = incomingData.getString("COMMENT");
            String oldTime = incomingData.getString("TIME");
            positionToEdit = incomingData.getInt("EDITED");

            systolicInput.setText(oldSystolic);
            diastolicInput.setText(oldDiastolic);
            BPMInput.setText(oldBPM);
            mDisplayDate.setText(oldDate);
            commentInput.setText(oldComment);
            mDisplayTime.setText(oldTime);
        }

        //Add the data if there is incoming data

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        addMeasurement.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = convertDate(month) + "/" + convertDate(day) + "/" + convertDate(year);
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addMeasurement.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time = convertDate(selectedHour) + ":" + convertDate(selectedMinute);
                        mDisplayTime.setText(time);
                    }
                }, hour, minute, true);
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkedInputs()){
                    sendInputs();
                }
            }
        });
    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

    private boolean checkedInputs(){
        final String newSystolic = systolicInput.getText().toString();
        final String newDiastolic = diastolicInput.getText().toString();
        final String newBPM = BPMInput.getText().toString();
        final String newDate = mDisplayDate.getText().toString();
        final String newComment = commentInput.getText().toString();
        final String newTime = mDisplayTime.getText().toString();

        try {
            int sysInt = Integer.parseInt(newSystolic);
            int diaInt = Integer.parseInt(newDiastolic);
            int BPMInt = Integer.parseInt(newBPM);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Your inputs are invalid!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Integer.parseInt(newSystolic) < 0 || Integer.parseInt(newDiastolic) < 0 || Integer.parseInt(newBPM) < 0){
            Toast.makeText(getApplicationContext(),"Make sure your integers are positive!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newDate.equals("Click here to set a date")){
            Toast.makeText(getApplicationContext(),"Enter a date!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(newTime.equals("Click here to set a time")){
            Toast.makeText(getApplicationContext(),"Enter a time!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newComment.length() > 20){
            Toast.makeText(getApplicationContext(),"Your comment is too long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void sendInputs(){
        final String newSystolic = systolicInput.getText().toString();
        final String newDiastolic = diastolicInput.getText().toString();
        final String newBPM = BPMInput.getText().toString();
        final String newDate = mDisplayDate.getText().toString();
        final String newComment = commentInput.getText().toString();
        final String newTime = mDisplayTime.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("EDITED", positionToEdit);
        intent.putExtra("SYSTOLIC", newSystolic);
        intent.putExtra("DIASTOLIC", newDiastolic);
        intent.putExtra("BPM", newBPM);
        intent.putExtra("DATE", newDate);
        intent.putExtra("COMMENT", newComment);
        intent.putExtra("TIME", newTime);
        setResult(RESULT_OK, intent);
        finish();
    }
}