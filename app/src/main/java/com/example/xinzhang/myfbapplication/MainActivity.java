package com.example.xinzhang.myfbapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.facebook.FacebookSdk;
import static com.example.xinzhang.myfbapplication.R.id.textView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public final static String RES_MESSAGE = "com.example.myfirstapp.passResJson";
    private static final String TAG = "kw" ;
    public final static ArrayList<ResultRow> userListf = new ArrayList<>();
    public final static ArrayList<ResultRow> pageListf = new ArrayList<>();
    public final static ArrayList<ResultRow> eventListf = new ArrayList<>();
    public final static ArrayList<ResultRow> placeListf = new ArrayList<>();
    public final static ArrayList<ResultRow> groupListf = new ArrayList<>();
    HomeFragment homeFragment;
    EditText editKeyword;

    private LocationManager locationManager;
    LocationListener mLocationListener;
    private String locationProvider;
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    EditText test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commit();

        getLocation();

        SharedPreferences myPreference=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        HashMap<String, String> favMap = (HashMap<String, String>) myPreference.getAll();
        userListf.clear();
        pageListf.clear();
        eventListf.clear();
        placeListf.clear();
        groupListf.clear();
        for(String key : favMap.keySet()){
            Log.i(TAG, "key: "+key);
            String id = key.split("\\|")[0];
            String type = key.split("\\|")[1];
            String rowinfo = favMap.get(key);
            Log.i(TAG, "value: "+rowinfo);
            String proUrl = rowinfo.split("\\|")[1];
            String posterName = rowinfo.split("\\|")[2];

            if(type.equals("User")){
                userListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Page")){
                pageListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Event")){
                eventListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Place")){
                placeListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Group")){
                groupListf.add(new ResultRow(id, proUrl, posterName, type));
            }
        }
    }

    public void getLocation() {
        locationManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location loc) {
                if (loc != null) {
                    setLatitude(loc.getLatitude());
                    setLongitude(loc.getLongitude());
                } else {
                    Toast.makeText(getApplicationContext(), "Your current location is temporarily unavailable.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            public void onProviderDisabled(final String s) {
                Toast.makeText(getApplicationContext(), "onProviderDisabled",
                        Toast.LENGTH_SHORT).show();
            }

            public void onProviderEnabled(final String s) {
                Toast.makeText(getApplicationContext(), "onProviderEnabled",
                        Toast.LENGTH_SHORT).show();
            }

            public void onStatusChanged(final String s, final int i, final Bundle b) {
                Toast.makeText(getApplicationContext(), "onStatusChanged",
                        Toast.LENGTH_SHORT).show();
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        Location lastKnownLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }
        if (!locationProvider.equals("")) {
            locationManager.requestLocationUpdates(
                    locationProvider, 1000, 1, mLocationListener);
        }
        if (lastKnownLocation != null) {
            setLatitude(lastKnownLocation.getLatitude());
            Log.i(TAG, "last location "+lastKnownLocation.getLatitude());
            setLongitude(lastKnownLocation.getLongitude());
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册监听
        if (locationManager != null) {
            locationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resultActivity.resContent = null;
        resultActivity.resUserContent=null;
        resultActivity.resPageContent=null;
        resultActivity.resEventContent=null;
        resultActivity.resPlaceContent=null;
        resultActivity.resGroupContent=null;
        SharedPreferences myPreference=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        HashMap<String, String> favMap = (HashMap<String, String>) myPreference.getAll();
        userListf.clear();
        pageListf.clear();
        eventListf.clear();
        placeListf.clear();
        groupListf.clear();
        for(String key : favMap.keySet()){
            Log.i(TAG, "key: "+key);
            String id = key.split("\\|")[0];
            String type = key.split("\\|")[1];
            String rowinfo = favMap.get(key);
            Log.i(TAG, "value: "+rowinfo);
            String proUrl = rowinfo.split("\\|")[1];
            String posterName = rowinfo.split("\\|")[2];

            if(type.equals("User")){
                userListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Page")){
                pageListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Event")){
                eventListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Place")){
                placeListf.add(new ResultRow(id, proUrl, posterName, type));
            }else if(type.equals("Group")){
                groupListf.add(new ResultRow(id, proUrl, posterName, type));
            }
        }

        if (!locationProvider.equals("")) {
            // 当GPS定位时，在这里注册requestLocationUpdates监听就非常重要而且必要。没有这句话，定位不能成功。
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

            }
            locationManager.requestLocationUpdates(locationProvider, 1000, 1,
                    mLocationListener);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, homeFragment).commit();
            setTitle("Search on FB");
        } else if (id == R.id.nav_gallery) {
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, favoriteFragment).commit();
            setTitle("Favorites");
        }  else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, aboutMeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void search(View view) {
        editKeyword = (EditText) findViewById(R.id.keywordEditText);
        System.out.println("----"+editKeyword);
        String keyword = editKeyword.getText().toString();
        if(keyword == null || keyword.length() == 0){
            Toast.makeText(this,"Please enter a keyword!",Toast.LENGTH_SHORT).show();
        }else{
            Log.i(TAG, "search: " + keyword);
            Intent intent = new Intent(this, resultActivity.class);
            intent.putExtra(RES_MESSAGE, keyword);
            startActivity(intent);

        }
    }

    public void clear(View view) {
        editKeyword.setText("");
    }



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

