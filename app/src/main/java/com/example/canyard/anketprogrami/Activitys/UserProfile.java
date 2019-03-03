package com.example.canyard.anketprogrami.Activitys;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canyard.anketprogrami.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private Toolbar mToolbar;
    private CircleImageView mProfileImage;
    private TextView mProfileName, mProfileStatus, accesCodePa;
    private Button mProfileSendReqBtn, mDeclineBtn,bittipl;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;

    private String mCurrent_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mToolbar = (Toolbar) findViewById(R.id.user_singletool);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        final String user_id = getIntent().getStringExtra("user_id");

        mRootRef = FirebaseDatabase.getInstance().getReference();
/*
    <color name="colorPrimary">#345D7E</color>
    <color name="colorPrimaryDark">#F27281</color>
    <color name="colorAccent">#F8B195</color>
 */
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mProfileImage = (CircleImageView) findViewById(R.id.ppimage);
        mProfileName = (TextView) findViewById(R.id.display_name_profile);
        mProfileStatus = (TextView) findViewById(R.id.kullanici_durum);
        mProfileSendReqBtn = (Button) findViewById(R.id.add_friend);
        mDeclineBtn = (Button) findViewById(R.id.sil);

        accesCodePa=(TextView)findViewById(R.id.accessCodepp);


        mCurrent_state = "not_friends";

        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);


        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String erisimkodu = dataSnapshot.child("accessCode").getValue().toString();

                mProfileName.setText(display_name);
                mProfileStatus.setText(status);
                accesCodePa.setText(erisimkodu);


                Picasso.with(UserProfile.this).load(image).placeholder(R.drawable.person).into(mProfileImage);

                if (mCurrent_user.getUid().equals(user_id)) {


                    mProfileSendReqBtn.setEnabled(false);
                    mProfileSendReqBtn.setVisibility(View.INVISIBLE);

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                }
        });


            }




    }

