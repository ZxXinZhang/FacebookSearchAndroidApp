package com.example.xinzhang.myfbapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xinzhang.myfbapplication.resultActivity.RES_MESSAG;
import static com.example.xinzhang.myfbapplication.resultActivity.resContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resGroupContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resPageContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resUserContent;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by xinzhang on 4/14/17.
 */

public class resGroup extends Fragment {
    private static final String TAG = "group";
    List<ResultRow> userList = new ArrayList<>();
    private String pre;
    private String next;
    JSONObject userObj = null;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.resgroup, container, false);

        if(resContent!=null){
            displayUser();
        }


        return rootView;
    }


    public void requestGroupContent(String url){
        resGroup.DownloadWebPage task = new resGroup.DownloadWebPage();
        task.execute(new String[] {url});
//        return;
    }

    private class DownloadWebPage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(urls[0]).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {

                try {
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "Download failed";
        }

        protected void onPostExecute(String result) {
            Log.i(TAG, "next!!!"+result);
            resGroupContent = result;
            displayUser();
        }
    }


    public void displayUser(){
        try {
            userObj = new JSONObject(resGroupContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray userResArray;
        try {
            userResArray  = userObj.getJSONArray("data");
            userList.clear();
            for(int i = 0; i < userResArray.length(); i++){
                JSONObject oneUser = userResArray.getJSONObject(i);
                String id = oneUser.getString("id");
                String name = oneUser.getString("name");
                String proUrl = oneUser.getJSONObject("picture").getJSONObject("data").getString("url");

                userList.add(new ResultRow(id,proUrl,name,"Group"));

            }

            pre = null;
            next = null;
            if(userObj.has("paging")){
                Log.i(TAG, "onCreateVi:"+"has paging");
                JSONObject pageObj = userObj.getJSONObject("paging");
                Log.i(TAG, "onCreateVi:"+"11111");
                if(pageObj.has("previous")){
                    pre = pageObj.getString("previous");
                }
                if(pageObj.has("next")){
                    next = pageObj.getString("next");
                }
                Log.i(TAG, "next page: "+next);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listview = (ListView)rootView.findViewById(R.id.listviewGroup);
        ResItemAdapter resItemAdapter = new ResItemAdapter(getApplicationContext(), userList);
        listview.setAdapter(resItemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String detail = userList.get(position).id+"|"+userList.get(position).proUrl+"|"+userList.get(position).name+"|Group";
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(RES_MESSAG, detail);
                startActivity(intent);
            }
        });

        Button buttonGroupN = (Button) rootView.findViewById(R.id.buttonGroupN);
        Button buttonGroupP = (Button) rootView.findViewById(R.id.buttonGroupP);

        if(pre == null){
            buttonGroupP.setClickable(false);
            buttonGroupP.setTextColor(Color.rgb(190,190,190));
        }else{
            buttonGroupP.setTextColor(Color.rgb(0,0,0));
            buttonGroupP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestGroupContent(pre);
//                Log.i(TAG, "onClick: "+"...");
                }
            });
        }

        if(next == null){
            buttonGroupN.setClickable(false);
            buttonGroupN.setTextColor(Color.rgb(190,190,190));
        }else{
            buttonGroupN.setTextColor(Color.rgb(0,0,0));
            buttonGroupN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestGroupContent(next);
//                Log.i(TAG, "onClick: "+"...");
                }
            });
        }

    }





}