package com.naren008.taskmanager;

import static com.google.android.material.timepicker.TimeFormat.CLOCK_12H;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naren008.taskmanager.databinding.ActivityAlarmSetterBinding;
import com.naren008.taskmanager.databinding.ActivityMainBinding;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class alarmSetter extends AppCompatActivity {

    private ActivityAlarmSetterBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    String id;
    Boolean ins = false;
    DatabaseReference alarmToken, user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setter);
        binding = ActivityAlarmSetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

        id = getIntent().getStringExtra("id");
        String uid = id;
        Log.d("IDENTIFIER", id+"");
        alarmToken = FirebaseDatabase.getInstance().getReference().child("Alarm");
//        user = FirebaseDatabase.getInstance().getReference("User");

        binding.SelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        binding.SetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
                Intent in = new Intent(alarmSetter.this, Profile.class);
//                if(ins){
//                    in.putExtra("dialog", "true");
//                }else{
//                    in.putExtra("dialog", "false");
//                }
                startActivity(in);
            }
        });

        binding.CancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });

    }

    private void cancelAlarm() {

        Intent intent = new Intent(this, broadcastReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {

        if (id.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            DatabaseReference dusr = FirebaseDatabase.getInstance().getReference("Alarm");
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(alarmSetter.this, broadcastReceiver.class);
            intent.putExtra("id", id);
            pendingIntent = PendingIntent.getBroadcast(alarmSetter.this, 0, intent, 0);
            Log.d("CALENDER", calendar.getTimeInMillis()+"");
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d("ALARM",calendar.toString()+"");

            dusr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String task = snapshot.child(id).child("task").toString();
                    Log.d("TASK", task+"");
                    intent.putExtra("task", task);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            ins = true;

        }
        Toast.makeText(alarmSetter.this, "Alarm Set Successfully ", Toast.LENGTH_SHORT).show();





    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        picker.show(getSupportFragmentManager(), "alarm-notifier");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (picker.getHour() > 12) {
                    binding.selectedTime.setText(picker.getHour() - 12 + " : " + picker.getMinute() + " PM");
                } else {
                    binding.selectedTime.setText(picker.getHour() + " : " + picker.getMinute() + " AM");
                }
                int hour = picker.getHour();
                int minute = picker.getMinute();

                String s = binding.TaskMessage.getText().toString();
                UserAlarm userAlarm = new UserAlarm(hour, minute, s);
                alarmToken.child(id).setValue(userAlarm);
                binding.TaskMessage.setText("");


//                DatabaseReference duser = FirebaseDatabase.getInstance().getReference("Alarm");
//                duser.keepSynced(true);
//                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//                duser.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String ids = snapshot.child(id).getKey();
//                        Log.d("ID   : ", ids+"");
//                        String hour = snapshot.child(id).child("hour").toString();
//                        Log.d("HOUR :  ", hour+"");
//                        String minute = snapshot.child(id).child("minute").toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }

        });
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderChannel";
            String description = "Channel for Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("alarm-notifier", name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    public interface FirebaseCallback{
//        void onResponse(String hour, String minute);
//    }
//
//    public void readFirebase(FirebaseCallback callback){
//        DatabaseReference d = FirebaseDatabase.getInstance().getReference("Alarm");
//        d.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String hour = snapshot.child(id).child("hour").toString();
//                String minute = snapshot.child(id).child("minute").toString();
//                callback.onResponse(hour, minute);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//

}