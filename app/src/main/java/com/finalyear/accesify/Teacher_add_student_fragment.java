package com.finalyear.accesify;


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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Teacher_add_student_fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;

    public Teacher_add_student_fragment() {
        // Required empty public constructor
    }

    //testdev27
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_teacher_add_student, container, false);
        mUserslist = v.findViewById(R.id.users_recycler);
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
                    .child("Students")
                    .limitToLast(50);

            FirebaseRecyclerOptions<Users> options =
                    new FirebaseRecyclerOptions.Builder<Users>()
                            .setQuery(query, Users.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, Teacher_add_student_fragment.UserViewHolder>(options) {
                @Override
                public Teacher_add_student_fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.student_lists_layout, parent, false);

                    return new Teacher_add_student_fragment.UserViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(final Teacher_add_student_fragment.UserViewHolder holder, int position, final Users model) {
                    // Bind the Chat object to the ChatHolder

                    holder.setRollno(model.roll_number);
                    holder.setName(model.name);
                    holder.setImage(model.thumb_image);
                    final String user_id = getRef(position).getKey();
                    holder.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            final DatabaseReference mTeacherDatbase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            DatabaseReference mStudentDatbase=FirebaseDatabase.getInstance().getReference().child("Students").child(model.roll_number);

                            Toast.makeText(view.getContext(),model.roll_number+view.getNextFocusUpId(),Toast.LENGTH_LONG).show();
                                mStudentDatbase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String name_value= dataSnapshot.child("name").getValue().toString();
                                        String rollnumber =dataSnapshot.child("roll_number").getValue().toString();

                                        HashMap<String,String> usermap2= new HashMap<>();
                                        usermap2.put("roll_number",rollnumber);
                                        usermap2.put("name",name_value);

                                        mTeacherDatbase.child("Students").child(model.roll_number).setValue(usermap2);
                                        mTeacherDatbase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String subject=dataSnapshot.child("subject").getValue().toString();
                                                Map Map= new HashMap<>();
                                                Map.put("subject",subject);
                                                holder.button.setEnabled(false);
                                                mTeacherDatbase.child("Students").child(model.roll_number).updateChildren(Map, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                        if(databaseError != null){

                                                            Log.d("CHAT_LOG", databaseError.getMessage().toString());}
                                                    }
                                                });

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
    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            button=mView.findViewById(R.id.upload_marks);
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
        public void setImage(String thumb_image){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.student_imageID);
            Picasso.get().load(thumb_image).placeholder(R.drawable.contact_avatar).into(userImageView);

        }
    }

}
