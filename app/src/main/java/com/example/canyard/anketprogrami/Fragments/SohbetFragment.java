package com.example.canyard.anketprogrami.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.canyard.anketprogrami.Activitys.AyarlarActivity;
import com.example.canyard.anketprogrami.Activitys.ChatActivity;
import com.example.canyard.anketprogrami.Activitys.ProfilActivity;
import com.example.canyard.anketprogrami.Classes.Messages;
import com.example.canyard.anketprogrami.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SohbetFragment extends Fragment {
    private RecyclerView sohbetListesi;
    private DatabaseReference mFriendsDatabase,mUserDatabase,lastMessages;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    private View mMainView;
    public SohbetFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView= inflater.inflate(R.layout.fragment_sohbet, container, false);
        sohbetListesi=mMainView.findViewById(R.id.sohbet_list);
        sohbetListesi.setLayoutManager(new LinearLayoutManager(getContext()));


        mAuth=FirebaseAuth.getInstance();
        mCurrent_user_id=mAuth.getCurrentUser().getUid();

        mFriendsDatabase= FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);

        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);




        sohbetListesi.setHasFixedSize(true);
        sohbetListesi.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(mFriendsDatabase,Messages.class).build();

        FirebaseRecyclerAdapter<Messages,chatViewHolder>chatadapter=new FirebaseRecyclerAdapter<Messages, chatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final chatViewHolder holder, int position, @NonNull Messages model) {

                final String list_user_id=getRef(position).getKey();

                mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                      final   String ad=dataSnapshot.child("name").getValue().toString();
                      final  String profilFotosu=dataSnapshot.child("image").getValue().toString();

                        holder.chat_single_name.setText(ad);
                        Picasso.with(getContext()).load(profilFotosu).into(holder.chat_image);

                        holder.chat_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chatIntent=new Intent(getContext(),ChatActivity.class);
                                    chatIntent.putExtra("user_id",list_user_id);
                                    chatIntent.putExtra("user_name",ad);

                                startActivity(chatIntent);
                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_user_single_layout,parent,false);
                SohbetFragment.chatViewHolder viewHolder=new SohbetFragment.chatViewHolder(view);
                return viewHolder;

            }
        };
        sohbetListesi.setAdapter(chatadapter);
        chatadapter.startListening();

    }

    public static class chatViewHolder extends RecyclerView.ViewHolder{
        TextView chat_single_name;
        CircleImageView chat_image;
        public chatViewHolder(View itemView) {
            super(itemView);
            chat_single_name=itemView.findViewById(R.id.chat_single_name);
            chat_image=itemView.findViewById(R.id.chat_single_image);
        }
    }

    }



