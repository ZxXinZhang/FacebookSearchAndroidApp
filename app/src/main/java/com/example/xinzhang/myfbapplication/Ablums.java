package com.example.xinzhang.myfbapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.xinzhang.myfbapplication.DetailsActivity.detailContent;


public class Ablums extends Fragment {
    private static final String TAG = "albums";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_ablums, container, false);
        Log.i(TAG, "albums............. ");
//        if(detailContent!=null){
//            Log.i(TAG, "albums not null ");
//            JSONObject detJson;
//            try {
//                detJson = new JSONObject(detailContent);
//                Log.i(TAG, "albums leng: "+ detJson.getString("albums"));
//                //JSONObject albumsObj = detJson.getJSONObject("albums");
//                //JSONArray albumsArr = albumsObj.getJSONArray("data");
//                //Log.i(TAG, "albums leng: "+ albumsArr.length());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else{
//            Log.i(TAG, "albums is null ");
//        }

        return rootView;
    }



}
