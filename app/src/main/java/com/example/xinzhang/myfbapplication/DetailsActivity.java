package com.example.xinzhang.myfbapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.LoginAuthorizationType;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "detailss";
    public final static String fb_MESSAGE = "com.example.myfirstapp.fbfb";
    String resultInfo;
    String posterName;
    String proUrl;
    public static String detailUrl;
    public static String detailContent;
    public static String type;
    public static String FavKey;
    String id;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    TabLayout tabLayout;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private void showNoAlbums(){
        TextView textViewNoAlbums = (TextView) findViewById(R.id.textViewNoAlbums);
        textViewNoAlbums.setText("No albums available to display");
        return;
    }

    private void showNoPosts(){
        TextView textViewNoPosts = (TextView) findViewById(R.id.textViewNoPosts);
        textViewNoPosts.setText("No posts available to display");
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, " ============== ");
        setContentView(R.layout.activity_det);
        final int[] detIcon = new int[]{
                R.drawable.albums,
                R.drawable.posts
        };
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        resultInfo = intent.getStringExtra(resultActivity.RES_MESSAG);
        String[] a = resultInfo.split("\\|");
        id = resultInfo.split("\\|")[0];
        proUrl = resultInfo.split("\\|")[1];
        posterName = resultInfo.split("\\|")[2];
        type = resultInfo.split("\\|")[3];
        FavKey = id+"|"+type;
        Log.i(TAG, "resultInfo:"+resultInfo+"  id:" +id);
        detailUrl = "http://lowcost-env-hw9.gzaeh2m7fd.us-west-1.elasticbeanstalk.com/index.php?detail="+id+"&hi_img=true";
        Log.i(TAG, " detail url:"+detailUrl);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        for (int i=0; i < tabLayout.getTabCount(); i++)
        {
            tabLayout.getTabAt(i).setIcon(detIcon[i]);
        }

        DetailsActivity.DownloadWebPageTask task = new DetailsActivity.DownloadWebPageTask();
        task.execute(new String[] {detailUrl});
    }


    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            //resultqqq = (TextView) findViewById(R.id.textView5);
            Request request = new Request.Builder().url(urls[0]).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                Log.i(TAG, "detail: succ");
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
            List<PostItem> postList = new ArrayList<>();
            List<String> albumsHeader = new ArrayList<>();
            HashMap<String,List<String>> hashAlbums = new HashMap<>();
            detailContent = result.trim();
            Log.i(TAG, "all detail: ="+detailContent+"=" +" leng:"+ detailContent.length());
            if(detailContent.length()!=0){
//                Log.i(TAG, " detail not null : " + detailContent.length());
//                JSONObject detJson;
                try {
                    JSONObject detJson = new JSONObject(detailContent);
//                    String a = detJson.getString("posts");
//                    Log.i(TAG, " a:" +a);
                    if(!detJson.has("posts")){
                        showNoPosts();
                    }else{
                        JSONObject postJson = detJson.getJSONObject("posts");
                        if(!postJson.has("data")){
                            showNoPosts();
                        }else{
                            JSONArray postArr = postJson.getJSONArray("data");
                            for(int i = 0; i < postArr.length();i++){
                                JSONObject onePostJson = postArr.getJSONObject(i);
                                if(onePostJson.has("message")){
                                    String postContent = onePostJson.getString("message");
                                    String postTime = onePostJson.getString("created_time").replace("T"," ").replace("+0000","");
                                    postList.add(new PostItem(proUrl,posterName,postTime,postContent));
                                }
                            }
                            ListView listviewPost = (ListView) findViewById(R.id.listviewPost1);
                            PostItemAdapter postItemAdapter = new PostItemAdapter(getApplicationContext(), postList);
                            listviewPost.setAdapter(postItemAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject detJson = new JSONObject(detailContent);
                    if(!detJson.has("albums")){
                        showNoAlbums();
                    }else{
                        JSONObject albumsJson = detJson.getJSONObject("albums");
                        if(albumsJson.has("data")){
                            Log.i(TAG, "has data");
                            if(albumsJson.getString("data") == "null"){
                                showNoAlbums();
                            }else{
                                JSONArray albumsArr = albumsJson.getJSONArray("data");
                                Log.i(TAG, "ee");
                                for(int i = 0; i < albumsArr.length();i++){
                                    JSONObject oneAlbumsJson = albumsArr.getJSONObject(i);
                                    String albumsName = oneAlbumsJson.getString("name");
                                    JSONObject twoPhotos = oneAlbumsJson.getJSONObject("photos");
                                    JSONArray twoPh = twoPhotos.getJSONArray("data");
                                    String photo1ID = twoPh.getJSONObject(0).getString("picture");
                                    String photo2ID = twoPh.getJSONObject(1).getString("picture");
                                    albumsHeader.add(albumsName);
                                    List<String> value = new ArrayList<String>();
                                    value.add(photo1ID);
                                    value.add(photo2ID);
                                    hashAlbums.put(albumsName, value);
                                }
                                ExpandableListView AlbumsList = (ExpandableListView) findViewById(R.id.exListView);
                                AlbumsItemAdapter albumItemAdapter = new AlbumsItemAdapter(getApplicationContext(),albumsHeader,hashAlbums);
                                AlbumsList.setAdapter(albumItemAdapter);
                            }

                        }else{
                            Log.i(TAG, "dd");
                            showNoAlbums();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else{
                Log.i(TAG, " detail null, add text ");
                showNoAlbums();
                showNoPosts();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences myPreference=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        if(!myPreference.contains(FavKey)){
            Log.i(TAG, "onCreateOptionsMenu: "+"........."+FavKey);
            menu.add(1,2131558641,0,"Add to Favorites");
        }else{
            menu.add(1,2131558641,0,"Remove from Favorites");
        }
        menu.add(1,2131558642,0,"Share");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idid = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: "+idid);

        switch (idid){
            case 2131558641:
                SharedPreferences myPreference=getSharedPreferences("myPreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = myPreference.edit();
                if(!myPreference.contains(FavKey)){
                    editor.putString(FavKey, resultInfo);
                    editor.commit();
                    Toast.makeText(this,"Added to Favorites!",Toast.LENGTH_SHORT).show();
                    item.setTitle("Remove from Favorites");
                }else{
                    editor.remove(FavKey);
                    editor.commit();
                    Toast.makeText(this,"Removed from Favorites!",Toast.LENGTH_SHORT).show();
                    item.setTitle("Add to Favorites");
                }
                break;
            case 2131558642:
                Toast.makeText(getApplicationContext(),"Sharing "+posterName+"!!",Toast.LENGTH_SHORT).show();
                callbackManager = CallbackManager.Factory.create();
                shareDialog = new ShareDialog(this);
                if (ShareDialog.canShow(ShareLinkContent.class)) {

                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(posterName)
                            .setContentDescription(
                                    "FB SEARCH FROM CSCI571...")
                            .setContentUrl(Uri.parse("https://developers.facebook.com/andriod"))
                            .setImageUrl(Uri.parse(proUrl))
                            .build();
                    shareDialog.show(linkContent);
                }

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.e("FB", "success");
                        int tabposition = tabLayout.getSelectedTabPosition();
                        if(tabposition == 0){
                            Toast.makeText(getApplicationContext(),"You shared this albums.",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"You shared this posts.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.e("FB", "cancel");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Log.e("FB", "error");
                    }

                });

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }





    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Ablums ablumsTab = new Ablums();
                    return ablumsTab;
                case 1:
                    Posts postsTab = new Posts();
                    return postsTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ablums";
                case 1:
                    return "Posts";

            }
            return null;
        }
    }
}
