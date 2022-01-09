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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
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

public class CalendarAdminMonitor extends AppCompatActivity {


    private Calendar day;
    private Calendar day_2;
    private int id ;

    String strDate;
    private String strDate1;
    private String selected;

    private LongSparseArray<List<Event>> allEvents;
    private SimpleDateFormat formatter;
    private DateFormat dateFormat;
    private DateFormat timeFormat;
    private Calendar editEventDate;
    private Calendar editEventStartTime;
    private Calendar editEventEndTime;
    private Event editEventDraft;
    private ViewGroup content;
    private TextView dateTextView;
    private ScrollView scrollView;
    private DayView dayView;
    private long   findDifference(String start_date, String end_date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long difference_In_Days = 0;
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

    private void callservice(final String strDate1, final String selectedDte){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listGrpCl",
                new Response.Listener<String>() {
                    ArrayList<Event> seances = new ArrayList<>();
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            JSONArray jsonArray = obj.getJSONArray("clients");
                            long diffA = 0;
                            Toast.makeText(getApplicationContext(), "you have a seance here", Toast.LENGTH_LONG).show();
                            Map<Long, ArrayList<Event>> dict =  new HashMap<Long, ArrayList<Event>>();

                            for (int i = 0; i< jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String st = object.getString("startDate");
                                int id_clt = object.getInt("clientID");
                                int id_seanceJson = object.getInt("seanceID");
                                Log.i("SEance id " , Integer.toString(id_seanceJson));
                                String fullName = object.getString("fName") +" " +object.getString("lName");
                                Log.i("Full Name", fullName);
                                String[] arrOfStr = st.split(" ", 2);
                                String datejson =arrOfStr[0], timejson=arrOfStr[1];
                                String[] arrOfStr1 = timejson.split(":", 3);
                                String hours =arrOfStr1[0], minutes=arrOfStr1[1];
                                String duration = object.getString("durationMinut");
                                String isDone = object.getString("isDone");
                                Log.i("IsDone", isDone);
                                //SimpleDateFormat  dtf = new SimpleDateFormat("yyy-mm-dd");
                                diffA = findDifference(strDate1, datejson);
                                day_2 = Calendar.getInstance();
                                day_2.add(Calendar.DAY_OF_MONTH, (int)  diffA);
                                day_2.set(Calendar.HOUR_OF_DAY, 0);
                                day_2.set(Calendar.MINUTE, 0);
                                day_2.set(Calendar.SECOND, 0);
                                day_2.set(Calendar.MILLISECOND, 0);
                                Log.i("DAtE ", datejson + "");
                                if (isDone.equals("1") && diffA < 0){
                                    //allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark, id_clt, id_seanceJson))));
                                    //dict.put(day_2.getTimeInMillis(), seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark, id_clt, id_seanceJson)));
                                    if (dict.containsKey(day_2.getTimeInMillis())){
                                        seances = dict.get(day_2.getTimeInMillis());
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark, id_clt, id_seanceJson));
                                    }else {
                                        seances = new ArrayList<>();
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark, id_clt, id_seanceJson));
                                        dict.put(day_2.getTimeInMillis(), seances);
                                    }

                                }
                                else if (isDone.equals("0") && diffA < 0){
                                    //allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_red_dark, id_clt,id_seanceJson))));

                                    if (dict.containsKey(day_2.getTimeInMillis())){
                                        seances = dict.get(day_2.getTimeInMillis());
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_red_dark, id_clt,id_seanceJson));
                                    }else {
                                        seances = new ArrayList<>();
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_red_dark, id_clt,id_seanceJson));
                                        dict.put(day_2.getTimeInMillis(), seances);
                                    }
                                }
                                else {
                                    //allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_blue_light, id_clt,id_seanceJson))));

                                    if (dict.containsKey(day_2.getTimeInMillis())){
                                        seances = dict.get(day_2.getTimeInMillis());
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_blue_light, id_clt,id_seanceJson));
                                    }else {
                                        seances = new ArrayList<>();
                                        seances.add(new Event(fullName, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_blue_light, id_clt,id_seanceJson));
                                        dict.put(day_2.getTimeInMillis(), seances);
                                    }
                                }

                                Log.i("Time ", timejson + "");

                                Log.i("diff ", diffA+"");
                            }
                            for (Map.Entry<Long, ArrayList<Event>> set :
                                    dict.entrySet()) {
                                allEvents.put(set.getKey(), set.getValue());
                            }

                            onEventsChange();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("id_current_monitor", Integer.toString(id));
                params.put("startDate", selectedDte);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void addTask( final int duraiton, final String title, final String startdate, final long test_event){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=addtTask",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("rspone", response);
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
                params.put("user_Fk",Integer.toString(id));
                params.put("durationMinut", Integer.toString(duraiton));
                params.put("title", title);
                params.put("startDate", (startdate));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading ...");
        progress.setCancelable(false);
        progress.show();
    }

    private void callServiceMonitor(final String date_time){
        //sets the maximum value 100
        //displays the progress bar
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=listTasksMonitor",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);

                        try {
                            //converting response to json object
                            Log.i("ResponseTest", response);
                            JSONObject obj = new JSONObject(response);

                            JSONArray jsonArray = obj.getJSONArray("Tasks");
                            long diffA = 0;
                            long diff = 0;
                            for (int i = 0; i< jsonArray.length(); i++){
                                Toast.makeText(getApplicationContext(), "there are a Task To Do", Toast.LENGTH_LONG).show();
                                JSONObject object = jsonArray.getJSONObject(i);
                                //Log.i("id", object.getString("clientID"));
                                //Client(int clientID, String clientEmail, String passwd, String clientPhone, String fName, String lName)
                                //tacheList.add(new Tache(object.getString("titre"), object.getString("time")));
                                String titre = object.getString("titre");
                                String time = object.getString("time");
                                String[] arrOfStr = time.split(" ", 2);
                                String datejson =arrOfStr[0], timejson=arrOfStr[1];
                                int id_task = object.getInt("taskID");
                                //durationMinut
                                String duration = object.getString("durationMinut");
                                String[] arrOfStr1 = timejson.split(":", 3);
                                Log.i("--->DATE JSON ", datejson);
                                Log.i("---->Current Day", strDate1);

                                String hours =arrOfStr1[0], minutes=arrOfStr1[1];
                                SimpleDateFormat  dtf = new SimpleDateFormat("yyy-mm-dd");

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
                                day_2 = Calendar.getInstance();
                                day_2.add(Calendar.DAY_OF_YEAR, (int)  diffA);
                                day_2.set(Calendar.HOUR_OF_DAY, 0);
                                day_2.set(Calendar.MINUTE, 0);
                                day_2.set(Calendar.SECOND, 0);
                                day_2.set(Calendar.MILLISECOND, 0);

                                if ( diffA < 0){
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(titre, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_green_dark,id_task))));

                                }
                                else if ( diffA < 0){
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(titre, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_red_dark, id_task))));
                                }
                                else {
                                    allEvents.put(day_2.getTimeInMillis(), new ArrayList<>(Arrays.asList(new Event(titre, "Club", Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(duration), android.R.color.holo_blue_light, id_task))));
                                }

                            }
                            onEventsChange();


                        } catch (JSONException | ParseException e) {
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
                params.put("id_current_monitor", Integer.toString(id));
                /// TODO  : here we want to pass the date form the prevois intent using putExtrat
                params.put("startDate", date_time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog


    }
    //changeSeance(id,id_monitor,secondedateStart, Integer.toString(duration));

    private void changeTAsk(final int id_task, final String title, final String startdate, final int duration, final long test_event){
        final ProgressDialog progress = new ProgressDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://favoured-checks.000webhostapp.com/Api.php?apicall=modtTask",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //progressBar.setVisibility(View.GONE);
                        String titleJson = "";

                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseTest", response);
                            String seance = obj.getString("seance");
                            JSONObject obj_1 = new JSONObject(seance);
                            titleJson = obj_1.getString("title");
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
            //id_current_monitor
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("taskID", Integer.toString(id_task));
                params.put("user_Fk", Integer.toString(id));
                params.put("durationMinut", Integer.toString(duration));
                params.put("title", title);
                /// TODO  : here we want to pass the date form the prevois intent using putExtrat
                params.put("startDate", startdate);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
// To dismiss the dialog
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = Integer.parseInt(extras.getString(ListerMonitor.ID));
        Log.i("ATTENTION ", id+"");
        day = Calendar.getInstance();
        final Date date = day.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        strDate1 =  dateFormat1.format(date);
        Log.i("Date A : " , strDate1);
        day.add(Calendar.DAY_OF_MONTH,  0);
        /// TODO : here the work on the date
        SimpleDateFormat  dtf = new SimpleDateFormat("yyy-mm-dd");
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        allEvents = new LongSparseArray<>();
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        Calendar test = dateFormat.getCalendar();
        Date month = test.getTime();
        Log.i("test", month.toString());
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String format = formatter.format(date);
        Log.i("TEST 2 ", format);
        strDate = dateFormat.format(date);
        Log.i("Date : " , strDate1);
        //callservice(strDate1, strDate1);
        callServiceMonitor(strDate1);


        //DateFormat dateFormat1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_calendar_admin_monitor);


        content = findViewById(R.id.sample_content_admin_monitor);
        dateTextView = findViewById(R.id.sample_date_admin_monitor);
        scrollView = findViewById(R.id.sample_scroll__admin_monitor);
        dayView = findViewById(R.id.sample_day_admin_monitor);
        // Inflate a label view for each hour the day view will display
        Calendar hour = (Calendar) day.clone();
        List<View> hourLabelViews = new ArrayList<>();
        for (int i = dayView.getStartHour(); i <= dayView.getEndHour(); i++) {
            hour.set(Calendar.HOUR_OF_DAY, i);

            TextView hourLabelView = (TextView) getLayoutInflater().inflate(R.layout.hour_label_admin_monitor, dayView, false);
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
        Log.i("Selected pre", selected);
        //callservice(strDate1, selected);
        callServiceMonitor(selected);
        onDayChange();
    }

    public void onNextClick(View v) {
        day.add(Calendar.DAY_OF_YEAR, 1);
        final Date date = day.getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        selected =  dateFormat1.format(date);
        Log.i("Precedent clicked ", selected);
        //callservice(strDate1, selected);
        callServiceMonitor(selected);
        onDayChange();
    }

    public void onAddEventClick(View v) {
        editEventDate = (Calendar) day.clone();

        editEventStartTime = (Calendar) day.clone();

        editEventEndTime = (Calendar) day.clone();
        editEventEndTime.add(Calendar.MINUTE, 30);

        showEditEventDialog(false, null, null, android.R.color.holo_red_dark,0);
    }

    public void onScrollClick(View v) {
        showScrollTargetDialog();
    }

    private void onDayChange() {
        dateTextView.setText(formatter.format(day.getTime()));
        onEventsChange();
    }

    private void onEventsChange(long event_change/*, String title_test*/) {
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
                View eventView = remaining > 0 ? recycled.get(--remaining) : getLayoutInflater().inflate(R.layout.event_admin_monitor, dayView, false);

                ((TextView) eventView.findViewById(R.id.event_title_admin_monitor)).setText(event.title);
                ((TextView) eventView.findViewById(R.id.event_location_admin_monitor)).setText(event.location);
                eventView.setBackgroundColor(getResources().getColor(event.color));

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
                        Log.i("Id Seance :  : " , " "+ editEventDraft.idtask);
                        showEditEventDialog(true, editEventDraft.title, editEventDraft.location, editEventDraft.color,  editEventDraft.idtask);
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
                View eventView = remaining > 0 ? recycled.get(--remaining) : getLayoutInflater().inflate(R.layout.event_admin_monitor, dayView, false);

                ((TextView) eventView.findViewById(R.id.event_title_admin_monitor)).setText(event.title);
                ((TextView) eventView.findViewById(R.id.event_location_admin_monitor)).setText(event.location);
                eventView.setBackgroundColor(getResources().getColor(event.color));

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


                        showEditEventDialog(true, editEventDraft.title, editEventDraft.location, editEventDraft.color, editEventDraft.idtask);
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

    private void showEditEventDialog(boolean eventExists, @Nullable String eventTitle, @Nullable String eventLocation, @ColorRes int eventColor, final int id_task) {
        final View view = getLayoutInflater().inflate(R.layout.edit_event_dialog_admin_monitor, content, false);
        final TextView titleTextView = view.findViewById(R.id.edit_event_title_admin_monitor);
        final TextView locationTextView = view.findViewById(R.id.edit_event_location_admin_monitor);
        final TextView ClientId = view.findViewById(R.id.id_clt_admin_monitor);
        final Button startTimeButton = view.findViewById(R.id.edit_event_start_time_admin_monitor);
        final Button endTimeButton = view.findViewById(R.id.edit_event_end_time_admin_monitor);
        final Button dateButton = view.findViewById(R.id.edit_event_date_admin_monitor);




        titleTextView.setText(eventTitle);

        locationTextView.setText(eventLocation);
        if (Integer.toString((id_task)).equals(null)){
            Log.i("Attention " , "is null");
        }else {
            //ClientId.setText(Integer.toString(id_task));

        }
        //Log.i("ClinetId ", ClientId.getText() +"");
        //final String idClt = ClientId.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

                new DatePickerDialog(CalendarAdminMonitor.this, listener, day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH)).show();

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

                new TimePickerDialog(CalendarAdminMonitor.this, listener, editEventStartTime.get(Calendar.HOUR_OF_DAY), editEventStartTime.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(CalendarAdminMonitor.this)).show();

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

                new TimePickerDialog(CalendarAdminMonitor.this, listener, editEventEndTime.get(Calendar.HOUR_OF_DAY), editEventEndTime.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(CalendarAdminMonitor.this)).show();

            }
        });

        DateFormat dateFormat1First = new SimpleDateFormat("yyyy-MM-dd");
        // currentDateSelecte = dateFormat1First.format(editEventDate.getTime());
        DateFormat dateFormat2First = new SimpleDateFormat("HH:mm:ss");
        Log.i("time on this event is ", "++" + dateFormat2First.format(editEventStartTime.getTime()));
        final String firstDateSelected = dateFormat1First.format(editEventDate.getTime()) + " " +dateFormat2First.format(editEventStartTime.getTime());
        Log.i("Date test", firstDateSelected);


        // If the event already exists, we are editing it, otherwise we are adding a new event
        builder.setTitle(eventExists ? "Edit Task" : "Add Task");

        // When the event changes are confirmed, read the new values from the dialog and then add
        // this event to the list
        /// TODO : here we want to store the value of isDone and set to the DB
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Ok", "is clicked");
                List<Event> events = allEvents.get(editEventDate.getTimeInMillis());
                if (events == null) {
                    events = new ArrayList<>();
                    allEvents.put(editEventDate.getTimeInMillis(), events);
                }

                String title = titleTextView.getText().toString();
                String location = locationTextView.getText().toString();
                int hour = editEventStartTime.get(Calendar.HOUR_OF_DAY);
                int minute = editEventStartTime.get(Calendar.MINUTE);
                int duration = (int) (editEventEndTime.getTimeInMillis() - editEventStartTime.getTimeInMillis()) / 60000;
                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
                String secondedateStart = dateFormat1.format(editEventDate.getTime())+" "+dateFormat2.format(editEventStartTime.getTime());
                Log.i("Seconde Date", secondedateStart);

                //Log.i("id Task", idClt);
                Log.i("duration", duration + "");
                Log.i("date ", secondedateStart);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String nowDate =  formatter.format(date);
                Log.i("Now time ", nowDate);
                if (findDifference(firstDateSelected, secondedateStart) < 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("ATTENTION");
                    alertDialog.setMessage("Please You can't move the task to previous date");
                    alertDialog.setIcon(R.drawable.warning);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                else if (findDifference(firstDateSelected, secondedateStart) == 0 && id_task !=0){


                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("INFORMATION");
                    alertDialog.setMessage("you can move the task to the next hours but pleas contact the client");
                    alertDialog.setIcon(R.drawable.information);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                } else if (findDifference(firstDateSelected, secondedateStart) > 0 && id_task != 0){
                    if (findDifference(nowDate, secondedateStart) > 0){

                        Log.i("Good", "good");

                        Log.i("id task : " ,  id_task+"");
                        Log.i("title : " ,  title+"");
                        Log.i("start date : " ,  secondedateStart+"");
                        Log.i("duration : " ,  duration+"");
                        //events.add(new Event(title, location, hour, minute, duration, android.R.color.holo_blue_dark,id_task));
                        changeTAsk(id_task, title, secondedateStart,duration, editEventDate.getTimeInMillis());



                    }
                    else {
                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                        alertDialog.setTitle("ATTENTION");
                        alertDialog.setMessage("Please You can't move the task to previous date");
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
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();
                    alertDialog.setTitle("ATTENTION");
                    alertDialog.setMessage("Please You can't move the task to previous date");
                    alertDialog.setIcon(R.drawable.warning);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                if (id_task == 0){
                    if (findDifference(nowDate, secondedateStart) > 0){
                        // addTask(final int user_fk, final int duraiton, final String title, final String startdate, final long test_event)
                        //events.add(new Event(title, location, hour, minute, duration, android.R.color.holo_blue_dark, Integer.parseInt("0")));
                        addTask(duration,title, secondedateStart,editEventDate.getTimeInMillis());
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
                 onEditEventDismiss(true);
            }
        });



        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onEditEventDismiss(false);
            }
        });
        builder.setView(view);
        builder.show();
    }

    private void showScrollTargetDialog() {
        View view = getLayoutInflater().inflate(R.layout.scroll_target_dialog_admin_monitor, content, false);
        final Button timeButton = view.findViewById(R.id.scroll_target_time_admin_monitor);
        final Button firstEventTopButton = view.findViewById(R.id.scroll_target_first_event_top_admin_monitor);
        final Button firstEventBottomButton = view.findViewById(R.id.scroll_target_first_event_bottom_admin_monitor);
        final Button lastEventTopButton = view.findViewById(R.id.scroll_target_last_event_top_admin_monitor);
        final Button lastEventBottomButton = view.findViewById(R.id.scroll_target_last_event_bottom_admin_monitor);

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

                new TimePickerDialog(CalendarAdminMonitor.this, listener, 0, 0, android.text.format.DateFormat.is24HourFormat(CalendarAdminMonitor.this)).show();

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
        private int idClt;
        private int idSeance;
        private int idtask;
        private long day;
        @Nullable
        private final String title;
        @Nullable
        private final String location;
        private final int hour;
        private final int minute;
        private final int duration;

        @ColorRes
        private final int color;

        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;
            //this.date = date;
        }
        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color, int idClt, int idSeance, long day) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;
            this.idClt = idClt;
            this.idSeance = idSeance;
            this.day = day;
        }


        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color, int idClt, int idSeance) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;
            this.idClt = idClt;
            this.idSeance = idSeance;
        }
        private Event(@Nullable String title, @Nullable String location, int hour, int minute, int duration, @ColorRes int color, int idtask) {
            this.title = title;
            this.location = location;
            this.hour = hour;
            this.minute = minute;
            this.duration = duration;
            this.color = color;
            this.idtask = idtask;
        }
    }
}