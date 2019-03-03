package com.example.canyard.anketprogrami.Activitys;

import android.content.Intent;
import android.nfc.Tag;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.canyard.anketprogrami.Classes.SekmeSayfaSecici;
import com.example.canyard.anketprogrami.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private TabLayout mTablayaut;
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
   private NavigationView navigationView;

    private SekmeSayfaSecici sekmeSayfaSecici;

    private TextView ad,mail;
    private CircleImageView main_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);




       Toolbar mainTool=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mainTool);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mAuth = FirebaseAuth.getInstance();

      //  PreferenceActivity.Header header=new PreferenceActivity.Header();


        mViewPager=(ViewPager)findViewById(R.id.main_tabPager);
        sekmeSayfaSecici=new SekmeSayfaSecici(getSupportFragmentManager());
        mViewPager.setAdapter(sekmeSayfaSecici);
        mTablayaut=(TabLayout)findViewById(R.id.main_tabs);
        mTablayaut.setupWithViewPager(mViewPager);
        mDrawerLayout=findViewById(R.id.Drawer1);
        navigationView=findViewById(R.id.Drawer112);

        navigationView.setNavigationItemSelectedListener(this);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mainTool,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.Drawer1);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        switch (id){
            case R.id.menu_ayarlar:
                Intent ayarlarIntent=new Intent(AppActivity.this,AyarlarActivity    .class);
                startActivity(ayarlarIntent);
                break;
            case R.id.kullanicilar:
                Intent kullanicilar=new Intent(AppActivity.this,KullanicilarActivity.class);
                startActivity(kullanicilar);
                break;
            case R.id.uygulama_cikis:
                FirebaseAuth.getInstance().signOut();

                sendToStart();
                break;
        }




        return true;
    }
    public void sendToStart(){

        Intent denemeint=new Intent(AppActivity.this,MainActivity.class);

        startActivity(denemeint);

        finish();
    }

}
