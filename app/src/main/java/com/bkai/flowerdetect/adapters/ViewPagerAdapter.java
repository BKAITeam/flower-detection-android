package com.bkai.flowerdetect.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bkai.flowerdetect.models.Flower;
import com.bkai.flowerdetect.views.fragments.CoTichFragment;
import com.bkai.flowerdetect.views.fragments.ScienceFragment;

/**
 * Created by marsch on 5/4/17.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, int mNumbOfTabsumb) {
        super(fm);
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                CoTichFragment coTichFragment = new CoTichFragment();
                return coTichFragment;
            case 1:
                ScienceFragment scienceFragment = new ScienceFragment();

                return scienceFragment;
            default: CoTichFragment ok = new CoTichFragment();
                return ok;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
