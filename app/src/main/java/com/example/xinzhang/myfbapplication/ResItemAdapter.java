package com.example.xinzhang.myfbapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ResItemAdapter extends ArrayAdapter<ResultRow> {
    private static final String TAG = "adap";

    private Context context;

    List<ResultRow> mDataItems;
    LayoutInflater mInflater;
    public ResItemAdapter(Context context, List<ResultRow> objects) {
        super(context, R.layout.list_item, objects);
        Log.i(TAG, "ResItemAdapter: " + objects.size());
        mDataItems = objects;
        mInflater = LayoutInflater.from(context);
        this.context = context;

    }


    @Override
    public @NonNull View getView(int position, @Nullable View convertView,
                                 @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item,parent,false);
        }
        ImageView imgView = (ImageView) convertView.findViewById(R.id.imageViewResPro);
        ImageView imgFav = (ImageView) convertView.findViewById(R.id.imageViewResFav);
        ImageView imgDet = (ImageView) convertView.findViewById(R.id.imageViewResDet);
        TextView tvName = (TextView) convertView.findViewById(R.id.itemNameText);
        String name = mDataItems.get(position).getName();
        String proUrl = mDataItems.get(position).getProUrl();
        String type = mDataItems.get(position).getType();
        String id = mDataItems.get(position).getId();
        String key = id+"|"+type;
        tvName.setText(name);
        Picasso.with(getApplicationContext()).load(proUrl).into(imgView);
        SharedPreferences myPreference=context.getSharedPreferences("myPreference", Context.MODE_PRIVATE);
        if(myPreference.contains(key)){
            imgFav.setImageResource(R.drawable.favorites_on);
        }else{
            imgFav.setImageResource(R.drawable.favorites_off);
        }
        imgDet.setImageResource(R.drawable.details);
        return convertView;
    }

}
