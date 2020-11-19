package org.techtown.mysummaryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class HomeFragment extends Fragment {

    MainActivity activity;
    NewsFragment newsFragment;
    Button newsButton;

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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        // rootview가 플래그먼트 화면으로 보이게 된다. 부분화면을 보여주고자하는 틀로 생각하면 된다.
        // fragment_main 파일과 MainFragment와 연결 해준다.
        // 인플레이션 과정을 통해서 받을 수 있다.
        newsFragment = new NewsFragment();

        newsButton = (Button) rootview.findViewById(R.id.newsButton);
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.onFragmentChange(1);

            }
        });

        //getActivity();      // 액티비티위에 올라가게 한다.


        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }


}
