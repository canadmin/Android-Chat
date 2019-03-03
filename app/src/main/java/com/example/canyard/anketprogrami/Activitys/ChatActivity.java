package com.example.canyard.anketprogrami.Activitys;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canyard.anketprogrami.Classes.Messages;
import com.example.canyard.anketprogrami.R;
import com.example.canyard.anketprogrami.adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageList;


    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager mlinearLayout;

    private DatabaseReference mMessageDatabase;

    private String mchatUser;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private TextView mTitleView,seen;
    private CircleImageView mProfileImage;

    private String mCurrentUserId;

    private MessageAdapter mAdapter;
    private Toolbar mChatToolBar;
    private DatabaseReference mRootRef;
    private String userName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatToolBar=(Toolbar)findViewById(R.id.chat_app_bar);
        setSupportActionBar(mChatToolBar);


        ActionBar actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        mRootRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        mchatUser=getIntent().getStringExtra("user_id");

        userName=getIntent().getStringExtra("user_name");
        getSupportActionBar().setTitle(userName);

        mCurrentUserId=mAuth.getCurrentUser().getUid();


        //****

        getSupportActionBar().setTitle(userName);
        LayoutInflater inflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=inflater.inflate(R.layout.toolbar_layout,null);

        actionBar.setCustomView(action_bar_view);

        mChatSendBtn=(ImageButton)findViewById(R.id.chat_send_btn);
        mChatMessageView=(EditText)findViewById(R.id.chat_message_view);
        mMessageList=(RecyclerView)findViewById(R.id.messages_list);
        mlinearLayout=new LinearLayoutManager(this);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(mlinearLayout);

        mAdapter=new MessageAdapter(messagesList);


        //****
        mTitleView=(TextView)findViewById(R.id.chat_bar_title);
        seen=(TextView)findViewById(R.id.last_seen_bar);

        mMessageList.setAdapter(mAdapter);

        loadMessages();
        mTitleView.setText(userName);

        mRootRef.child("Users").child(mchatUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String online=dataSnapshot.child("online").getValue().toString();
                String image=dataSnapshot.child("image").toString();
                if(online.equalsIgnoreCase("true")){
                    seen.setText("Çevrimiçi");
                }else{
                    seen.setText("Çevrimdısi");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRootRef.child("Chat").child(mCurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(mchatUser)){
                    Map chatAddMap=new HashMap();
                    chatAddMap.put("seen",false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);


                    Map chatUserMap=new HashMap();
                    chatUserMap.put("Chat/"+mCurrentUserId+"/"+mchatUser,chatAddMap);
                    chatUserMap.put("Chat/"+mchatUser+"/"+mCurrentUserId,chatAddMap);


                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError!=null) {

                                Log.d("CHAT LOG", databaseError.getMessage().toString());
                            }

                        }
                    });
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();

            }
        });



    }
    private void loadMessages() {
        mRootRef.child("messages").child(mCurrentUserId).child(mchatUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Messages message1=dataSnapshot.getValue(Messages.class);
                messagesList.add(message1);
                mAdapter.notifyDataSetChanged();
                mMessageList.smoothScrollToPosition(mMessageList.getAdapter().getItemCount());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void sendMessage(){
        String message=mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){

            String current_user_ref="messages/"+mCurrentUserId+ "/" +mchatUser;
            String chat_user_Ref="messages/"+mchatUser+"/"+mCurrentUserId;

            DatabaseReference user_message_push=mRootRef.child("messages").child(mCurrentUserId).child(mchatUser).push();
            String push_id=user_message_push.getKey();

            Map messageMap=new HashMap();
            messageMap.put("message",message);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time",ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);


            Map messageUserMap=new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageUserMap.put(chat_user_Ref+"/"+push_id,messageMap);


            mChatMessageView.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError!=null) {

                        Log.d("CHAT LOG", databaseError.getMessage().toString());
                    }
                }
            });



        }


    }
}
