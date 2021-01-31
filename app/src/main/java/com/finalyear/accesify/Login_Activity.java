package com.finalyear.accesify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import org.w3c.dom.Text;

import java.util.HashMap;

public class Login_Activity extends AppCompatActivity
{
    //Declaration of fields
    private EditText mDisplayname,mRollnumber;
    private EditText mEmail;
    private EditText mPassword,mConfirmpassword;
    private Button reg_submit_button;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mUsersDatabase;
    //Progressdialog
    private ProgressDialog mProgressdialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDisplayname=(EditText)findViewById(R.id.log_displayname);
        mEmail=(EditText)findViewById(R.id.log_email);
        mPassword=(EditText) findViewById(R.id.log_pass);
        mRollnumber=(EditText) findViewById(R.id.log_rollnumber);
        mConfirmpassword=(EditText) findViewById(R.id.log_confirmpass);
        mToolbar=(Toolbar)findViewById(R.id.log_app_bar);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        //toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reg_submit_button=(Button)findViewById(R.id.log_register);

        reg_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayname.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String rollnumber = mRollnumber.getText().toString();
                String ConfirmPassword = mConfirmpassword.getText().toString();

                //progress dialogue
                mProgressdialogue = new ProgressDialog(Login_Activity.this);

                if (TextUtils.isEmpty(display_name) || (TextUtils.isEmpty(email)) || (TextUtils.isEmpty(password)||(TextUtils.isEmpty(rollnumber))))
                {
                    Toast.makeText(Login_Activity.this,"you have left one or two fields empty",Toast.LENGTH_LONG).show();

                }
                else if(!ConfirmPassword.equals(password))
                {
                    Toast.makeText(Login_Activity.this,"Passwords do not match",Toast.LENGTH_LONG).show();
                }
                else
                { mProgressdialogue.setTitle("Registering User");
                    mProgressdialogue.setMessage("Please wait");
                    mProgressdialogue.setCanceledOnTouchOutside(false);
                    mProgressdialogue.show();
                    register_user(display_name,email,password,rollnumber);
                }



            }
        });
    }

    private void register_user(final String display_name, final String email, final String password, final String rollnumber) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();
                 //   String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    String uid= currentuser.getUid();

                    mFirebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Students").child(rollnumber);

                    HashMap<String,String> usermap2= new HashMap<>();
                    usermap2.put("roll_number",rollnumber);
                    usermap2.put("name",display_name);
                    usermap2.put("image","default");
                    usermap2.put("thumb_image","default");
                    usermap2.put("email",email);
                    usermap2.put("password",password);
                    usermap2.put("branch","default");
                    usermap2.put("father_name","default");
                    usermap2.put("course","default");
                    usermap2.put("group","default");
                    usermap2.put("flag","0");
                    usermap2.put("phone_number","default");
                    mFirebaseDatabase.setValue(usermap2);
                    mFirebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        HashMap<String,String> usermap= new HashMap<>();
                        usermap.put("roll_number",rollnumber);
                        usermap.put("name",display_name);
                        usermap.put("image","default");
                        usermap.put("thumb_image","default");
                        usermap.put("email",email);
                        usermap.put("password",password);
                        usermap.put("branch","default");
                        usermap.put("father_name","default");
                        usermap.put("course","default");
                        usermap.put("group","default");
                        usermap.put("flag","0");
                        usermap.put("phone_number","default");
                   ;
                      //  final int finalI = i;
                        mFirebaseDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mProgressdialogue.dismiss();

                                Intent RegIntent=new Intent(Login_Activity.this,Home_student.class);
                                RegIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(RegIntent);
                                finish();
                            } }

                    });}
                    /*  */

                 else {
                    mProgressdialogue.hide();
                    Toast.makeText(Login_Activity.this, "Check your details once again", Toast.LENGTH_LONG);
                }
            }
        });
    }


}

