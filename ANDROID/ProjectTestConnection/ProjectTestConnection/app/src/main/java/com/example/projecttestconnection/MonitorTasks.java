package com.example.projecttestconnection;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.LongSparseArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RadioButton;
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

public class MonitorTasks extends AppCompatActivity {
    int id;
    private Calendar day;
    private Calendar day_2;
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

    private void callServiceMonitor(final String date_time){
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
                params.put("startDate", date_time);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        progress.setTitle("Loading");
        progress.setMessage("Wait while loading the tasks...");
        progress.setCancelable(false); 
        progress.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Log.i("Date : " , strDate1);
        //callservice(strDate1, strDate1);
        callServiceMonitor(strDate1);
        setContentView(R.layout.activity_monitor_tasks);

        
        User user = SharedPrefManagerSl.getInstance(this).getUser();
        id = user.getId();
        timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

        setContentView(R.layout.activity_monitor_tasks);

        content = findViewById(R.id.sample_content_monitor_task);
        dateTextView = findViewById(R.id.sample_date_monitor_task);
        scrollView = findViewById(R.id.sample_scroll_monitor_task);
        dayView = findViewById(R.id.sample_day_monitor_task);
        // Inflate a label view for each hour the day view will display
        Calendar hour = (Calendar) day.clone();
        List<View> hourLabelViews = new ArrayList<>();
        for (int i = dayView.getStartHour(); i <= dayView.getEndHour(); i++) {
            hour.set(Calendar.HOUR_OF_DAY, i);

            TextView hourLabelView = (TextView) getLayoutInflater().inflate(R.layout.hour_label__monitor_task, dayView, false);
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
                View eventView = remaining > 0 ? recycled.get(--remaining) : getLayoutInflater().inflate(R.layout.event_monitor_task, dayView, false);

                ((TextView) eventView.findViewById(R.id.event_title_monitor_task)).setText(event.title);
                ((TextView) eventView.findViewById(R.id.event_location_monitor_task)).setText(event.location);
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
        View view = getLayoutInflater().inflate(R.layout.edit_event_dialog_monitor_task, content, false);
        final TextView titleTextView = view.findViewById(R.id.edit_event_title);
        final TextView locationTextView = view.findViewById(R.id.edit_event_location);
        final TextView ClientId = view.findViewById(R.id.id_clt);
        final TextView SeanceId = view.findViewById(R.id.id_seance);
        final RadioButton redRadioButton = view.findViewById(R.id.edit_event_red);
        final RadioButton blueRadioButton = view.findViewById(R.id.edit_event_blue);
        final RadioButton greenRadioButton = view.findViewById(R.id.edit_event_green);


        titleTextView.setText(eventTitle);

        locationTextView.setText(eventLocation);
        if (Integer.toString((id_task)).equals(null)){
            Log.i("Attention " , "is null");
        }else {
            ClientId.setText(Integer.toString(id_task));
        }
        Log.i("ClinetId ", ClientId.getText() +"");
        Log.i("SeanceId ", SeanceId.getText() +"");
        final String idClt = ClientId.getText().toString();
        if (eventColor == android.R.color.holo_blue_dark) {
            blueRadioButton.setChecked(true);
        } /*else if (eventColor == android.R.color.holo_orange_dark) {
            orangeRadioButton.setChecked(true);
        } */else if (eventColor == android.R.color.holo_green_dark) {
            //Log.i("---->Présent", "is Clicked");
            greenRadioButton.setChecked(true);
        } /*else if (eventColor == android.R.color.holo_purple) {
            purpleRadioButton.setChecked(true);
        } */else if (eventColor == android.R.color.holo_red_dark){

            redRadioButton.setChecked(true);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // If the event already exists, we are editing it, otherwise we are adding a new event
        builder.setTitle(eventExists ? R.string.make_event : R.string.add_event);

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


                events.add(new Event(title, location, hour, minute, duration, android.R.color.holo_blue_dark,Integer.parseInt(idClt)));

                onEditEventDismiss(true);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onEditEventDismiss(false);
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
        View view = getLayoutInflater().inflate(R.layout.scroll_target_dialog_monitor_task, content, false);
        final Button timeButton = view.findViewById(R.id.scroll_target_time_monitor_task);
        final Button firstEventTopButton = view.findViewById(R.id.scroll_target_first_event_top_monitor_task);
        final Button firstEventBottomButton = view.findViewById(R.id.scroll_target_first_event_bottom_monitor_task);
        final Button lastEventTopButton = view.findViewById(R.id.scroll_target_last_event_top_monitor_task);
        final Button lastEventBottomButton = view.findViewById(R.id.scroll_target_last_event_bottom_monitor_task);

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

                new TimePickerDialog(MonitorTasks.this, listener, 0, 0, android.text.format.DateFormat.is24HourFormat(MonitorTasks.this)).show();

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
        
        private int idtask;
      
        @Nullable
        private final String title;
        @Nullable
        private final String location;
        private final int hour;
        private final int minute;
        private final int duration;

        @ColorRes
        private final int color;

       
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