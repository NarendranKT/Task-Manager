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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPage extends AppCompatActivity {


    EditText RegUserName, RegEmail, RegRole, RegPhno, RegPwd, RegConPwd;
    Button Register;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        RegUserName = findViewById(R.id.RegUserName);
        RegEmail = findViewById(R.id.RegEmail);
        RegRole = findViewById(R.id.RegRole);
        RegPhno = findViewById(R.id.RegPhno);
        RegPwd = findViewById(R.id.RegPwd);
        RegConPwd = findViewById(R.id.RegConPwd);
        Register = findViewById(R.id.Register);
        progressDialog  = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regitrationForm();
            }
        });

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
    }

    private void regitrationForm() {
        String email = RegEmail.getText().toString();
        String username = RegUserName.getText().toString();
        String role = RegRole.getText().toString();
        String phno = RegPhno.getText().toString();
        String pwd = RegPwd.getText().toString();
        String conpwd = RegConPwd.getText().toString();

        if(username.isEmpty()){
            RegUserName.setError("Please enter username");
            RegUserName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            RegEmail.setError("Please enter emailid");
            RegEmail.requestFocus();
            return;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            RegEmail.setError("Please enter valid emailid");
            RegEmail.requestFocus();
            return;
        }

        if(role.isEmpty()){
            RegRole.setError("Please enter your role");
            RegRole.requestFocus();
            return;
        }

        if(phno.isEmpty()){
            RegPhno.setError("Please enter contact number");
            RegPhno.requestFocus();
            return;
        }

        if (pwd.isEmpty()){
            RegPwd.setError("Password is required");
            RegPwd.requestFocus();
            return;
        }

        if (pwd.length() < 6){
            RegPwd.setError("Min password length should be 6 characters");
            RegPwd.requestFocus();
            return;
        }

        if(!pwd.equals(conpwd)){
            RegConPwd.setError("Password doesn't matched");
            RegConPwd.requestFocus();
            return;
        }

        progressDialog.setMessage("Please Wait While Registering...");
        progressDialog.setTitle("Registration");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Users user = new Users(username,email,role,phno,uid);


                    FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        OnRegSuccess();
                                        Toast.makeText(RegisterPage.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterPage.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterPage.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OnRegSuccess() {
        Intent intent = new Intent(RegisterPage.this, MainActivity.class);
        startActivity(intent);
    }
}