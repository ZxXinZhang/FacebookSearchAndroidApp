package com.example.xinzhang.myfbapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.xinzhang.myfbapplication.MainActivity.RES_MESSAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "home" ;
    View rootView;
//    EditText editText;
//    EditText  editKeyword;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        editText = (EditText) rootView.findViewById(R.id.editText);
//        editKeyword = (EditText) rootView.findViewById(R.id.keywordEditText);
//        System.out.println("!!!!!!!!"+editText);
//        System.out.println("~~~~~~~~~~`"+editKeyword);
        return rootView;
    }

//
//    public void getEditText(CallBack callBack) {
//                String msg = editKeyword.getText().toString();
//                callBack.getResult(msg);
//    }
//
//    public interface CallBack {
//        public void getResult(String result);
//    }

}
