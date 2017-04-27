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
import static com.example.xinzhang.myfbapplication.resultActivity.resEventContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resPlaceContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resUserContent;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by xinzhang on 4/14/17.
 */

public class resPlace extends Fragment {
    private static final String TAG = "place";
    List<ResultRow> userList = new ArrayList<>();
    View rootView;
    private String pre;
    private String next;
    JSONObject userObj = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.resplace, container, false);

        if(resContent!=null){
           displayUser();
        }


        return rootView;
    }

    public void requestPageContent(String url){
        resPlace.DownloadWebPage task = new resPlace.DownloadWebPage();
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
            resPlaceContent = result;
            displayUser();
        }
    }


    public void displayUser(){
        try {
            Log.i(TAG, "event json ");
            userObj = new JSONObject(resPlaceContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray userResArray;
        try {
            Log.i(TAG, "event array ");
            userResArray  = userObj.getJSONArray("data");
            userList.clear();
            for(int i = 0; i < userResArray.length(); i++){
                JSONObject oneUser = userResArray.getJSONObject(i);
                String id = oneUser.getString("id");
                String name = oneUser.getString("name");
                String proUrl = oneUser.getJSONObject("picture").getJSONObject("data").getString("url");
                userList.add(new ResultRow(id,proUrl,name,"Place"));

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
                Log.i(TAG, "pre page: "+pre);
                Log.i(TAG, "next page: "+next);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listview = (ListView)rootView.findViewById(R.id.listviewPlace);
        ResItemAdapter resItemAdapter = new ResItemAdapter(getApplicationContext(), userList);
        listview.setAdapter(resItemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String detail = userList.get(position).id+"|"+userList.get(position).proUrl+"|"+userList.get(position).name+"|Place";
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(RES_MESSAG, detail);
                startActivity(intent);
            }
        });


        Button buttonPlaceN = (Button) rootView.findViewById(R.id.buttonPlaceN);
        Button buttonPlaceP = (Button) rootView.findViewById(R.id.buttonPlaceP);

        if(pre == null){
            buttonPlaceP.setClickable(false);
            buttonPlaceP.setTextColor(Color.rgb(190,190,190));
        }else{
            buttonPlaceP.setTextColor(Color.rgb(0,0,0));
            buttonPlaceP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPageContent(pre);
//                Log.i(TAG, "onClick: "+"...");
                }
            });
        }

        if(next == null){
            buttonPlaceN.setClickable(false);
            buttonPlaceN.setTextColor(Color.rgb(190,190,190));
        }else{
            buttonPlaceN.setTextColor(Color.rgb(0,0,0));
            buttonPlaceN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPageContent(next);
//                Log.i(TAG, "onClick: "+"...");
                }
            });
        }


    }

}