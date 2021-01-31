package com.finalyear.accesify;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.jackandphantom.blurimage.BlurImage;


/**
 * A simple {@link Fragment} subclass.
 */
public class Student_View_profie_fragment extends Fragment {


    private DatabaseReference Studentdatabase;
    private CircleImageView circleImageView;
    private ImageView imageView;
    private TextView mobile,father_name,display_name,group,branch,rollnumber,course;


    public Student_View_profie_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student__view_profie_, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mobile=getView().findViewById(R.id.mobileNumber);
        circleImageView=getView().findViewById(R.id.profile);
        display_name=getView().findViewById(R.id.name);
        group=getView().findViewById(R.id.group);
        branch=getView().findViewById(R.id.branch);
        rollnumber=getView().findViewById(R.id.rollnum);
        course=getView().findViewById(R.id.course);
        father_name=getView().findViewById(R.id.father_name);
        imageView=getView().findViewById(R.id.header_cover_image);
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Studentdatabase= FirebaseDatabase.getInstance().getReference().child("Students").child(dataSnapshot.child("roll_number").getValue().toString());
                Studentdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name_value= dataSnapshot.child("name").getValue().toString();
                        String course_value= dataSnapshot.child("course").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        String group_value = dataSnapshot.child("group").getValue().toString();
                        String father_name_value= dataSnapshot.child("father_name").getValue().toString();
                        String branch_value = dataSnapshot.child("branch").getValue().toString();
                        String phone_value = dataSnapshot.child("phone_number").getValue().toString();
                        String roll_number =dataSnapshot.child("roll_number").getValue().toString();
                        final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                        display_name.setText(name_value);
                        branch.setText(branch_value);
                        group.setText(group_value);
                        course.setText(course_value);
                        mobile.setText(phone_value);
                        father_name.setText(father_name_value);
                        rollnumber.setText(roll_number);

                        if (!image.equals("default")) {

                            Picasso.get().load(thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.contact_avatar).into(circleImageView, new Callback() {
                                @Override
                                public void onSuccess() {




                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(thumb_image).placeholder(R.drawable.contact_avatar).into(circleImageView);
                                }
                            });

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
}
