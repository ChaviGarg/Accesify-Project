package com.finalyear.accesify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Teacher_login extends AppCompatActivity {
    public EditText teacher_email;
    public EditText teacher_password;
    ProgressDialog mProgressDialogue;
    FirebaseAuth mAuth;
    String email, password;
    DatabaseReference mUsersdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        mAuth = FirebaseAuth.getInstance();
        teacher_email = (EditText) findViewById(R.id.welcome_email_ID);
        teacher_password = (EditText) findViewById(R.id.welcome_password_ID);
        mUsersdatabase = FirebaseDatabase.getInstance().getReference().child("Teachers");
        mProgressDialogue = new ProgressDialog(this);
    }

    public void submit(View view) {

        email = teacher_email.getText().toString();
        password = teacher_password.getText().toString();
        if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
            mProgressDialogue.setTitle("Loggin in");
            mProgressDialogue.setMessage("Checking Credentials");
            mProgressDialogue.setCanceledOnTouchOutside(false);
            mProgressDialogue.show();

            loginuser(email, password);
        }

    }

    private void loginuser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {



                            Intent logintent = new Intent(Teacher_login.this, Home_Faculty.class);
                            logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logintent);
                            finish();

                        }



                 else {
                    mProgressDialogue.hide();
                    Toast.makeText(Teacher_login.this, "Please Check your Credentials,login failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}