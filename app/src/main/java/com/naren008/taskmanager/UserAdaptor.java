package com.naren008.taskmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.naren008.taskmanager.Profile.alert;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.MyViewHolder> {

    List<Users> usersList;
    Context context;
    List<String> tokens;
    FirebaseUser user;
    String userID;
    FirebaseAuth mAuth;


    public UserAdaptor(List<Users> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new
                MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_rec_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String username = usersList.get(position).getUsername();
        String role = usersList.get(position).getRole();
        String uid = usersList.get(position).getUid();
        holder.recUserName.setText(username);
        holder.roletxt.setText(role);

//        mAuth = FirebaseAuth.getInstance();
//        userToken = FirebaseDatabase.getInstance().getReference().child("tokens");

        holder.recAlrmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ALERT", alert+"");


                AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
                alertbox.setTitle("Alert Dialog");
                alertbox.setMessage("Do you want to override the alarm?");
                alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(view.getContext(), alarmSetter.class);
                        intent.putExtra("id",uid);
                        context.startActivity(intent);
                    }
                });

                alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Alarm is not override", Toast.LENGTH_SHORT).show();
                    }
                });
                alertbox.show();

//                if(alert.equals("true")){
//
//
//                    alertbox.show();
//                }else{
//                    Intent intent = new Intent(view.getContext(), alarmSetter.class);
//                    intent.putExtra("id",uid);
//                    context.startActivity(intent);
//                }


//                user = FirebaseAuth.getInstance();
//                userID = FirebaseAuth.getInstance();
//                tokens.add(uid);

                Toast.makeText(context, "Please set the alarm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView recUserName, roletxt;
        Button recAlrmBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            recUserName = itemView.findViewById(R.id.recUserName);
            roletxt = itemView.findViewById(R.id.roletxt);
            recAlrmBtn = itemView.findViewById(R.id.recAlrmBtn);
        }
    }
}
