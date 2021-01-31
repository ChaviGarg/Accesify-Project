package com.finalyear.accesify;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class Teacher_Upload_Attendance_fragment extends Fragment {
Button upload;
    DatePicker datePicker;

    public Teacher_Upload_Attendance_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_teacher__upload__attendance, container, false);
        upload = rootView.findViewById(R.id.upload);
        datePicker=rootView.findViewById(R.id.dp);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  mon= String.valueOf(datePicker.getMonth());
                String year = String.valueOf(datePicker.getYear());
                String day = String.valueOf(datePicker.getDayOfMonth());
                Intent ob = new Intent(getActivity(),Count_attendance.class);
                ob.putExtra("date", String.format("%s%s%s",day,mon,year));
                startActivity(ob);
            }
        });

        return rootView;
    }


}
