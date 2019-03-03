package com.example.canyard.anketprogrami.Activitys;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canyard.anketprogrami.Classes.Friends;
import com.example.canyard.anketprogrami.Classes.Users;
import com.example.canyard.anketprogrami.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullanicilarActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUserlist;
    private Picasso PP;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private EditText et;
    private ImageButton btn;
    private static boolean kullanici_varmi=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanicilar);

        mToolbar = (Toolbar) findViewById(R.id.asdfa);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn=findViewById(R.id.erisimara);
        et=findViewById(R.id.arancak_erisim);


        mUserlist=(RecyclerView)findViewById(R.id.user_recyler_view);
        mUserlist.setHasFixedSize(true);
        mUserlist.setLayoutManager(new LinearLayoutManager(this));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arancak_ersisim=et.getText().toString();
                Ara(arancak_ersisim);
            }
        });
    }

    public void Ara(final String Aranacak_erisim){
        mUsersDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        kullanici_varmi=false;
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabase.orderByChild("accessCode").equalTo(Aranacak_erisim), Users.class).build();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                options) {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull Users model) {
                String kullanici_idleri=getRef(position).getKey();
                final String list_user_id=getRef(position).getKey();
                    kullanici_varmi=true;
                    final String profilFotosu = model.getImage();
//
                    Picasso.with(getApplicationContext()).load(model.getImage()).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.person).into(holder.user_single_image1, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getApplicationContext()).load(profilFotosu).into(holder.user_single_image1);
                        }
                    });
                    holder.user_single_status1.setText(model.getStatus());
                    holder.user_single_name1.setText(model.getName());




            }


            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_single, parent, false);
                        UsersViewHolder viewHolder = new UsersViewHolder(v);
                        return viewHolder;


            }
        };

        mUserlist.setAdapter(firebaseRecyclerAdapter1);
        firebaseRecyclerAdapter1.startListening();

    }
    public void Bitir(){
        Toast.makeText(this,"Bitti",Toast.LENGTH_SHORT).show();

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(mUsersDatabase, Users.class).build();

        FirebaseRecyclerAdapter<Users, UsersViewHolder> firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                options) {
            @Override
            protected void onBindViewHolder(@NonNull final UsersViewHolder holder, int position, @NonNull Users model) {
                String kullanici_idleri=getRef(position).getKey();
                final String list_user_id=getRef(position).getKey();
                String profilFotosu=model.getImage();
                Picasso.with(getApplicationContext()).load(profilFotosu).into(holder.user_single_image1);

                holder.user_single_status1.setText(model.getStatus());
                holder.user_single_name1.setText(model.getName());
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_single,parent,false);
                UsersViewHolder viewHolder=new UsersViewHolder(v);
                return viewHolder;
            }
        };

        mUserlist.setAdapter(firebaseRecyclerAdapter1);
        firebaseRecyclerAdapter1.startListening();
    }
*/
    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        CircleImageView user_single_image1;
        TextView user_single_name1;
        TextView user_single_status1;

        public UsersViewHolder(View itemView) {
            super(itemView);
            user_single_name1 = itemView.findViewById(R.id.user_single_name1);
            user_single_status1 = itemView.findViewById(R.id.user_single_status1);
            user_single_image1 = itemView.findViewById(R.id.user_single_image1);
        }
    }
}
