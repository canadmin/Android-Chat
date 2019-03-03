package com.example.canyard.anketprogrami.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.canyard.anketprogrami.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KayitActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText kullaniciAdi,eMail,sifre;
    private Button yarat;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mRegProgres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);
        mAuth = FirebaseAuth.getInstance();
        mRegProgres=new ProgressDialog(this);

        kullaniciAdi=findViewById(R.id.req_ad);
        eMail=findViewById(R.id.req_eposta);
        sifre=findViewById(R.id.req_sifre);
        yarat=(Button)findViewById(R.id.yarat);
        yarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String elecmail=eMail.getText().toString();
                String ad=kullaniciAdi.getText().toString();
                String pass=sifre.getText().toString();


                if(!TextUtils.isEmpty(ad) || !TextUtils.isEmpty(elecmail) || !TextUtils.isEmpty(pass)) {
                    mRegProgres.setTitle("Hesap oluştur");
                    mRegProgres.setMessage("Hesabınız oluşturuluyor lütfen bekleyiniz");
                    mRegProgres.setCanceledOnTouchOutside(false);
                    mRegProgres.show();
                    register_user(ad, elecmail, pass);
                }
            }
        });
    }
    private void register_user(final String dName, String elecmail, String pass) {
        mAuth.createUserWithEmailAndPassword(elecmail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();

                            mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            HashMap<String,String> usermap=new HashMap<>();
                            usermap.put("name",dName);
                            usermap.put("status","Merhaba millet ben de sizin gibi can'ın kölesiyim");
                            usermap.put("accessCode","");
                            usermap.put("image","https://firebasestorage.googleapis.com/v0/b/pmapp2-2b2ca.appspot.com/o/profile_images%2Fimages.jpg?alt=media&token=6f07d569-d18d-4130-af10-73c9f084a365");
                            usermap.put("thumb_image","default");
                            mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgres.dismiss();
                                        Intent mainIntent=new Intent(KayitActivity.this,MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                }
                            });
                            /* */
                        } else{
                            mRegProgres.hide();

                            Toast.makeText(KayitActivity.this," başarısız işlem",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });



    }
}
