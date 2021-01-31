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

import com.google.firebase.auth.FirebaseAuth;

public class Home_Faculty extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mTeacherNavView;
    private Teacher_Check_Assignment_fragment teacher_check_assignments_fragment;
    private Teacher_Upload_Attendance_fragment teacher_upload_attendance_fragment;
    private Teacher_Upload_Grades_fragment teacher_upload_grades_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__faculty);
        mTeacherNavView=findViewById(R.id.faculty_bottom_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        teacher_check_assignments_fragment= new Teacher_Check_Assignment_fragment();
        teacher_upload_attendance_fragment=new Teacher_Upload_Attendance_fragment();
        teacher_upload_grades_fragment=new Teacher_Upload_Grades_fragment();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mTeacherNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.FTBottomnav_Upload_attendance_button:
                        //mMainNavView.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(teacher_upload_attendance_fragment);
                        return true;
                    case R.id.FTBottomnav_UploadGrades:
                        //mMainNavView.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(teacher_upload_grades_fragment);
                        return true;
                    case R.id.FTBottomnav_Check_assignments:
                        //mMainNavView.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(teacher_check_assignments_fragment);
                        return true;
                    default: return false;
                }
            }
        });
        if (savedInstanceState == null) {
           mTeacherNavView.setSelectedItemId(R.id.FTBottomnav_UploadGrades); // change to whichever id should be default
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
        getMenuInflater().inflate(R.menu.home__faculty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
            //return true;
        //}

        //return super.onOptionsItemSelected(item);
    return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment frag1=null;
        if (id == R.id.FTdrawer_AddStudent_button) {
            frag1=new Teacher_add_student_fragment();
            // Handle the camera action
        } else if (id == R.id.FTdrawer_Editinfo_button) {
            frag1=new Teacher_Editinfo_fragment();

        } else if (id == R.id.FTdrawer_addNotice_button) {
            frag1=new Teacher_Add_Notice_fragment();

        }
        else if (id == R.id.FTdrawer_Noticeboard_button_button) {
            frag1=new Student_notice_fragment();

        }else if (id == R.id.FTdrawer_Logout_button) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this,Welcome.class));
            finish();
            return true;

        } else if (id == R.id.FTdrawer_Settings_button) {

        } else if (id == R.id.FTdrawer_About_button) {
            frag1=new About_fragments();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.screenarea1, frag1).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.screenarea1,fragment);
        fragmentTransaction.commit();
    }
}
