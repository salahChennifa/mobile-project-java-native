package com.example.projecttestconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListerClientTime extends AppCompatActivity {
    EditText date_time;
    String date_time_picker;
    final static String INFO_TIME = "TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister_client_time);
        date_time = findViewById(R.id.date_time_input);
        date_time.setInputType(InputType.TYPE_NULL);
        date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialogue(date_time);
            }
        });

    }

    public void searchClts(View view) {
        //Log.i("serach client ", "is clicked");
        /// TODO : here we should create a Activity for recylerView and cardView §§
        Log.i("INFO", date_time_picker);

        Bundle extras = new Bundle();
        extras.putString(INFO_TIME, date_time_picker);
        Intent intent = new Intent(this, ClientPrClients.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
    private void showDateTimeDialogue( EditText date_time_in){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        date_time_picker = simpleDateFormat.format(calendar.getTime()).toString();
                    }
                };
                new  TimePickerDialog(ListerClientTime.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new  DatePickerDialog(ListerClientTime.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}