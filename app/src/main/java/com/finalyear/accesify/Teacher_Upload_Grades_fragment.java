package com.finalyear.accesify;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Teacher_Upload_Grades_fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;

    public Teacher_Upload_Grades_fragment() {
        // Required empty public constructor
    }

    //testdev27
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_teacher__upload__grades, container, false);
        mUserslist = v.findViewById(R.id.grade_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Students")
                    .limitToLast(50);

            FirebaseRecyclerOptions<Users> options =
                    new FirebaseRecyclerOptions.Builder<Users>()
                            .setQuery(query, Users.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, Teacher_Upload_Grades_fragment.UserViewHolder>(options) {
                @Override
                public Teacher_Upload_Grades_fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.grades_list_layout, parent, false);

                    return new Teacher_Upload_Grades_fragment.UserViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(final Teacher_Upload_Grades_fragment.UserViewHolder holder, int position, final Users model) {
                    // Bind the Chat object to the ChatHolder

                    holder.setRollno(model.roll_number);
                    holder.setName(model.name);

                    DatabaseReference  teacherDatabase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    teacherDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String subject =dataSnapshot.child("subject").getValue().toString();

                         final DatabaseReference mStudentDatabase=FirebaseDatabase.getInstance().getReference().child("Grades");
                            mStudentDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(model.roll_number)){
                                        mStudentDatabase.child(model.roll_number).child(subject).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.hasChildren()) {

                                                    holder.editText.setText(dataSnapshot.child("marks").getValue().toString());
                                                    holder.editText.setEnabled(false);
                                                    holder.button.setEnabled(false);
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                         //   mStudentDatabase.child(model.roll_number).child(dataSnapshot.child("subject").getValue().toString())

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatabaseReference mStudentDatbase=FirebaseDatabase.getInstance().getReference().child("Students").child(model.roll_number);

                            final String grades=holder.editText.getText().toString();
                            DatabaseReference mTeacherDatbase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                             mTeacherDatbase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        String subject = dataSnapshot.child("subject").getValue().toString();
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("marks", grades);
                                        hashMap.put("name", subject);
                                        FirebaseDatabase.getInstance().getReference().child("Grades").child(model.roll_number).child(subject).setValue(hashMap);
                                        holder.button.setEnabled(false);
                                        holder.editText.setEnabled(false);
                                        mStudentDatbase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String message = "Your grades for "+model.subject+" have been uploaded. Please check the application for the same";
                                                String number = dataSnapshot.child("phone_number").getValue().toString();
                                                SmsManager sms = SmsManager.getDefault();
                                                sms.sendTextMessage(number, null, message, null, null);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                }

            };
            mUserslist.setAdapter(adapter);
            adapter.startListening();
        }
    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button;
        EditText editText;


        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            button=mView.findViewById(R.id.upload_marks);

             editText=mView.findViewById(R.id.grades_ET);
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.student_displayname);
            userNameView.setText(name);
        }

        public void setRollno(String rollno) {
            TextView userStatusView = (TextView)
                    mView.findViewById(R.id.student_Rollno_ID);
            userStatusView.setText(rollno);

        }
        public  void setSubject(String subject){
            /*EditText editText= mView.findViewById(R.id.grades_ET);
            editText.setText(subject);*/
        }
    }

}
