package com.example.xinzhang.myfbapplication;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private resultActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    View rootView;
    private ViewPager mViewPager;
    final int[] resIcon = new int[]{
            R.drawable.users,
            R.drawable.pages,
            R.drawable.events,
            R.drawable.places,
            R.drawable.groups
    };
    TabLayout tabLayout;
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
//         Inflate the layout for this fragment
        ArrayList<Fragment> fragmentList;
        fragmentList = new ArrayList<>();
        fragmentList.add(new FavUser());
        fragmentList.add(new FavPage());
        fragmentList.add(new FavEvent());
        fragmentList.add(new FavPlace());
        fragmentList.add(new FavGroup());
        mSectionsPagerAdapter = new resultActivity.SectionsPagerAdapter(getChildFragmentManager(), fragmentList);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) rootView.findViewById(R.id.containerf);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabsf);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i=0; i < tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(i).setIcon(resIcon[i]);
        }

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();


    }
}
