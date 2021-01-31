package com.finalyear.accesify;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
public class Student_View_Attendance_Fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase,mTeacherDatabase;
    private LinearLayoutManager mLayoutManager;


    public Student_View_Attendance_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mTeacherDatabase=FirebaseDatabase.getInstance().getReference().child("Teachers");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_student_attendance_view, container, false);
        mUserslist = v.findViewById(R.id.view_attendance_recycler);
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
                                            .child("Teachers")
                                            .limitToLast(50);

                FirebaseRecyclerOptions<Users> options =
                                            new FirebaseRecyclerOptions.Builder<Users>()
                                                    .setQuery(query, Users.class)
                                                    .build();
                FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, Student_View_Attendance_Fragment.UserViewHolder>(options) {
                                        @Override
                                        public Student_View_Attendance_Fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                            // Create a new instance of the ViewHolder, in this case we are using a custom
                                            // layout called R.layout.message for each item
                                            View view = LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.view_attendance, parent, false);

                                            return new Student_View_Attendance_Fragment.UserViewHolder(view);
                                        }
                                        @Override
                    protected void onBindViewHolder(final Student_View_Attendance_Fragment.UserViewHolder holder, final int position, final Users model) {
                                            // Bind the Chat object to the ChatHolder
                                         holder.teacher_name.setText(model.name);
                                           holder.subject_tv.setText(model.subject);

                                           // holder.setName(model.name);

                                            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot != null) {
                                                        String roll_number = dataSnapshot.child("roll_number").getValue().toString();
                                                        FirebaseDatabase.getInstance().getReference().child("Attendance").child(roll_number).child(model.subject).child("present").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot != null) {

                                                                    String count = String.valueOf(dataSnapshot.getChildrenCount());
                                                                    holder.present_tv.setText(count);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        }); FirebaseDatabase.getInstance().getReference().child("Attendance").child(roll_number).child(model.subject).child("absent").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot != null) {

                                                                    String count = String.valueOf(dataSnapshot.getChildrenCount());
                                                                    holder.absent_tv.setText(count);
                                                                    holder.total_tv.setText((Integer.parseInt(holder.absent_tv.getText().toString())+Integer.parseInt(holder.present_tv.getText().toString())+""));
                                                                    Double percent=(Double.parseDouble(holder.present_tv.getText().toString())/(Double.parseDouble(holder.total_tv.getText().toString())))*100;
                                                                    holder.overall_tv.setText(percent.intValue()+"%");
                                                                    ObjectAnimator.ofInt(holder.progressBar, "progress",percent.intValue() )
                                                                            .setDuration(400)
                                                                            .start();

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






                                        }

                                    };
                                    mUserslist.setAdapter(adapter);
                                    adapter.startListening();
                                }

    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button;
        TextView subject_tv,present_tv,absent_tv,total_tv,overall_tv,teacher_name;
        ProgressBar progressBar;


        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            subject_tv=mView.findViewById(R.id.view_attendance_subject);
            present_tv=mView.findViewById(R.id.view_present);
            total_tv=mView.findViewById(R.id.view_total);
            overall_tv=mView.findViewById(R.id.view_percent);
            absent_tv=mView.findViewById(R.id.view_absent);
            teacher_name=mView.findViewById(R.id.view_teachers_name);
            progressBar=mView.findViewById(R.id.view_attendance_progressbar);
        }

        public void setName(String name) {
            //TextView userNameView = (TextView) mView.findViewById(R.id.student_displayname);
         //   userNameView.setText(name);
        }

        public void setRollno(String rollno) {
        //    TextView userStatusView = (TextView)
              //      mView.findViewById(R.id.student_Rollno_ID);
          //  userStatusView.setText(rollno);

        }
        public  void setSubject(String subject){
            /*EditText editText= mView.findViewById(R.id.grades_ET);
            editText.setText(subject);*/
        }
    }
}
