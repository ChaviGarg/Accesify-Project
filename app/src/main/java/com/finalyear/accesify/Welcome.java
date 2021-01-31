package com.finalyear.accesify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Welcome extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private EditText welcome_email;
    String contents;
    private EditText welcome_password;
    private Button welcome_submit_button;
    private Button welcome_register_button, welcome_scan_button;
    private DatabaseReference mUserDatabase;
    private ProgressDialog mProgressDialogue;
    String info, email, password;
    private static final int SELECT_PHOTO = 100;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        welcome_email = (EditText) findViewById(R.id.welcome_email_ID);
        welcome_password = (EditText) findViewById(R.id.welcome_password_ID);

       // mToolbar = (Toolbar) findViewById(R.id.welcome_appbar);
        mProgressDialogue = new ProgressDialog(this);
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //setSupportActionBar(mToolbar);
       // getSupportActionBar().setTitle("Login");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        welcome_register_button = (Button) findViewById(R.id.welcome_register_btnID);
        welcome_email = (EditText) findViewById(R.id.welcome_email_ID);
        welcome_password = (EditText) findViewById(R.id.welcome_password_ID);
        welcome_scan_button = (Button) findViewById(R.id.welcome_Scan_BTN_ID);
        welcome_submit_button = findViewById(R.id.welcome_submit_button);
        welcome_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = welcome_email.getText().toString();
                password = welcome_password.getText().toString();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mProgressDialogue.setTitle("Loggin in");
                    mProgressDialogue.setMessage("Checking Credentials");
                    mProgressDialogue.setCanceledOnTouchOutside(false);
                    mProgressDialogue.show();

                    info = password + email;
                    loginUser(email, password);
                }

            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null )
        {
            // FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("flag").keepSynced(true);
            mProgressDialogue.setTitle("Logging In");
            mProgressDialogue.setMessage("Please wait while we load your profile");
            mProgressDialogue.show();
            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    flag=Integer.parseInt(dataSnapshot.child("flag").getValue().toString());
                    Toast.makeText(getApplicationContext(),String.valueOf(flag),Toast.LENGTH_LONG).show();
                    if(flag==1)
                    {
                        Intent authintent = new Intent(getApplicationContext(), Home_Faculty.class);
                        mProgressDialogue.dismiss();
                        startActivity(authintent);
                        finish();
                    }
                       if (flag==0)
                       {
                           Intent authintent = new Intent(getApplicationContext(), Home_student.class);
                           mProgressDialogue.dismiss();
                           startActivity(authintent);
                           finish();}


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }) ;

        }

    }

    private void loginUser(String email, String Password) {
        mAuth.signInWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    mProgressDialogue.setMessage("Just a sec");
                    mProgressDialogue.show();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            flag=Integer.parseInt(dataSnapshot.child("flag").getValue().toString());
                            Toast.makeText(getApplicationContext(),String.valueOf(flag),Toast.LENGTH_LONG).show();
                            if(flag==1)
                            {

                                Intent authintent = new Intent(getApplicationContext(), Home_Faculty.class);
                                authintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(authintent);
                                finish();
                            }
                            if (flag==0)
                            {
                                Intent authintent = new Intent(getApplicationContext(), Home_student.class);
                                authintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(authintent);
                                finish();}
                            else Toast.makeText(getApplicationContext(),"Flag not set some error occured",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    }) ;


                            /*Intent logintent = new Intent(Welcome.this, Home_student.class);
                            logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logintent);
                            finish();*/




                } else {
                    mProgressDialogue.hide();
                    Toast.makeText(Welcome.this, "Please Check your Credentials,login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI (result.getContents().compareTo(sharedinfo)==0)accordingly.


    }

    public void Register(View view) {
        Intent intent = new Intent(Welcome.this, Login_Activity.class);
        startActivity(intent);
    }

    public void Scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    public void imp_gallery(View view) {
        Intent photoPic = new Intent(Intent.ACTION_PICK);
        photoPic.setType("image/*");
        startActivityForResult(photoPic, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO) {//if scanned from gallery
            if (resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                InputStream imagestream = null;
                try {
                    imagestream = getContentResolver().openInputStream(selectedImage);

                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "File not Found", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                Bitmap bMap = BitmapFactory.decodeStream(imagestream);
                int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];

                // copy pixel data from the Bitmap into the 'intArray' array
                bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(),
                        bMap.getHeight());

                LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
                        bMap.getHeight(), intArray);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                MultiFormatReader reader = new MultiFormatReader();
                Result result = null;
                try {
                    result = reader.decode(bitmap);
                    contents = result.getText();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }

                //comaring obtained string with shared info
                if (true) {

                    Toast.makeText(this, "ID Authenticated", Toast.LENGTH_LONG).show();
                    mProgressDialogue.setMessage("Your QR code is Authenticated, logging you in");
                    mProgressDialogue.show();

                    String res = contents;
                    String password1 = contents.substring(0, res.indexOf('+')).trim();
                    String email1 = contents.substring(contents.indexOf('+') + 1).trim();
                  //  Toast.makeText(this, email1, Toast.LENGTH_LONG).show();
                    mProgressDialogue.dismiss();
                    loginUser(email1, password1);
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanning Cancelled", Toast.LENGTH_LONG).show();

            } else if (1 == 1) {
                String res = result.getContents();
                String password1 = result.getContents().substring(0, res.indexOf('+')).trim();
                String email1 = result.getContents().substring(result.getContents().indexOf('+') + 1).trim();
              //  Toast.makeText(this, email1, Toast.LENGTH_LONG).show();
                loginUser(email1, password1);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_LONG).show();
            }

        }
    }
    public void Faculty_login(View view){
        Intent intent= new Intent(this,Teacher_login.class);
        startActivity(intent);
    }

}
