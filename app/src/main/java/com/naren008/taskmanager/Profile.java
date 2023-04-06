package com.naren008.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    RecyclerView userRecView;
    List<Users> usersMenu;
    UserAdaptor userAdaptor;
    DatabaseReference userDb;
    FirebaseUser firebaseUser;
    String userID;
    FirebaseAuth mAuth;
     static String alert;

//    Button recAlrmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userRecView = findViewById(R.id.userRecView);
        usersMenu = new ArrayList<>();
//        recAlrmBtn = findViewById(R.id.recAlrmBtn)
        mAuth = FirebaseAuth.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Profile.this);
        userRecView.setLayoutManager(linearLayoutManager);

        userAdaptor = new UserAdaptor(usersMenu, this);
        userRecView.setAdapter(userAdaptor);
        getUserNames();

        alert = getIntent().getStringExtra("dialog");
        Intent in = new Intent(Profile.this, UserAdaptor.class);
        in.putExtra("alert", alert);
        Log.d("DIALOG BOX", alert+"");
    }

//    public void onStart(){
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            getUserNames();
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    private void getUserNames() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //userID = firebaseUser.getUid();
        userDb = FirebaseDatabase.getInstance().getReference("User");

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Users users = snapshot1.getValue(Users.class);
                    usersMenu.add(users);

                }
                userAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}