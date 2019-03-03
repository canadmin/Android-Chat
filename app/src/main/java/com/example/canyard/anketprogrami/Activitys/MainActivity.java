package com.example.canyard.anketprogrami.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {
    private Button olustur;
    private Button giris;
    private FirebaseAuth mAuth;
    private EditText logMail, logPass;
    private ProgressDialog mLoginProgres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        olustur = (Button) findViewById(R.id.hesap_olustur);
        giris = (Button) findViewById(R.id.giris_yap);
        logMail = (EditText) findViewById(R.id.req_eposta);
        logPass = (EditText) findViewById(R.id.log_sifre);

        mLoginProgres = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kayit = new Intent(MainActivity.this, KayitActivity.class);
                startActivity(kayit);
                finish();
            }
        });


        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = logMail.getText().toString();
                String pass = logPass.getText().toString();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass)) {
                    mLoginProgres.setTitle("Giriş Yapılıyor");
                    mLoginProgres.setMessage("lütfen bekleyiniz!");
                    mLoginProgres.setCanceledOnTouchOutside(false);
                    mLoginProgres.show();
                    kullaniciGiris(email, pass);
                }
            }
        });



    }
    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();

        if(currentUser!=null){

            sendToStart();

        }
    }
    public void sendToStart(){

        Intent denemeint=new Intent(MainActivity.this,AppActivity.class);

        startActivity(denemeint);

        finish();
    }
    private void kullaniciGiris(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, " Giriş Yapılamadı", Toast.LENGTH_LONG).show();
                }



            }
        });
    }
}
