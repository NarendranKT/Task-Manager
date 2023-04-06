package com.naren008.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail, passwordtxt;
    Button loginButton, forgetPwd, RegisterButton;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.loginEmail);
        passwordtxt = findViewById(R.id.passwordtxt);
        loginButton = findViewById(R.id.loginButton);
        forgetPwd = findViewById(R.id.forgetPwd);
        RegisterButton = findViewById(R.id.RegisterButton);
        progressDialog  = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        // This code used to stay logged in
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            if(firebaseUser.isEmailVerified()){
                startActivity(new Intent(MainActivity.this, Profile.class));
            }

        }
//Registerd user
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

//forget pswrd
        forgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

///Un Registered user
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });

    }

    private void userLogin() {

        String email = loginEmail.getText().toString();
        String pwd = passwordtxt.getText().toString();

        if(email.isEmpty()){
            loginEmail.setError("Please enter emailid");
            loginEmail.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Please enter valid emailid");
            loginEmail.requestFocus();
            return;
        }

        if (pwd.isEmpty()){
            passwordtxt.setError("Password is required");
            passwordtxt.requestFocus();
            return;
        }

        progressDialog.setMessage("Please Wait While logging in...");
        progressDialog.setTitle("Log In");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,Profile.class);
                        startActivity(intent);
                    }else{
                        progressDialog.dismiss();
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please check your email to verify your account", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to login, Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}