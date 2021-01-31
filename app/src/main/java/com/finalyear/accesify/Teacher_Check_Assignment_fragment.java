package com.finalyear.accesify;


import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Teacher_Check_Assignment_fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase,teacherDatabase;
    private LinearLayoutManager mLayoutManager;


    public Teacher_Check_Assignment_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
         teacherDatabase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_teacher__check__assignment, container, false);
        mUserslist = v.findViewById(R.id.assignment_check_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);
        return v;}


        @Override
        public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            teacherDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    {

                        Query query = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Assignments").child(dataSnapshot.child("subject").getValue().toString())
                                .limitToLast(50);

                        FirebaseRecyclerOptions<Users> options =
                                new FirebaseRecyclerOptions.Builder<Users>()
                                        .setQuery(query, Users.class)
                                        .build();
                        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users,Teacher_Check_Assignment_fragment.UserViewHolder>(options) {
                            @Override
                            public Teacher_Check_Assignment_fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                // Create a new instance of the ViewHolder, in this case we are using a custom
                                // layout called R.layout.message for each item
                                View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.assignment_list_layout, parent, false);

                                return new Teacher_Check_Assignment_fragment.UserViewHolder(view);
                            }

                            @Override
                            protected void onBindViewHolder(final Teacher_Check_Assignment_fragment.UserViewHolder holder, int position, final Users model) {
                                // Bind the Chat object to the ChatHolder

                                holder.setRollno(model.roll_number);
                                holder.setName(model.name);
                                holder.textView.setText(model.number);


                                teacherDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {
                                            final String subject = dataSnapshot.child("subject").getValue().toString();

                                            final DatabaseReference mStudentDatabase = FirebaseDatabase.getInstance().getReference().child("Checked_Assignments");
                                            mStudentDatabase.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot!=null){
                                                        if (dataSnapshot.hasChild(model.roll_number)) {
                                                            mStudentDatabase.child(model.roll_number).child(subject+model.number).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.hasChildren()) {

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
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                            //   mStudentDatabase.child(model.roll_number).child(dataSnapshot.child("subject").getValue().toString())

                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                holder.download_button.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        Toast.makeText(getContext(),"Swipe down the notification to view assignement",Toast.LENGTH_LONG).show();
                                        holder.downloadFile(getContext(),model.name,".pdf","/assignemts",model.assignment);

                                    }


                                });


                                holder.button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



                                        final String grades=holder.editText.getText().toString();

                                        DatabaseReference mTeacherDatbase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mTeacherDatbase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                String subject=dataSnapshot.child("subject").getValue().toString();
                                                HashMap<String,String> hashMap=new HashMap<>();
                                                hashMap.put("marks",grades);
                                                hashMap.put("name",subject);
                                                hashMap.put("number",model.number);
                                                FirebaseDatabase.getInstance().getReference().child("Checked_Assignments").child(model.roll_number).child(subject+model.number).setValue(hashMap);
                                                holder.button.setEnabled(false);
                                                holder.editText.setEnabled(false);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button,download_button;
        EditText editText;
        TextView textView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            button=mView.findViewById(R.id.upload_marks);
            editText=mView.findViewById(R.id.assignment_grades);
            textView=mView.findViewById(R.id.assignment_number);
            download_button=mView.findViewById(R.id.assignment_download);
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

        public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


            DownloadManager downloadmanager = (DownloadManager) context.
                    getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

            return downloadmanager.enqueue(request);
        }
    }


}
