package com.finalyear.accesify;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Teacher_Add_Notice_fragment extends Fragment  {
    private static final int mGalleryPick = 1;
    private ProgressDialog mProgressDialogue;
    private Button upload_noticebtn;
    private EditText notice_info;
    private StorageReference storageReference;
    private DatabaseReference databaseReference,TeacherDatabase;



    public Teacher_Add_Notice_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notice");
        TeacherDatabase=FirebaseDatabase.getInstance().getReference().child("Teachers").child(uid);
        storageReference = FirebaseStorage.getInstance().getReference();

        return inflater.inflate(R.layout.fragment_teacher__add__notice, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        notice_info=getActivity().findViewById(R.id.notice_name);
        upload_noticebtn=getActivity().findViewById(R.id.notice_upload_button);

        upload_noticebtn.setOnClickListener(new View.OnClickListener() {
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
                    .start(getContext(),this);
            //  Toast.makeText(getActivity(), "" + imageUri, Toast.LENGTH_LONG).show();
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {//Progress Image
                mProgressDialogue = new ProgressDialog(getActivity());

                mProgressDialogue.setTitle("Uploading Image");
                mProgressDialogue.setMessage("Please wait while the Notice gets uploaded");
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();
                //image upload


                final Uri resultUri = result.getUri();


              ;

                String Current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();





                final StorageReference filepath = storageReference.child("notices").child(Calendar.getInstance().getTimeInMillis() + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                            while(!uriTask.isComplete());
                            final Uri download_url = uriTask.getResult();

                            TeacherDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name=dataSnapshot.child("name").getValue().toString();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                    String currentDateandTime = sdf.format(new Date());
                                    Map update_hashmap = new HashMap<>();
                                    update_hashmap.put("notice", download_url.toString());
                                    update_hashmap.put("name",name);
                                    update_hashmap.put("info",notice_info.getText().toString());
                                    update_hashmap.put("date", currentDateandTime);
                                    update_hashmap.put("time",Calendar.getInstance().getTimeInMillis()*-1);
                                    Log.d(name,notice_info.getText().toString());
                                   databaseReference.child(name+notice_info.getText().toString()).setValue(update_hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgressDialogue.dismiss();
                                                Toast.makeText(getContext(), "Notice Uploaded", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {

                            Toast.makeText(getActivity(), " Unsuccessful", Toast.LENGTH_LONG).show();
                            mProgressDialogue.dismiss();
                        }




                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }

    }




