package com.example.matbkhe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by YAT on 07/10/2017.
 */

public class Pageradapter extends FragmentStatePagerAdapter {

    int numtab;
    public Pageradapter( FragmentManager fm,int numtab) {
        super(fm);
        this.numtab=numtab;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DepartFragment tab1 = new DepartFragment();
                return tab1;
            case 1:
                LatestFragment tab2 = new LatestFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numtab;
    }
}
