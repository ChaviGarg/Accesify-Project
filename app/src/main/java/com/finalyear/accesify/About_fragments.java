package com.finalyear.accesify;


import android.content.Intent;
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
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class About_fragments extends Fragment {
    private RecyclerView mUserslist;
    private Toolbar mToolbar;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;


    public About_fragments() {
        // Required empty public constructor


    }

    //testdev27
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mLayoutManager = new LinearLayoutManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        mUserslist = v.findViewById(R.id.users_recycler);
        mUserslist.setHasFixedSize(true);
        mUserslist.setLayoutManager(mLayoutManager);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        {
            Query query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Students")
                    .limitToLast(50);

            FirebaseRecyclerOptions<Users> options =
                    new FirebaseRecyclerOptions.Builder<Users>()
                            .setQuery(query, Users.class)
                            .build();
            FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Users, About_fragments.UserViewHolder>(options) {
                @Override
                public About_fragments.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.student_lists_layout, parent, false);

                    return new About_fragments.UserViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(About_fragments.UserViewHolder holder, int position, Users model) {
                    // Bind the Chat object to the ChatHolder
                    holder.setRollno(model.roll_number);
                    holder.setName(model.name);
                   holder.setImage(model.thumb_image);
                    final String user_id = getRef(position).getKey();


                    // ...
                }

            };
            mUserslist.setAdapter(adapter);
            adapter.startListening();
        }
    }
    public static   class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView userNameView = (TextView) mView.findViewById(R.id.student_displayname);
            userNameView.setText(name);
        }

        public void setRollno(String rollno) {
            TextView userStatusView = (TextView)
                    mView.findViewById(R.id.student_Rollno_ID);
            userStatusView.setText(rollno);

        }
        public void setImage(String thumb_image){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.student_imageID);
            Picasso.get().load(thumb_image).placeholder(R.drawable.contact_avatar).into(userImageView);

        }
    }
}