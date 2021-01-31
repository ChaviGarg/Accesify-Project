package com.finalyear.accesify;


import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Student_Submit_Assignments_fragment extends Fragment  {
    final static int PICK_PDF_CODE = 2342;
    private ProgressDialog mProgressDialogue;
    private Button button;

    private EditText assignment_number;
    private StorageReference mStorageReference;
    private Spinner spin;
    private DatabaseReference assignmentDatabase,Usersdatabase;
    private RecyclerView mUserslist;
    private LinearLayoutManager mLayoutManager;


    public Student_Submit_Assignments_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        assignmentDatabase = FirebaseDatabase.getInstance().getReference();
        mProgressDialogue=new ProgressDialog(getContext());
        Usersdatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_student__submit__assignments, container, false);
        mUserslist = v.findViewById(R.id.view_assignments_reclyer);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         spin=getView().findViewById(R.id.submit_assignment_spinner);
         assignment_number=getView().findViewById(R.id.submit_assignment_number);
        button=getView().findViewById(R.id.submit_upload_button);
        Usersdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                {

                    Query query = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Checked_Assignments").child(dataSnapshot.child("roll_number").getValue().toString())
                            .limitToLast(50);

                    FirebaseRecyclerOptions<Users> options =
                            new FirebaseRecyclerOptions.Builder<Users>()
                                    .setQuery(query, Users.class)
                                    .build();
                    FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users,Student_Submit_Assignments_fragment.UserViewHolder>(options) {
                        @Override
                        public Student_Submit_Assignments_fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            // Create a new instance of the ViewHolder, in this case we are using a custom
                            // layout called R.layout.message for each item
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.view_checked_assignments_layout, parent, false);

                            return new Student_Submit_Assignments_fragment.UserViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(final Student_Submit_Assignments_fragment.UserViewHolder holder, int position, final Users model) {
                            // Bind the Chat object to the ChatHolder

                            holder.setNumber(model.number);
                            holder.setName(model.name);
                            holder.setMarks(model.marks);





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


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot != null) {
                     FirebaseDatabase.getInstance().getReference().child("Grades").child(dataSnapshot.child("roll_number").getValue().toString()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             if (dataSnapshot != null) {
                                 List<String> areas = new ArrayList<>();
                                 // Toast.makeText(getContext(), dataSnapshot.child("name").getValue().toString(), Toast.LENGTH_LONG).show();
                                 for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                     String subject = ds.child("name").getValue().toString();
                                     Log.d("Subject", subject);
                                     areas.add(subject);

                                 }
                                 ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, areas);
                                 arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                 spin.setAdapter(arrayAdapter);


                                 button.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         Intent intent = new Intent();
                                         intent.setType("application/pdf");
                                         intent.setAction(Intent.ACTION_GET_CONTENT);
                                         startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);

                                     }
                                 });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadFile(Uri uri){

        /*mProgressDialogue.setMessage("Please Wait");
        mProgressDialogue.show();*/

        final StorageReference filepath=mStorageReference.child("Assignments").child(spin.getSelectedItem().toString()+assignment_number.getText()+".pdf");
        filepath.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress =  (100.0 *taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                mProgressDialogue.setProgress(currentprogress);
                mProgressDialogue.setTitle("Uploading");
                mProgressDialogue.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialogue.setCanceledOnTouchOutside(false);
                mProgressDialogue.show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                while(!uriTask.isComplete());
                final Uri download_url = uriTask.getResult();
                Usersdatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String roll_number=dataSnapshot.child("roll_number").getValue().toString();
                        String name=dataSnapshot.child("name").getValue().toString();
                        String sub=spin.getSelectedItem().toString();
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put("assignment",download_url.toString());
                        hashMap.put("name",name);
                        hashMap.put("number",assignment_number.getText().toString());
                        hashMap.put("roll_number",roll_number);
                        hashMap.put("subject",sub);
                        assignmentDatabase.child("Assignments").child(spin.getSelectedItem().toString()).child(roll_number+assignment_number.getText().toString()).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            mProgressDialogue.dismiss();
                            Toast.makeText(getContext(),"Assignment uploaded",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });




            }



    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ProgressBar progressBar;


        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            progressBar=mView.findViewById(R.id.view_assignment_progressbar);

        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.view_assignment_subject);
            userNameView.setText(name);
        }

        public void setNumber(String no) {
            TextView userStatusView = (TextView)
                    mView.findViewById(R.id.view_assignment_number);
            userStatusView.setText(no);

        }
        public  void setMarks(String subject){
            TextView editText= mView.findViewById(R.id.viewgrades_grades);
            editText.setText(subject);
            ObjectAnimator.ofInt(progressBar, "progress",Integer.parseInt(subject ))
                    .setDuration(400)
                    .start();
        }


    }

}


