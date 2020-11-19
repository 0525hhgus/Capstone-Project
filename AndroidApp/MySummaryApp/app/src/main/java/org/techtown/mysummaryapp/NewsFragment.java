package org.techtown.mysummaryapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import org.techtown.mysummaryapp.R;

public class NewsFragment extends Fragment {

    MainActivity activity;

    //onAttach로 Activity에 올라오면 fragment를 참조 할 수 있다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (MainActivity) getActivity();
    }


    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    //프래그먼트 안에 들어가는 최상위 레이아웃 생성
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView =
                (ViewGroup) inflater.inflate(R.layout.fragment_news,container,false);


        return rootView;

    }



}
