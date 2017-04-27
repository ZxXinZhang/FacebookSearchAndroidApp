package com.example.xinzhang.myfbapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.internal.LoginAuthorizationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xinzhang.myfbapplication.MainActivity.latitude;
import static com.example.xinzhang.myfbapplication.MainActivity.longitude;
import static com.facebook.FacebookSdk.getApplicationContext;

public class resultActivity extends AppCompatActivity {
    public final static String RES_MESSAG = "idd";
    private static final String TAG = "res";
    public static String resContent;
    public static String resUserContent;
    public static String resPageContent;
    public static String resEventContent;
    public static String resPlaceContent;
    public static String resGroupContent;
    final int[] resIcon = new int[]{
            R.drawable.users,
            R.drawable.pages,
            R.drawable.events,
            R.drawable.places,
            R.drawable.groups
    };
    TabLayout tabLayout;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String url = "http://lowcost-env-hw9.gzaeh2m7fd.us-west-1.elasticbeanstalk.com/index.php?keyword=";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ArrayList<Fragment> fragmentList;
        fragmentList = new ArrayList<>();
        fragmentList.add(new resUser());
        fragmentList.add(new resPage());
        fragmentList.add(new resEvent());
        fragmentList.add(new resPlace());
        fragmentList.add(new resGroup());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentList);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(resIcon[i]);
        }

        Intent intent = getIntent();
        String keyword = intent.getStringExtra(MainActivity.RES_MESSAGE);
        resultActivity.DownloadWebPageTask task = new resultActivity.DownloadWebPageTask();
        url += keyword;
        Log.i(TAG, "location"+latitude+" "+longitude);
        url += "&latitude="+latitude+"&longitude="+longitude;
        task.execute(new String[]{url});
        ActionBar supportedActionBar = getSupportActionBar();
        if (supportedActionBar != null) {
            supportedActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }






    @Override
    protected void onResume() {

        super.onResume();
        if(resContent != null){
            ArrayList<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(new resUser());
            fragmentList.add(new resPage());
            fragmentList.add(new resEvent());
            fragmentList.add(new resPlace());
            fragmentList.add(new resGroup());
            mSectionsPagerAdapter.setFragments(fragmentList);
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(resIcon[i]);
            }
        }




    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
//            Log.i(TAG, "onPostExecute: aaaa");
            Request request = new Request.Builder().url(urls[0]).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
//                Log.i(TAG, "onPostExecute: succ");
                try {
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Download failed";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "all res: " + result);
            resContent = result;
            JSONObject resJson = null;
            ListView listview = null;
            final List<ResultRow> userList = new ArrayList<>();
            if (resContent != null) {
                try {
                    resJson = new JSONObject(resContent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject userObj = null;
                try {
                    userObj = resJson.getJSONObject("users");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                resUserContent = userObj.toString();


                JSONObject pageObj = null;
                try {
                    pageObj = resJson.getJSONObject("pages");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                resPageContent = pageObj.toString();
                JSONArray pageResArray = null;


                JSONObject eventObj = null;
                try {
                    eventObj = resJson.getJSONObject("events");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                resEventContent = eventObj.toString();
//                    JSONArray eventResArray  = eventObj.getJSONArray("data");

                JSONObject palceObj = null;
                try {
                    palceObj = resJson.getJSONObject("places");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                resPlaceContent = palceObj.toString();
//                    JSONArray placeResArray  = palceObj.getJSONArray("data");

                JSONObject groupObj = null;
                try {
                    groupObj = resJson.getJSONObject("groups");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                resGroupContent = groupObj.toString();
//                    JSONArray groupResArray  = groupObj.getJSONArray("data");

            }
            ArrayList<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(new resUser());
            fragmentList.add(new resPage());
            fragmentList.add(new resEvent());
            fragmentList.add(new resPlace());
            fragmentList.add(new resGroup());
            mSectionsPagerAdapter.setFragments(fragmentList);
            for (int i=0; i < tabLayout.getTabCount(); i++)
            {
                tabLayout.getTabAt(i).setIcon(resIcon[i]);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private FragmentManager fm;
        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            this.fragments = fragments;
        }


        public void setFragments(ArrayList<Fragment> fragments) {
            if(this.fragments != null){
                FragmentTransaction ft = fm.beginTransaction();
                for(Fragment f:this.fragments){
                    ft.remove(f);
                }
                ft.commit();
                ft=null;
                fm.executePendingTransactions();
            }

            this.fragments = fragments;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        public Fragment getItem(int arg0) {
            return fragments.get(arg0);
        }


        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";

            }
            return null;
        }
    }



}
