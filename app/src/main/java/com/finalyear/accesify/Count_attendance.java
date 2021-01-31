package com.finalyear.accesify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Count_attendance extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;
    private String date,subject;

    Button upload_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_count_attendance);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager = new LinearLayoutManager(this);
       // View v = inflater.inflate(R.layout.activity_count_attendance, container, false);
        mUserslist = findViewById(R.id.attendance_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();
        startListening();

    }

    public void startListening() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Students")
                .limitToLast(100);

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.attendance_list_layout, parent, false);

                return new UserViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(final UserViewHolder holder, int position, final Users model) {

                date=getIntent().getStringExtra("date");
                // Bind the Chat object to
                // the ChatHolder
                holder.setName(model.name);
                holder.setRollnumber(model.roll_number);

               /* final DatabaseReference mStudentDatabase=FirebaseDatabase.getInstance().getReference().child("Attendance");
                mStudentDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(model.roll_number)) {
//                            final String subject = dataSnapshot.child("subject").getValue().toString();

                            /*mStudentDatabase.child(model.roll_number).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        if (dataSnapshot.hasChild(date)) {
                                            mStudentDatabase.child(model.roll_number).child(date).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot!=null) {

                                                        if(dataSnapshot.child("status").getValue().toString()=="present"){

                                                            holder.checkBox.setChecked(true);
                                                            holder.upload_button.setEnabled(false);
                                                            if (dataSnapshot.child("status").getValue().toString()=="absent"){
                                                            holder.checkBox.setChecked(false);
                                                            holder.upload_button.setEnabled(false);
                                                        }

                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //   mStudentDatabase.child(model.roll_number).child(dataSnapshot.child("subject").getValue().toString())

                        }
                    }
                        @Override
                        public void onCancelled (@NonNull DatabaseError databaseError){

                        }
                     });
*/


                holder.upload_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 subject=dataSnapshot.child("subject").getValue().toString();

                                final DatabaseReference AttendanceDatabase=FirebaseDatabase.getInstance().getReference().child("Attendance");
                                AttendanceDatabase.child(model.roll_number).child(subject).child(date).addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {

                                            if (holder.checkBox.isChecked())
                                            {
                                                HashMap<String,String> hashMap= new HashMap<>();
                                                hashMap.put("name",model.name);
                                                hashMap.put("status","present");
                                                hashMap.put("roll_number",model.roll_number);
                                                hashMap.put("date",date);
                                                hashMap.put("subject",subject);
                                                AttendanceDatabase.child(model.roll_number).child(subject).child("present").child(date).setValue(hashMap);
                                                holder.checkBox.setEnabled(false);
                                                holder.upload_button.setEnabled(false);

                                            }
                                            else{
                                                HashMap<String,String> hashMap= new HashMap<>();
                                                hashMap.put("name",model.name);
                                                hashMap.put("status","absent");
                                                hashMap.put("roll_number",model.roll_number);
                                                hashMap.put("date",date);
                                                AttendanceDatabase.child(model.roll_number).child(subject).child("absent").child(date).setValue(hashMap);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });


                // ...
            }

        };
        mUserslist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CheckBox checkBox;
        Button upload_button;

        public UserViewHolder(View itemView) {

            super(itemView);
            mView = itemView;
            upload_button=mView.findViewById(R.id.upload_button);
            checkBox= mView.findViewById(R.id.checkBox);
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.student_displayname);
            userNameView.setText(name);
        }

        public void setRollnumber(String status) {
            TextView userStatusView=(TextView)
                    mView.findViewById(R.id.student_Rollno_ID);
            userStatusView.setText(status);
        }



        }
}
