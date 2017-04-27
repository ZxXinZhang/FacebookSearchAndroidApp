package com.example.xinzhang.myfbapplication;

/**
 * Created by xinzhang on 4/14/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONObject;

import static com.example.xinzhang.myfbapplication.MainActivity.pageListf;
import static com.example.xinzhang.myfbapplication.MainActivity.userListf;
import static com.example.xinzhang.myfbapplication.resultActivity.RES_MESSAG;
import static com.facebook.FacebookSdk.getApplicationContext;

// implements AdapterView.OnItemClickListener
public class FavPage extends Fragment {
    private static final String TAG = "userf";
    private String pre;
    private String next;
//    List<ResultRow> userListf = new ArrayList<>();
    View rootView;
    JSONObject userObj = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.favpage, container, false);
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
        ListView listview = (ListView)rootView.findViewById(R.id.listviewPagef);
        ResItemAdapter resItemAdapter = new ResItemAdapter(getApplicationContext(), pageListf);
        listview.setAdapter(resItemAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String detail = pageListf.get(position).id+"|"+pageListf.get(position).proUrl+"|"+pageListf.get(position).name+"|Page";
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(RES_MESSAG, detail);
                startActivity(intent);
            }
        });
    }



}
