package com.naren008.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText ResetEmail;
    Button ResetPwd;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);

        ResetEmail = findViewById(R.id.ResetEmail);
        ResetPwd = findViewById(R.id.ResetPwd);
        Auth = FirebaseAuth.getInstance();

        ResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = ResetEmail.getText().toString();

        if(email.isEmpty()){
            ResetEmail.setError("Please enter emailid");
            ResetEmail.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ResetEmail.setError("Please enter valid emailid");
            ResetEmail.requestFocus();
            return;
        }

        Auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    loginActivity();
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ForgotPassword.this, "Try again! Something wrong is happened!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginActivity() {
        Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}