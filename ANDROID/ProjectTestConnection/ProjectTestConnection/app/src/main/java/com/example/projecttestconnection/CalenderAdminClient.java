package com.example.projecttestconnection;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LongSparseArray;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.calendarpart.DayView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;




public class CalenderAdminClient extends AppCompatActivity {
    int id;
    List<String> list;
    ArrayAdapter<String> dataAdapter;
    private Calendar day;
    private Calendar day_2;
    private LongSparseArray<List<Event>> allEvents;
    private DateFormat dateFormat;
    SimpleDateFormat formatter;
    private DateFormat timeFormat;
    private Calendar editEventDate;
    private Calendar editEventStartTime;
    private Calendar editEventEndTime;
    private Event editEventDraft;
    private TextView SeanceIdTotal;

    private ViewGroup content;
    private TextView dateTextView;
    private ScrollView scrollView;
    private DayView dayView;
    private String strDate1;
    private String selected;
    Map<String, Monitor> listdrop;
    Map<String, String> dict_test;

    private void addSeance(final int id_monitor,final String start_date, final String duration, final long test_event ){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=addtseance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            Boolean error = obj.getBoolean("error");
                            String message = obj.getString("message");
                            if (!error){
                                Toast.makeText(getApplicationContext(), message,  Toast.LENGTH_LONG).show();
                            }
                            onEventsChange();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onEventsChange(test_event);
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.i("INFO", "paramter : id " + id + " id monitor " + id_monitor + " : start date  " + start_date + " : duration" + duration);
                params.put("clientID",Integer.toString(id));
                params.put("monitorID", Integer.toString(id_monitor));
                params.put("startDate", start_date);
                params.put("durationMinut", (duration));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading ...");
        progress.setCancelable(false);
        progress.show();
    }

    private void callService(final String strDate1, final String selectedDte){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listCltSeance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            JSONArray jsonArray = obj.getJSONArray("monitors");
                            long diffA = 0;
                            long diff = 0;
                            for (int i = 0; i< jsonArray.length(); i++){
                                Toast.makeText(getApplicationContext(), "you have a seance here", Toast.LENGTH_LONG).show();
                                JSONObject object = jsonArray.getJSONObject(i);
                                String st = object.getString("startDate");
                                int id_seanceJson = object.getInt("IDSeance");
                                int id_monitorJson = object.getInt("monitorID");
                                Log.i("SEance id " , Integer.toString(id_seanceJson));
                                String fullName = object.getString("userFName") +" " +object.getString("userLName");
                                String[] arrOfStr = st.split(" ", 2);
                                String datejson =arrOfStr[0], timejson=arrOfStr[1];
                                String[] arrOfStr1 = timejson.split(":", 3);
                                Log.i("--->DATE JSON ", datejson);
                                Log.i("---->Current Day", strDate1);

                                String hours =arrOfStr1[0], minutes=arrOfStr1[1];
                                String duration = object.getString("durationMinut");
                                String isDone = object.getString("isDone");
                                SimpleDateFormat  dtf = new SimpleDateFormat("yyy-mm-dd");
                                try {
                                    Date date2 = dtf.parse(strDate1);
                                    Date date1 = dtf.parse(datejson);
                                    diff = date1.getTime() - date2.getTime();
                                    Log.i("Days" ,  Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                                    diffA = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                    if (diffA > 0){
                                        diffA = findDifference(strDate1, datejson);
                                        if (diffA < 0){}
                                        //diffA--;
                                    }else {
                                        diffA = findDifference(strDate1, datejson);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Log.i("-->diff : ", diffA +"");
                                Log.i("-->Hours : ", timejson );
                                Log.i("Id_seance", id_seanceJson+"");
                                // seances_events.add();

                                day_2 = Calendar.getInstance();
                                day_2.add(Calendar.DAY_OF_YEAR, (int)  diffA);
                                day_2.set(Calendar.HOUR_OF_DAY, 0);
                                day_2.set(Calendar.MINUTE, 0);
                                day_2.set(Calendar.SECOND, 0);
                                day_2.set(Calendar.MILLISECOND, 0);
                                Log.i("isDone", isDone);
                                if (isDone.equals("1") && diffA < 0){
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark,id_seanceJson,id_monitorJson))));

                                }
                                else if (isDone.equals("0") && diffA < 0){
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_red_dark, id_seanceJson, id_monitorJson))));
                                }
                                else {
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_blue_light, id_seanceJson, id_monitorJson))));
                                }

                            }
                            onEventsChange();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            //id_current_monitor
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_current_client", Integer.toString(id));
                params.put("start_date_url", selectedDte);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading ...");
        progress.setCancelable(false);
        progress.show();

    }

    private void changeSeance(final int id_seance, final int id_monitor,final String start_date, final String duration, final long test_event){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=modtseance",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            String seance = obj.getString("seance");
                            Boolean error = obj.getBoolean("error");
                            String message = obj.getString("message");
                            if (!error){
                                Toast.makeText(getApplicationContext(), message,  Toast.LENGTH_LONG).show();
                            }
                            onEventsChange();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onEventsChange(test_event);
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seanceID",Integer.toString(id_seance));
                params.put("monitorID", Integer.toString(id_monitor));
                params.put("startDate", start_date);
                params.put("durationMinut", (duration));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading the seances...");
        progress.setCancelable(false);
        progress.show();

    }

    private long   findDifference(String start_date, String end_date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long difference_In_Days = 0;

        // Try Class
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);
            long difference_In_Time = d2.getTime() - d1.getTime();
            difference_In_Days = TimeUnit.MILLISECONDS.toDays(difference_In_Time) % 365;

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return difference_In_Days;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String monitors = extras.getString(DetailClt.LISTMONITOR);
        id = Integer.parseInt(extras.getString(DetailClt.ID));
        Log.i("Id Clinet  is " , id+"");
        list = new ArrayList<>();
        list.add("Select the monitor");
        listdrop = new HashMap<>();
        dict_test = new HashMap<>();
        JSONObject obj = null;
        try {
            obj = new JSONObject(monitors);
            JSONArray jsonArray = obj.getJSONArray("monitors");
            for (int i = 0; i< jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String idMonitor = object.getString("userID");
                String fullName = object.getString("userFName") + " " + object.getString("userLName");
                Log.i("Monitr" + i , "id : " + idMonitor + "fullName" + fullName);
                listdrop.put(fullName, new Monitor(idMonitor, fullName));
                dict_test.put(idMonitor, fullName);
                list.add(fullName);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        day = Calendar.getInstance();
        final Date date = day.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        strDate1 =  dateFormat1.format(date);
        Log.i("Date A : " , strDate1);
        day.add(Calendar.DAY_OF_MONTH,  0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        // Populate today's entry in the map with a list of example events
        allEvents = new LongSparseArray<>();
        //allEvents.put(day.getTimeInMillis(), new ArrayList<>(Arrays.asList(INITIAL_EVENTS)));
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.getDefault());
        Calendar test = dateFormat.getCalendar();
        Date month = test.getTime();
        Log.i("test", month.toString());
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String format = formatter.format(date);
        Log.i("TEST 2 ", format);
        String strDate = dateFormat.format(date);
        Log.i("Date : " , strDate);
        //Todo : call service §§§
        callService(strDate1, strDate1);
        setContentView(R.layout.activity_calender_admin_client);


        timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        content = findViewById(R.id.sample_content_adminmonitor);
        dateTextView = findViewById(R.id.sample_date__adminmonitor);
        scrollView = findViewById(R.id.sample_scroll_adminmonitor);
        dayView = findViewById(R.id.sample_day_adminmonitor);
        SeanceIdTotal = findViewById(R.id.id_seance_admin_adminclt);



        // Inflate a label view for each hour the day view will display
        Calendar hour = (Calendar) day.clone();
        List<View> hourLabelViews = new ArrayList<>();
        for (int i = dayView.getStartHour(); i <= dayView.getEndHour(); i++) {
            hour.set(Calendar.HOUR_OF_DAY, i);
            TextView hourLabelView = (TextView) getLayoutInflater().inflate(R.layout.hour_label_admin_clt, dayView, false);
            hourLabelView.setText(timeFormat.format(hour.getTime()));
            hourLabelViews.add(hourLabelView);
        }
        dayView.setHourLabelViews(hourLabelViews);
        onDayChange();
    }


    public void onPreviousClick(View v) {
        day.add(Calendar.DAY_OF_YEAR, -1);
        final Date date = day.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        selected =  dateFormat1.format(date);
        callService(strDate1, selected);
        onDayChange();
    }

    public void onNextClick(View v) {
        day.add(Calendar.DAY_OF_YEAR, 1);
        final Date date = day.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        final String selected =  dateFormat1.format(date);
        callService(strDate1, selected);
        onDayChange();
    }

    public void onAddEventClick(View v) {
        Log.i("Here", "on Add event is clicked");
        editEventDate = (Calendar) day.clone();

        editEventStartTime = (Calendar) day.clone();

        editEventEndTime = (Calendar) day.clone();
        editEventEndTime.add(Calendar.MINUTE, 30);

        showEditEventDialog(false, null, null, android.R.color.holo_red_dark, 0,0);

        onDayChange();

    }


    public void onScrollClick(View v) {
        showScrollTargetDialog();
    }

    private void onDayChange() {
        dateTextView.setText(formatter.format(day.getTime()));
        onEventsChange();
    }

    private void onEventsChange() {
        // The day view needs a list of event views and a corresponding list of event time ranges
        List<View> eventViews = null;
        List<DayView.EventTimeRange> eventTimeRanges = null;
        List<Event> events = allEvents.get(day.getTimeInMillis());

        if (events != null) {
            // Sort the events by start time so the layout happens in correct order
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o1.hour < o2.hour ? -1 : (o1.hour == o2.hour ? (o1.minute < o2.minute ? -1 : (o1.minute == o2.minute ? 0 : 1)) : 1);
                }
            });

            eventViews = new ArrayList<>();
            eventTimeRanges = new ArrayList<>();

            // Reclaim all of the existing event views so we can reuse them if needed, this process
            // can be useful if your day view is hosted in a recycler view for example
            List<View> recycled = dayView.removeEventViews();
            int remaining = recycled != null ? recycled.size() : 0;

            for (final Event event : events) {
                // Try to recycle an existing event view if there are enough left, otherwise inflate
                // a new one
                View eventView = remaining > 0 ? recycled.get(--remaining) : getLayoutInflater().inflate(R.layout.event_admin_clt, dayView, false);

                ((TextView) eventView.findViewById(R.id.event_title)).setText(event.title);
                ((TextView) eventView.findViewById(R.id.event_location)).setText(event.location);
                SeanceIdTotal = findViewById(R.id.id_seance_admin_adminclt);
                eventView.setBackgroundColor(getResources().getColor(event.color));
                Log.i("Id sance inside ", "Event change " + event.id_sance);
                Log.i("Id monitor inside ", "Event change " + event.id_monitor);

                // When an event is clicked, start a new draft event and show the edit event dialog
                eventView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editEventDraft = event;
                        editEventDate = (Calendar) day.clone();
                        editEventStartTime = Calendar.getInstance();
                        editEventStartTime.set(Calendar.HOUR_OF_DAY, editEventDraft.hour);
                        editEventStartTime.set(Calendar.MINUTE, editEventDraft.minute);
                        editEventStartTime.set(Calendar.SECOND, 0);
                        editEventStartTime.set(Calendar.MILLISECOND, 0);
                        editEventEndTime = (Calendar) editEventStartTime.clone();
                        editEventEndTime.add(Calendar.MINUTE, editEventDraft.duration);

                        Log.i("Id Seance :  : " , " "+ editEventDraft.id_sance);
                        showEditEventDialog(true, editEventDraft.title, editEventDraft.location, editEventDraft.color,  editEventDraft.id_sance, editEventDraft.id_monitor);
                    }
                });
                eventViews.add(eventView);
                // The day view needs the event time ranges in the start minute/end minute format,
                // so calculate those here
                int startMinute = 60 * event.hour + event.minute;
                int endMinute = startMinute + event.duration;
                eventTimeRanges.add(new DayView.EventTimeRange(startMinute, endMinute));
            }
        }
        // Update the day view with the new events
        dayView.setEventViews(eventViews, eventTimeRanges);
    }

    private void onEventsChange(long event_change) {
        // The day view needs a list of event views and a corresponding list of event time ranges
        Log.i("INFO", "On Evnet change");
        List<View> eventViews = null;
        List<DayView.EventTimeRange> eventTimeRanges = null;
        List<Event> events = allEvents.get(event_change);

        if (events != null) {
            // Sort the events by start time so the layout happens in correct order
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return o1.hour < o2.hour ? -1 : (o1.hour == o2.hour ? (o1.minute < o2.minute ? -1 : (o1.minute == o2.minute ? 0 : 1)) : 1);
                }
            });

            eventViews = new ArrayList<>();
            eventTimeRanges = new ArrayList<>();

            // Reclaim all of the existing event views so we can reuse them if needed, this process
            // can be useful if your day view is hosted in a recycler view for example
            List<View> recycled = dayView.removeEventViews();
            int remaining = recycled != null ? recycled.size() : 0;

            for (final Event event : events) {
                // Try to recycle an existing event view if there are enough left, otherwise inflate
                // a new one
                View eventView = remaining > 0 ? recycled.get(--remaining) : getLayoutInflater().inflate(R.layout.event_admin_clt, dayView, false);

                ((TextView) eventView.findViewById(R.id.event_title)).setText(event.title);
                ((TextView) eventView.findViewById(R.id.event_location)).setText(event.location);
                //SeanceIdTotal = findViewById(R.id.id_seance_admin);
                eventView.setBackgroundColor(getResources().getColor(event.color));
                Log.i("Id sance inside ", "Event change " + event.id_sance);
                Log.i("Id monitor inside ", "Event change " + event.id_monitor);

                // When an event is clicked, start a new draft event and show the edit event dialog
                eventView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editEventDraft = event;
                        editEventDate = (Calendar) day.clone();
                        editEventStartTime = Calendar.getInstance();
                        editEventStartTime.set(Calendar.HOUR_OF_DAY, editEventDraft.hour);
                        editEventStartTime.set(Calendar.MINUTE, editEventDraft.minute);
                        editEventStartTime.set(Calendar.SECOND, 0);
                        editEventStartTime.set(Calendar.MILLISECOND, 0);
                        editEventEndTime = (Calendar) editEventStartTime.clone();
                        editEventEndTime.add(Calendar.MINUTE, editEventDraft.duration);
                        Log.i("Id Seance :  : " , " "+ editEventDraft.id_sance);
                        showEditEventDialog(true, editEventDraft.title, editEventDraft.location, editEventDraft.color,  editEventDraft.id_sance, editEventDraft.id_monitor);
                    }
                });
                eventViews.add(eventView);
                // The day view needs the event time ranges in the start minute/end minute format,
                // so calculate those here
                int startMinute = 60 * event.hour + event.minute;
                int endMinute = startMinute + event.duration;
                eventTimeRanges.add(new DayView.EventTimeRange(startMinute, endMinute));
            }
        }
        // Update the day view with the new events
        dayView.setEventViews(eventViews, eventTimeRanges);
    }


    private void showEditEventDialog(boolean eventExists, @Nullable String eventTitle, @Nullable String eventLocation, @ColorRes int eventColor, final int id_seance, final int id_monitor ) {
        Log.i("Here", "on select !!");

        Log.i("Id_input_monitor", id_monitor+"");

        final View view = getLayoutInflater().inflate(R.layout.edit_event_dialog_admin_clt, content, false);
        final TextView titleTextView = view.findViewById(R.id.edit_event_title_adminclt);
        final TextView locationTextView = view.findViewById(R.id.edit_event_location_adminclt);
        final Button dateButton = view.findViewById(R.id.edit_event_date_adminclt);
        final Button startTimeButton = view.findViewById(R.id.edit_event_start_time_adminclt);
        final Button endTimeButton = view.findViewById(R.id.edit_event_end_time_adminclt);
        final TextView SeanceId = view.findViewById(R.id.id_seance_admin_adminclt);
       
        final Spinner spinner2 =  view.findViewById(R.id.spinner2_adminclt);

        titleTextView.setText(eventTitle);
        locationTextView.setText(eventLocation);
        SeanceId.setText(Integer.toString(id_seance));
        Log.i("SeanceId ---->", id_seance +"");
        Log.i("MonitreId ---->", id_monitor+"");



        dateButton.setText(dateFormat.format(editEventDate.getTime()));
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editEventDate.set(Calendar.YEAR, year);
                        editEventDate.set(Calendar.MONTH, month);
                        editEventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        dateButton.setText(dateFormat.format(editEventDate.getTime()));
                    }
                };

                new DatePickerDialog(CalenderAdminClient.this, listener, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        startTimeButton.setText(timeFormat.format(editEventStartTime.getTime()));
        final long test_date = editEventDate.getTimeInMillis();
        Log.i(" first Start date : ", dateFormat.format(editEventDate.getTime()) + timeFormat.format(editEventStartTime.getTime()));

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editEventStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        editEventStartTime.set(Calendar.MINUTE, minute);

                        startTimeButton.setText(timeFormat.format(editEventStartTime.getTime()));

                        if (!editEventEndTime.after(editEventStartTime)) {
                            editEventEndTime = (Calendar) editEventStartTime.clone();
                            editEventEndTime.add(Calendar.MINUTE, 30);

                            endTimeButton.setText(timeFormat.format(editEventEndTime.getTime()));
                        }
                    }
                };

                new TimePickerDialog(CalenderAdminClient.this, listener, editEventStartTime.get(Calendar.HOUR_OF_DAY), editEventStartTime.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(CalenderAdminClient.this)).show();

            }
        });

        endTimeButton.setText(timeFormat.format(editEventEndTime.getTime()));
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editEventEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        editEventEndTime.set(Calendar.MINUTE, minute);

                        if (!editEventEndTime.after(editEventStartTime)) {
                            editEventEndTime = (Calendar) editEventStartTime.clone();
                            editEventEndTime.add(Calendar.MINUTE, 30);
                        }

                        endTimeButton.setText(timeFormat.format(editEventEndTime.getTime()));
                    }
                };

                new TimePickerDialog(CalenderAdminClient.this, listener, editEventEndTime.get(Calendar.HOUR_OF_DAY), editEventEndTime.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(CalenderAdminClient.this)).show();

            }
        });

        DateFormat dateFormat1First = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2First = new SimpleDateFormat("HH:mm:ss");
        Log.i("time on this event is ", "++" + dateFormat2First.format(editEventStartTime.getTime()));
        final String firstDateSelected = dateFormat1First.format(editEventDate.getTime()) + " " +dateFormat2First.format(editEventStartTime.getTime());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle(eventExists ? "Modify seance" : "Add sance");
        dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        final String fullName =  dict_test.get(Integer.toString(id_monitor));
        int indxi = list.indexOf(fullName);
        spinner2.setSelection(indxi);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Event> events = allEvents.get(editEventDate.getTimeInMillis());
                List<Event> events_test = allEvents.get((long) test_date);
                Log.i("Fisrt ", test_date + "");
                Log.i("SEcode", editEventDate.getTimeInMillis()+"");
                if (events == null) {
                    events = new ArrayList<>();
                    allEvents.put(editEventDate.getTimeInMillis(), events);
                }
                if (events_test == null) {
                    events_test = new ArrayList<>();
                    allEvents.put((long) test_date, events_test);
                }
                // Todo here the time
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                String secondedateStart = dateFormat1.format(editEventDate.getTime())+" "+dateFormat2.format(editEventStartTime.getTime());
                Log.i("Seconde Date", secondedateStart);
                Log.i("First Date", firstDateSelected);
                Log.i ("id seance : " , id_seance+"");
                String title = titleTextView.getText().toString();
                String location = locationTextView.getText().toString();
                int hour = editEventStartTime.get(Calendar.HOUR_OF_DAY);
                int minute = editEventStartTime.get(Calendar.MINUTE);
                int duration = (int) (editEventEndTime.getTimeInMillis() - editEventStartTime.getTimeInMillis()) / 60000;
                Log.i("Duration", duration +"");
                String nameSelected = spinner2.getSelectedItem().toString();
                final Monitor monitor1 = listdrop.get(nameSelected);
                Map<Boolean, Integer> test_1 = showIdSance(events, id_seance);
                Map<Boolean, Integer> test_2 = showIdSance(events_test, id_seance);
                Log.i("boolean test", showIdSance(events, id_seance) +"");
                Log.i("!!boolean test", showIdSance(events_test, id_seance) +"");
                Log.i("id      -- sance ", id_seance+"");
                Log.i("id      -- Monitor  ", id_monitor+"");
                Log.i("TEST_Diff",  findDifference(firstDateSelected, secondedateStart)+"");
                if (findDifference(firstDateSelected, secondedateStart) < 0 ){
                    Log.i("++Attention : ", id_seance+"");
                    Log.i("+++ID Moiotre", id_monitor+"");
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("ATTENTION");
                    alertDialog.setMessage("Please You can't move the seance to previous date");
                    alertDialog.setIcon(R.drawable.warning);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                else if (findDifference(firstDateSelected, secondedateStart) == 0 && id_seance !=0){

                    Log.i("+++ID Moiotre", id_monitor+"");
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("INFORMATION");
                    alertDialog.setMessage("you can move the seance to the next hours but pleas contact the client");
                    alertDialog.setIcon(R.drawable.information);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                else if (findDifference(firstDateSelected, secondedateStart) > 0 && id_seance != 0){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String nowDate =  formatter.format(date);
                    Log.i("Now time ", nowDate);
                    if (findDifference(nowDate, secondedateStart) > 0){
                        Event tmpEvent =  events_test.get(test_2.get(true));
                        tmpEvent.title = title;
                        tmpEvent.location = location;
                        tmpEvent.hour = hour;
                        tmpEvent.minute  = minute;
                        tmpEvent.duration = duration;
                        tmpEvent.id_sance = id_seance;
                        tmpEvent.id_monitor = Integer.parseInt(monitor1.getId());
                        events_test.set(test_2.get(true), tmpEvent);
                        events_test.add(new Event(title, location, hour, minute, duration, android.R.color.holo_blue_dark, id_seance,Integer.parseInt(monitor1.id)));
                        Log.i("id Seance : " ,  id_seance+"");
                        Log.i("id monitor : " ,  id_monitor+"");
                        Log.i("start date : " ,  secondedateStart+"");
                        Log.i("duration : " ,  duration+"");
                        if (tmpEvent.color == android.R.color.holo_red_dark){
                            Log.i("INFO ", "Change on " + id_seance);
                            Log.i("!!DATE a TEST", editEventDate.getTime().toString());
                            //allEvents.put(editEventDate.getTimeInMillis(), events_test);
                            changeSeance(id_seance,id_monitor,secondedateStart, Integer.toString(duration),  editEventDate.getTimeInMillis());
                        }
                        else {
                            Log.i("+++ID Moiotre", id_monitor+"");
                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                            alertDialog.setTitle("INFORMATION");
                            alertDialog.setMessage("the client is not absent in this seance");
                            alertDialog.setIcon(R.drawable.information);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("ATTENTION");
                        alertDialog.setMessage("Please You can't move the seance to previous date");
                        alertDialog.setIcon(R.drawable.warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();
                    }
                }

                /// Todo more work to  change
                if (id_seance == 0 || id_monitor ==0){
                    Log.i("INFO ", "here a new seance");
                    // changeSeance(id_seance,id_monitor,secondedateStart, Integer.toString(duration),  editEventDate.getTimeInMillis());

                    Log.i("Id seance ", id_seance+"");

                    // String fullName =  dict_test.get(Integer.toString(id_monitor));
                    //int indxi = list.indexOf(fullName);
                    //spinner2.setSelection(indxi);
                    String fullname_test = spinner2.getSelectedItem().toString();
                    Log.i("Name Monitor", fullname_test);

                    Monitor monitor =   listdrop.get(fullname_test);
                    Log.i("Id Client", id+"");
                    Log.i("Id monitor ", monitor.getId()+"");
                    Log.i("start date ", secondedateStart+"");
                    Log.i("duration ", Integer.toString(duration)+"");
                    Log.i("Date to add", editEventDate.getTime().toString());
                    //TODO :: 1 --- !! work on the web service to add a client

                    //TODO :: 2 --- !!! work on the deffrent cas to hindle the errors of the date etc :
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String nowDate =  formatter.format(date);
                    Log.i("Now time ", nowDate);

                    if (findDifference(nowDate, secondedateStart) > 0){
                        events_test.add(new Event(title, location, hour, minute, duration, android.R.color.holo_blue_dark, Integer.parseInt("0"),Integer.parseInt(monitor.getId())));
                        addSeance(Integer.parseInt(monitor.getId()), secondedateStart,Integer.toString(duration), editEventDate.getTimeInMillis());
                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("ATTENTION");
                        alertDialog.setMessage("Please You can't move the seance to previous date");
                        alertDialog.setIcon(R.drawable.warning);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onEditEventDismiss(false);
            }
        });

        if (eventExists) {
            builder.setNeutralButton(R.string.edit_event_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onEditEventDismiss(true);
                }
            });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onEditEventDismiss(false);
            }
        });
        builder.setView(view);
        builder.show();
    }

    private Map<Boolean, Integer> showIdSance(List<Event> events, int id_seance) {

        Map<Boolean, Integer> tmp = new HashMap<>();
        for (int i =0; i< events.size(); i++){

            int id_S = events.get(i).id_sance;
            if (id_S == id_seance){
                tmp.put(true, i);
                return tmp;
            }
        }
        tmp.put(false, -1);
        return tmp;
    }


    private void showScrollTargetDialog() {
        View view = getLayoutInflater().inflate(R.layout.scroll_target_dialog_admin_clt, content, false);
        final Button timeButton = view.findViewById(R.id.scroll_target_time_adminclt);
        final Button firstEventTopButton = view.findViewById(R.id.scroll_target_first_event_top__adminclt);
        final Button firstEventBottomButton = view.findViewById(R.id.scroll_target_first_event_bottom__adminclt);
        final Button lastEventTopButton = view.findViewById(R.id.scroll_target_last_event_top__adminclt);
        final Button lastEventBottomButton = view.findViewById(R.id.scroll_target_last_event_bottom__adminclt);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.scroll_to);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setView(view);

        final AlertDialog dialog = builder.show();

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int top = dayView.getHourTop(hourOfDay);
                        int bottom = dayView.getHourBottom(hourOfDay);
                        int y = top + (bottom - top) * minute / 60;
                        scrollView.smoothScrollTo(0, y);
                        dialog.dismiss();
                    }
                };

                new TimePickerDialog(CalenderAdminClient.this, listener, 0, 0, android.text.format.DateFormat.is24HourFormat(CalenderAdminClient.this)).show();

            }
        });

        firstEventTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, dayView.getFirstEventTop());
                dialog.dismiss();
            }
        });

        firstEventBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, dayView.getFirstEventBottom());
                dialog.dismiss();
            }
        });

        lastEventTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, dayView.getLastEventTop());
                dialog.dismiss();
            }
        });

        lastEventBottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.smoothScrollTo(0, dayView.getLastEventBottom());
                dialog.dismiss();

            }
        });
    }

    private void onEditEventDismiss(boolean modified) {
        if (modified && editEventDraft != null) {
            List<Event> events = allEvents.get(day.getTimeInMillis());
            if (events != null) {
                events.remove(editEventDraft);
            }
        }
        editEventDraft = null;

        onEventsChange();
    }

    private static class Event {
        private int id_sance;
        private int id_monitor;
        @Nullable
        private  String title;
        @Nullable
        private  String location;
        private  int hour;
        private  int minute;
        private  int duration;

        @ColorRes
        private final int color;

        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;

        }

        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color, int idSeance, int id_monitor) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;
            this.id_sance = idSeance;
            this.id_monitor = id_monitor;

        }


    }
}