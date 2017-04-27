package com.example.xinzhang.myfbapplication;

/**
 * Created by xinzhang on 4/14/17.
 */

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.xinzhang.myfbapplication.MainActivity.userListf;
import static com.example.xinzhang.myfbapplication.resultActivity.RES_MESSAG;
import static com.example.xinzhang.myfbapplication.resultActivity.resContent;
import static com.example.xinzhang.myfbapplication.resultActivity.resUserContent;
import static com.facebook.FacebookSdk.getApplicationContext;

// implements AdapterView.OnItemClickListener
public class FavUser extends Fragment {
    private static final String TAG = "userf";
    private String pre;
    private String next;
//    List<ResultRow> userListf = new ArrayList<>();
    View rootView;
    JSONObject userObj = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favuser, container, false);
//        Log.i(TAG, "length of list: " + userListf.szie());

        displayUserf();

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i(TAG, "onResuem: "+"execute");
        displayUserf();


    }


    public void displayUserf(){
        ListView listview = (ListView)rootView.findViewById(R.id.listviewUserf);
        ResItemAdapter resItemAdapter = new ResItemAdapter(getApplicationContext(), userListf);
        listview.setAdapter(resItemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String detail = userListf.get(position).id+"|"+userListf.get(position).proUrl+"|"+userListf.get(position).name+"|User";
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(RES_MESSAG, detail);
                startActivity(intent);
            }
        });
    }



}
