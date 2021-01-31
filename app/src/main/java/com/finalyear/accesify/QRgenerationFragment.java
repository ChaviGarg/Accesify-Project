package com.finalyear.accesify;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import static com.finalyear.accesify.R.id.qr_rollnumber;


/**
 * A simple {@link Fragment} subclass.
 */
public class QRgenerationFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference mUsersDatabase;
    EditText mrollnumber;
    ImageView iv;
    Button b1, b2;
    String rollnum;
    static String info,email,password;
    ProgressDialog progressDialog;


    public QRgenerationFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrgeneration, container, false);




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //mrollnumber =getView().findViewById(qr_rollnumber);
        iv = getView().findViewById(R.id.imageView);
        b1 = getView().findViewById(R.id.button2);
        b2 = getView().findViewById(R.id.button4);
        super.onViewCreated(view, savedInstanceState);
        iv.setImageDrawable(null);
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.keepSynced(true);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



              //  rollnum = mrollnumber.getText().toString();
                /*progressDialog= new ProgressDialog(getContext());
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Please wait while generate your QR code");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();*/

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        email = dataSnapshot.child("email").getValue().toString();
                        password = dataSnapshot.child("password").getValue().toString();
                        info = password +"+"+ email;
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(info, BarcodeFormat.QR_CODE, 200, 200);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            iv.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Bitmap image= BitmapFactory.decodeResource(getResources(), iv.getId());

                File path=Environment.getExternalStorageDirectory();

                File dir=new File(path+"/savedimages/");
                dir.mkdir();

                File file=new File(dir, "qrcode.png");

                OutputStream out=null;
                try
                {
                    out=new FileOutputStream(file);
                    image.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();



                }
                catch (java.io.IOException e)
                {
                    e.printStackTrace();
                }
*/

                if (iv.getDrawable() == null) {
                    Toast.makeText(getActivity(), "Image not found", Toast.LENGTH_LONG).show();
                } else {
                    BitmapDrawable draw = (BitmapDrawable) iv.getDrawable();
                    Bitmap bitmap = draw.getBitmap();

                    FileOutputStream outStream = null;
                    File sdCard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdCard.getAbsolutePath() + "/savedCodes");
                    dir.mkdirs();
                    String fileName = String.format("%d.jpg", System.currentTimeMillis());
                    File outFile = new File(dir, fileName);
                    try {
                        outStream = new FileOutputStream(outFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    try {
                        outStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(outFile));
                    //sendBroadcast(intent);

                    Toast.makeText(getActivity(), "Image Saved", Toast.LENGTH_LONG).show();
                }
            }


        });


    }
}


