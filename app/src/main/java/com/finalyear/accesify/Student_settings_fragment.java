package com.finalyear.accesify;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Student_settings_fragment extends Fragment {

    private static final int mGalleryPick = 1;
    private int branchbtntype,groupbtntype,fatherbtntype,phonebtntype,coursebtntype;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference StudentDatabase;
    private DatabaseReference databaseReference;
    private TextView textView;
    private ProgressDialog mProgressDialogue;
    private CircleImageView circleImageView;
    private EditText father_name, course, branch, phonenumber, group;
    String rollnumber;
    private Button change_photo, change_fathername, change_course, change_phonenumber, change_group, change_branch;


    public Student_settings_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_student_settings, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        circleImageView = getView().findViewById(R.id.settings_image_ID);
        textView = getView().findViewById(R.id.settings_displayname_ID);
        father_name = getView().findViewById(R.id.settings_fathername_ID);
        branch = getView().findViewById(R.id.settings_branch_ID);
        phonenumber = getView().findViewById(R.id.settings_phonenumber_ID3);
        group = getView().findViewById(R.id.settings_group_name_ID);
        course = getView().findViewById(R.id.settings_Course_ID);
        change_photo = getView().findViewById(R.id.settings_changeDP_Btn_ID);
        change_course = getView().findViewById(R.id.settings_changeCourse_button_ID);
        change_fathername = getView().findViewById(R.id.settings_changefathername_button_ID5);
        change_phonenumber = getView().findViewById(R.id.settings_changephone_button_ID3);
        change_group = getView().findViewById(R.id.settings_changegroup_button_ID4);
        change_branch = getView().findViewById(R.id.settings_changebranch_button_ID2);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        StudentDatabase=FirebaseDatabase.getInstance().getReference().child("Students");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.keepSynced(true);


        super.onViewCreated(view, savedInstanceState);
        //Disabling the buttons
        branch.setEnabled(false);
        phonenumber.setEnabled(false);
        group.setEnabled(false);
        course.setEnabled(false);
        father_name.setEnabled(false);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rollnumber=dataSnapshot.child("roll_number").getValue().toString();

                FirebaseDatabase.getInstance().getReference().child("Students").child(rollnumber).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name_value= dataSnapshot.child("name").getValue().toString();
                        String course_value= dataSnapshot.child("course").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        String group_value = dataSnapshot.child("group").getValue().toString();
                        String father_name_value= dataSnapshot.child("father_name").getValue().toString();
                        String branch_value = dataSnapshot.child("branch").getValue().toString();
                        String phone_value = dataSnapshot.child("phone_number").getValue().toString();
                        final String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                        textView.setText(name_value);
                        branch.setText(branch_value);
                        group.setText(group_value);
                        course.setText(course_value);
                        phonenumber.setText(phone_value);
                        father_name.setText(father_name_value);
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


        change_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(branchbtntype==1){
                    //progress bar for changing status
                    mProgressDialogue=new ProgressDialog(getContext());
                    mProgressDialogue.setTitle("Saving Changes");
                    mProgressDialogue.setMessage("Please wait.....");
                    mProgressDialogue.show();

                    String newbranch=branch.getText().toString();
                    StudentDatabase.child(rollnumber).child("branch").setValue(newbranch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();
                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your branch",Toast.LENGTH_LONG).show();
                        }
                    });


                    branchbtntype=0;
                    branch.setEnabled(false);
                    change_branch.setText("Change Branch");


                }else {
                    branchbtntype=1;
                    change_branch.setText("Save Branch");
                   // change_branch.setBackgroundColor(Color.BLUE);
                    branch.setEnabled(true);
                    branch.setText("");

                }

            }
        });
        change_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupbtntype==1){
                    //progress bar for changing status
                    mProgressDialogue=new ProgressDialog(getContext());
                    mProgressDialogue.setTitle("Saving Changes");
                    mProgressDialogue.setMessage("Please wait.....");
                    mProgressDialogue.show();

                    String newbranch=group.getText().toString();
                    StudentDatabase.child(rollnumber).child("group").setValue(newbranch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();
                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your group",Toast.LENGTH_LONG).show();
                        }
                    });

                    groupbtntype=0;
                    group.setEnabled(false);
                    change_group.setText("Change Group");


                }else {
                    groupbtntype=1;
                    change_group.setText("Save Group");
                    // change_branch.setBackgroundColor(Color.BLUE);
                    group.setEnabled(true);
                    group.setText("");

                }

            }
        });
        change_fathername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fatherbtntype==1){
                    //progress bar for changing status
                    mProgressDialogue=new ProgressDialog(getContext());
                    mProgressDialogue.setTitle("Saving Changes");
                    mProgressDialogue.setMessage("Please wait.....");
                    mProgressDialogue.show();

                    String newbranch=father_name.getText().toString();

                    StudentDatabase.child(rollnumber).child("father_name").setValue(newbranch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();

                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your branch",Toast.LENGTH_LONG).show();
                        }
                    });
                    /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();
                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your branch",Toast.LENGTH_LONG).show();
                        }
                    });*/
                    fatherbtntype=0;
                    father_name.setEnabled(false);
                    change_fathername.setText("Change Name");


                }else {
                    fatherbtntype=1;
                    change_fathername.setText("Save Name");
                    // change_branch.setBackgroundColor(Color.BLUE);
                    father_name.setEnabled(true);
                    father_name.setText("");

                }

            }
        });
        change_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coursebtntype==1){
                    //progress bar for changing status
                    mProgressDialogue=new ProgressDialog(getContext());
                    mProgressDialogue.setTitle("Saving Changes");
                    mProgressDialogue.setMessage("Please wait.....");
                    mProgressDialogue.show();

                    String newbranch=course.getText().toString();
                    StudentDatabase.child(rollnumber).child("course").setValue(newbranch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();
                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your Group",Toast.LENGTH_LONG).show();
                        }
                    });

                    coursebtntype=0;
                    course.setEnabled(false);
                    change_course.setText("Change Course");


                }else {
                    coursebtntype=1;
                    change_course.setText("Save Course");
                    // change_branch.setBackgroundColor(Color.BLUE);
                    course.setEnabled(true);
                    course.setText("");

                }

            }
        });
        change_phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phonebtntype==1){
                    //progress bar for changing status
                    mProgressDialogue=new ProgressDialog(getContext());
                    mProgressDialogue.setTitle("Saving Changes");
                    mProgressDialogue.setMessage("Please wait.....");
                    mProgressDialogue.show();

                    String newbranch=phonenumber.getText().toString();
                    StudentDatabase.child(rollnumber).child("phone_number").setValue(newbranch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressDialogue.dismiss();
                            }
                            else
                                Toast.makeText(getContext(),"there was some error in changing your Group",Toast.LENGTH_LONG).show();
                        }
                    });


                    phonebtntype=0;
                    phonenumber.setEnabled(false);
                    change_phonenumber.setText("Change Phone");


                }else {
                    phonebtntype=1;
                    change_phonenumber.setText("Save Phone");
                    // change_branch.setBackgroundColor(Color.BLUE);
                    phonenumber.setEnabled(true);
                    phonenumber.setText("");

                }

            }
        });
        //DP AND THUMB IMAGE
        change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), mGalleryPick);
         /* CropImage.activity()
                   .setGuidelines(CropImageView.Guidelines.ON)
                   .start(SettingsActivity.this); */
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mGalleryPick && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(getContext(),this);
            //  Toast.makeText(getActivity(), "" + imageUri, Toast.LENGTH_LONG).show();
        }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //Toast.makeText(getActivity(), "heloo", Toast.LENGTH_LONG).show();
                final CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {//Progress Image
                    mProgressDialogue = new ProgressDialog(getActivity());

                    mProgressDialogue.setTitle("Uploading Image");
                    mProgressDialogue.setMessage("Please wait while the image gets uploaded");
                    mProgressDialogue.setCanceledOnTouchOutside(false);
                    mProgressDialogue.show();
                    //image upload


                    final Uri resultUri = result.getUri();


                    File thumb_imagefile = new File(resultUri.getPath());

                    String Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    Bitmap thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(getActivity()).setMaxHeight(200).setQuality(70).compressToBitmap(thumb_imagefile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();


                    final StorageReference filepath = storageReference.child("profile_photos").child(Current_user_id + ".jpg");
                    final StorageReference thumb_filepath = storageReference.child("profile_photos").child("thumbs").child(Current_user_id + "jpg");

                    filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                                while(!uriTask.isComplete());
                                final Uri download_url = uriTask.getResult();

                                //Thumbnail generation
                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                        Task<Uri> uriTask = thumb_task.getResult().getStorage().getDownloadUrl();

                                        while(!uriTask.isComplete());
                                        final Uri thumb_download_url = uriTask.getResult();


                                        if (thumb_task.isSuccessful()) {

                                            Map update_hashmap = new HashMap<>();
                                            update_hashmap.put("image", download_url.toString());
                                            update_hashmap.put("thumb_image", thumb_download_url.toString());


                                            StudentDatabase.child(rollnumber).updateChildren(update_hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mProgressDialogue.dismiss();
                                                        Toast.makeText(getActivity(), "Profile picture updated", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });

                                        } else {

                                            Toast.makeText(getActivity(), " Unsuccessful", Toast.LENGTH_LONG).show();
                                            mProgressDialogue.dismiss();
                                        }
                                    }
                                });


                            } else {

                            }
                        }
                    });

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    Exception error = result.getError();
                }
            }

        }


    }
