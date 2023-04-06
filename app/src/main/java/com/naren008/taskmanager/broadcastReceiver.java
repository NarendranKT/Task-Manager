package com.naren008.taskmanager;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class broadcastReceiver extends BroadcastReceiver {


    static Ringtone r;
    private int aud;
    @Override
    public void onReceive(Context context, Intent intent) {

        String si = intent.getStringExtra("id");
        String task = intent.getStringExtra("task");
        Log.d("ID FROM RECEIVER: ", si+"");
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("Alarm");
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String task = snapshot.child(si).child("hour").toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.d("PLAY1","PLAYING1");
        Intent i = new Intent(context, Stop.class);
        i.putExtra("id", si);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "alarm-notifier")
                .setSmallIcon(R.mipmap.ic_task_manager_round)
                .setContentTitle("New Task For You!")
                .setContentText("Open")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123, builder.build());


        Uri s = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (s == null){
            s = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (s == null){
                s = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }


        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolAlarm = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        aud = audioManager.getRingerMode();

//        Stop stop = new Stop(aud);
        int ringer = audioManager.getRingerMode();
        i.putExtra("ringer", ringer);

        if(audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_RING,maxVolAlarm,AudioManager.FLAG_PLAY_SOUND);
        r = RingtoneManager.getRingtone(context.getApplicationContext(), s);
        Log.d("PLAY-END","PLAYING-ENDED");
        r.play();



    }



}
