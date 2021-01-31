package com.finalyear.accesify;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class Student_View_Grades_Fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;


    public Student_View_Grades_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_student__view__grades, container, false);
        mUserslist = v.findViewById(R.id.view_grades_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUsersDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String rollnumber=dataSnapshot.child("roll_number").getValue().toString();

                Log.d("",rollnumber);

               {
                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Grades").child(rollnumber)
                            .limitToLast(50);

                    FirebaseRecyclerOptions<Users> options =
                            new FirebaseRecyclerOptions.Builder<Users>()
                                    .setQuery(query, Users.class)
                                    .build();
                    FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, Student_View_Grades_Fragment.UserViewHolder>(options) {
                        @Override
                        public Student_View_Grades_Fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            // Create a new instance of the ViewHolder, in this case we are using a custom
                            // layout called R.layout.message for each item
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.view_grades_list_layout, parent, false);

                            return new Student_View_Grades_Fragment.UserViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(final Student_View_Grades_Fragment.UserViewHolder holder, int position, final Users model) {
                            // Bind the Chat object to the ChatHolder/
                            holder.setMarks(model.marks);
                            Log.d("",model.marks);
                            holder.setSubject(model.name);


                        /*   final DatabaseReference mGrades=FirebaseDatabase.getInstance().getReference().child("Grades").child(model.roll_number).child(model.name);
                            mGrades.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String grades=dataSnapshot.child(model.subject).getValue().toString();

                                        holder.textView.setText(grades);
                                        holder.textView2.setText(model.subject);

                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/



                        }

                    };
                    mUserslist.setAdapter(adapter);
                    adapter.startListening();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
       // Button button;
        TextView textView,textView2;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
           // button=mView.findViewById(R.id.student_list_switch);
            textView=mView.findViewById(R.id.viewgrades_grades);
            textView2=mView.findViewById(R.id.view_grades_subject);
        }

        public void setMarks(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.viewgrades_grades);
            ProgressBar progressBar=mView.findViewById(R.id.view_grades_progressbar);
            ObjectAnimator.ofInt(progressBar, "progress", Integer.parseInt(name))
                    .setDuration(400)
                    .start();
           // progressBar.setProgress(Integer.parseInt(name),true);
            userNameView.setText(name);
        }
/*
        public void setRollno(String rollno) {
            TextView userStatusView = (TextView)
                    mView.findViewById(R.id.student_Rollno_ID);
            userStatusView.setText(rollno);

        }*/
       public  void setSubject(String subject){
            TextView textView= mView.findViewById(R.id.view_grades_subject);
            textView.setText(subject);
       }
    }
}
