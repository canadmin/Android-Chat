package com.example.canyard.anketprogrami.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canyard.anketprogrami.Classes.Messages;
import com.example.canyard.anketprogrami.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private FirebaseUser uCurrentUser;

    private FirebaseAuth mAuth;
    private List<Messages> mMessageList;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);
        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;

        public CircleImageView profileImage;


        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);


        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        uCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String current_uid = uCurrentUser.getUid();
        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();

        if (from_user.equals(current_uid)) {
            params.gravity = Gravity.RIGHT;
            viewHolder.messageText.setLayoutParams(params);
            viewHolder.messageText.setBackgroundResource(R.drawable.karsi_baloncuk);
            viewHolder.messageText.setTextColor(Color.BLACK);
        } else {
            params.gravity = Gravity.LEFT;
            viewHolder.messageText.setLayoutParams(params);
            viewHolder.messageText.setBackgroundResource(R.drawable.benim_baloncuk);
            viewHolder.messageText.setTextColor(Color.BLACK);
            viewHolder.messageText.setGravity(Gravity.RIGHT | Gravity.END);
        }
        viewHolder.messageText.setText(c.getMessage());


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }
}


