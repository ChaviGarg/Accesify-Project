package com.finalyear.accesify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home_student extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView mStudentNavView;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    String name,thumb_image,rollnumber;
    private FirebaseAuth mAuth;
    private DatabaseReference Studentdatabase;
    private Student_Submit_Assignments_fragment student_submit_assignments_fragment;

    private Student_View_Attendance_Fragment view_attendance_fragment;
    private Student_View_Grades_Fragment view_grades_fragment;
    private QRgenerationFragment qRgenerationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        mStudentNavView=findViewById(R.id.student_bottom_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Studentdatabase= FirebaseDatabase.getInstance().getReference().child("Students");
        mAuth=FirebaseAuth.getInstance();
            //initialtestcommit


        student_submit_assignments_fragment= new Student_Submit_Assignments_fragment();
        view_attendance_fragment=new Student_View_Attendance_Fragment();
        view_grades_fragment= new Student_View_Grades_Fragment();
        qRgenerationFragment= new QRgenerationFragment();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        if(mAuth.getCurrentUser()!=null) {
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    {
                        Studentdatabase.child(dataSnapshot.child("roll_number").getValue().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                name = dataSnapshot.child("name").getValue().toString();
                                rollnumber = dataSnapshot.child("roll_number").getValue().toString();
                                thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                                //  final View navView = navigationView.inflateHeaderView(R.layout.nav_header_home_student);
                                TextView userName = (TextView) navigationView.findViewById(R.id.nav_username);
                                userName.setText(name);
                                TextView userStatus = (TextView) navigationView.findViewById(R.id.nav_rollnumber);
                                userStatus.setText(rollnumber);

                                CircleImageView user_imageView =  navigationView.findViewById(R.id.nav_user_image);


                                Picasso.get().load(thumb_image).centerCrop().fit().placeholder(R.drawable.contact_avatar).into(user_imageView);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        mStudentNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.STBottomnav_View_attendance_button:
                        //mMainNavView.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(view_attendance_fragment);
                        return true;
                    case R.id.STBottomnav_Grades:
                        //mMainNavView.setItemBackgroundResource(R.color.colorP(result.getContents().compareTo(sharedinfo)==0)rimary);
                        setFragment(view_grades_fragment);
                        return true;
                    case R.id.STBottomnav_Submit_assignments:
                        //mMainNavView.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(student_submit_assignments_fragment);
                        return true;
                    default: return false;
                }
            }
        });
        if (savedInstanceState == null) {
            mStudentNavView.setSelectedItemId(R.id.STBottomnav_Grades); // change to whichever id should be default
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
     /*   int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
            return true;
        }

       // return super.onOptionsItemSelected(item);
    //}*/
   return false; }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment frag = null;
        if (id == R.id.STdrawer_Noticeboard_button) {
            frag = new Student_notice_fragment();
            // Handle the camera action
        } else if (id == R.id.STdrawer_ViewProfile_button) {
            frag = new Student_View_profie_fragment();

        } else if (id == R.id.STdrawer_Settings_button) {
            frag = new Student_settings_fragment();

        } else if (id == R.id.STdrawer_Logout_button) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,Welcome.class));
            finish();
            return true;
        } else if (id == R.id.nav_share) {
            frag= new QRgenerationFragment();
          //  Intent intent = new Intent(this,QRGenerator.class);
           // startActivity(intent);

        } else if (id == R.id.STdrawer_About_button) {
            frag = new About_fragments();

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.screenarea, frag).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.screenarea,fragment);
        fragmentTransaction.commit();
    }

}