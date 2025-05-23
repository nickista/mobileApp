package com.example.calendarapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private ListView eventListView;
    private Button addEventButton;
    private ArrayAdapter<String> eventAdapter;
    private List<String> eventList;
    private Map<String, List<Event>> eventsMap;
    private String selectedDate;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "CalendarPrefs";
    private static final String EVENTS_KEY = "events";
    private static final String CHANNEL_ID = "calendar_notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        createNotificationChannel();
        loadEvents();
        setupCalendar();
        setupEventList();
        setupAddEventButton();
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        eventListView = findViewById(R.id.eventListView);
        addEventButton = findViewById(R.id.addEventButton);

        eventList = new ArrayList<>();
        eventsMap = new HashMap<>();
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // 오늘 날짜를 기본 선택 날짜로 설정
        Calendar today = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(today.getTime());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "캘린더 알림";
            String description = "캘린더 일정 알림";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                updateEventList();
            }
        });
    }

    private void setupEventList() {
        eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventList);
        eventListView.setAdapter(eventAdapter);

        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteEventDialog(position);
                return true;
            }
        });

        updateEventList();
    }

    private void setupAddEventButton() {
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEventDialog();
            }
        });
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);

        EditText titleEditText = dialogView.findViewById(R.id.titleEditText);
        EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        Button selectTimeButton = dialogView.findViewById(R.id.selectTimeButton);

        final Calendar selectedDateTime = Calendar.getInstance();

        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(selectedDateTime, selectTimeButton);
            }
        });

        builder.setView(dialogView)
                .setTitle("새 일정 추가")
                .setPositiveButton("추가", (dialog, which) -> {
                    String title = titleEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();

                    if (!title.isEmpty()) {
                        Event event = new Event(title, description, selectedDateTime.getTimeInMillis());
                        addEvent(event);
                        scheduleNotification(event);
                        Toast.makeText(MainActivity.this, "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void showTimePickerDialog(Calendar dateTime, Button timeButton) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    dateTime.set(Calendar.MINUTE, minute);

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    timeButton.setText("시간: " + timeFormat.format(dateTime.getTime()));
                },
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }

    private void addEvent(Event event) {
        if (!eventsMap.containsKey(selectedDate)) {
            eventsMap.put(selectedDate, new ArrayList<>());
        }
        eventsMap.get(selectedDate).add(event);
        saveEvents();
        updateEventList();
    }

    private void updateEventList() {
        eventList.clear();
        if (eventsMap.containsKey(selectedDate)) {
            List<Event> dayEvents = eventsMap.get(selectedDate);
            for (Event event : dayEvents) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String eventString = timeFormat.format(new Date(event.getDateTime())) + " - " + event.getTitle();
                if (!event.getDescription().isEmpty()) {
                    eventString += "\n  " + event.getDescription();
                }
                eventList.add(eventString);
            }
        }
        eventAdapter.notifyDataSetChanged();
    }

    private void showDeleteEventDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("일정 삭제")
                .setMessage("이 일정을 삭제하시겠습니까?")
                .setPositiveButton("삭제", (dialog, which) -> {
                    if (eventsMap.containsKey(selectedDate)) {
                        eventsMap.get(selectedDate).remove(position);
                        if (eventsMap.get(selectedDate).isEmpty()) {
                            eventsMap.remove(selectedDate);
                        }
                        saveEvents();
                        updateEventList();
                        Toast.makeText(MainActivity.this, "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void scheduleNotification(Event event) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("title", event.getTitle());
        intent.putExtra("description", event.getDescription());

        int requestCode = (event.getTitle() + event.getDateTime()).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long notificationTime = event.getDateTime() - (10 * 60 * 1000);

        if (notificationTime > System.currentTimeMillis()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                    } else {
                        // 사용자에게 권한 설정 요청
                        Intent intentSettings = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startActivity(intentSettings);
                        Toast.makeText(this, "정확한 알람 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                    }
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "알람 권한이 거부되었습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void saveEvents() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(eventsMap);
        editor.putString(EVENTS_KEY, json);
        editor.apply();
    }

    private void loadEvents() {
        String json = sharedPreferences.getString(EVENTS_KEY, "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Event>>>(){}.getType();
            eventsMap = gson.fromJson(json, type);
            if (eventsMap == null) {
                eventsMap = new HashMap<>();
            }
        }
    }
}