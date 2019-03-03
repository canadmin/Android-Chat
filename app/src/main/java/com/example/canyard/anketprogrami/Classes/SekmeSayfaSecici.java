package com.example.canyard.anketprogrami.Classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.canyard.anketprogrami.Fragments.ArkadaslarFragment;
import com.example.canyard.anketprogrami.Fragments.SohbetFragment;

public class SekmeSayfaSecici extends FragmentPagerAdapter {


    public SekmeSayfaSecici(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SohbetFragment sohbetlerFragment = new SohbetFragment();
                return sohbetlerFragment;
            case 1:
                ArkadaslarFragment arkadaslarFragment = new ArkadaslarFragment();
                return arkadaslarFragment;
            default:
                return null;
        }
        }

        @Override
        public int getCount () {
            return 2;
        }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Sohbetler";
            case 1:
                return "Arkadaşlar";
            default:
                return null;
        }
    }
}
