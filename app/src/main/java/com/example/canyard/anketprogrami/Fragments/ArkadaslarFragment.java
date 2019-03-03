package com.example.canyard.anketprogrami.Fragments;
import com.example.canyard.anketprogrami.Activitys.AppActivity;
import com.example.canyard.anketprogrami.Activitys.ChatActivity;
import com.example.canyard.anketprogrami.Activitys.ProfilActivity;
import com.example.canyard.anketprogrami.Activitys.UserProfile;
import com.example.canyard.anketprogrami.Classes.Friends;
import com.example.canyard.anketprogrami.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ArkadaslarFragment extends Fragment {
    private RecyclerView arkadasListesi;
    private DatabaseReference mFriendsDatabase,mUserDatabase;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;

    private View mMainView;


    public ArkadaslarFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView=inflater.inflate(R.layout.fragment_arkadaslar,container,false);
        arkadasListesi=mMainView.findViewById(R.id.arkadaslar_listesi);
        arkadasListesi.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth=FirebaseAuth.getInstance();


        mCurrent_user_id=mAuth.getCurrentUser().getUid();

        mFriendsDatabase=FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);

        mUserDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabase.keepSynced(true);


        arkadasListesi.setHasFixedSize(true);
        arkadasListesi.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(mFriendsDatabase,Friends.class).build();

        FirebaseRecyclerAdapter<Friends,ArkadaslarViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, ArkadaslarViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ArkadaslarViewHolder holder, int position, @NonNull Friends model) {
                String kullanici_idleri=getRef(position).getKey();
                final String list_user_id=getRef(position).getKey();
                holder.user_single_status.setText(model.getDate());

                mUserDatabase.child(kullanici_idleri).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String ad=dataSnapshot.child("name").getValue().toString();
                        String profilFotosu=dataSnapshot.child("image").getValue().toString();


                        holder.user_single_name.setText(ad);
                        Picasso.with(getContext()).load(profilFotosu).into(holder.user_single_image);
                            
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[]=new CharSequence[]{"Profili Görüntüle","Mesaj Göndder"};
                                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                                builder.setTitle("İşlem Seç");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            Intent profileIntent=new Intent(getContext(),UserProfile.class);
                                            profileIntent.putExtra("user_id",list_user_id);
                                            startActivity(profileIntent);

                                        }
                                        if(which==1){
                                            Intent profileIntent1=new Intent(getContext(),ChatActivity.class);
                                            profileIntent1.putExtra("user_id",list_user_id);
                                            profileIntent1.putExtra("user_name",ad);


                                            startActivity(profileIntent1);
                                        }
                                    }
                                });
                                builder.show();
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
            public ArkadaslarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);
                ArkadaslarViewHolder viewHolder=new ArkadaslarViewHolder(view);
                return viewHolder;


            }
        };


        arkadasListesi.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public static class ArkadaslarViewHolder extends RecyclerView.ViewHolder{
        CircleImageView user_single_image;
        TextView user_single_name;
        TextView  user_single_status;
        public ArkadaslarViewHolder(View itemView) {
            super(itemView);
            user_single_name=itemView.findViewById(R.id.user_single_name);
            user_single_status=itemView.findViewById(R.id.user_single_status);
            user_single_image=itemView.findViewById(R.id.user_single_image);
        }
    }
}




