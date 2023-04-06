package com.naren008.taskmanager;

import static com.naren008.taskmanager.broadcastReceiver.r;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Stop extends AppCompatActivity {

    TextView logintext;
    Button Stop2;
    AudioManager audioManager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);

        String id = getIntent().getStringExtra("id");
        int ringer = getIntent().getIntExtra("ringer", 2);
//        Log.d("RINGER : ", ringer+"");
        logintext = findViewById(R.id.logintext);
        Stop2 = findViewById(R.id.Stop);

//        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
//                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                }

                r.stop();
            }
        });

        DatabaseReference duser = FirebaseDatabase.getInstance().getReference("Alarm");
        duser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String task = snapshot.child(id).child("task").getValue().toString();
                logintext.setText(task);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}