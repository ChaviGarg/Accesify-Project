package com.finalyear.accesify;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class Student_notice_fragment extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;


    public Student_notice_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Notice");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_student_notice, container, false);
        mUserslist = v.findViewById(R.id.notice_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);
        return v;
        // Inflate the layout for this fragment

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Notice")
                    .orderByChild("time")
                    .limitToFirst(50);

            FirebaseRecyclerOptions<Users> options =
                    new FirebaseRecyclerOptions.Builder<Users>()
                            .setQuery(query, Users.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, Student_notice_fragment.UserViewHolder>(options) {
                @Override
                public Student_notice_fragment.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.teacher_notice_list_layout, parent, false);

                    return new Student_notice_fragment.UserViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(Student_notice_fragment.UserViewHolder holder, int position, final Users model) {
                    // Bind the Chat object to the ChatHolder

                    holder.setInfo(model.info);
                    holder.setName(model.name);
                    holder.setNotice(model.notice);
                    final String user_id = getRef(position).getKey();



                }

            };
            mUserslist.setAdapter(adapter);
            adapter.startListening();
        }
    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button button;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            button=mView.findViewById(R.id.upload_marks);
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.teacher_name);
            userNameView.setText(name);
        }

        public void setInfo(String info) {
            TextView userStatusView = (TextView)
                    mView.findViewById(R.id.teachers_notice_name);
            userStatusView.setText(info);

        }
        public void setNotice(String notice){

            ImageView userImageView = mView.findViewById(R.id.teacher_imageview);
            Picasso.get().load(notice).into(userImageView);

        }
    }
}
