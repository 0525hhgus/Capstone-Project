package org.techtown.mysummaryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.mysummaryapp.MainActivity;
import org.techtown.mysummaryapp.R;
import org.techtown.mysummaryapp.ui.judm.JudmFragment;
import org.techtown.mysummaryapp.ui.magz.MagzFragment;
import org.techtown.mysummaryapp.ui.news.NewsFragment;
import org.techtown.mysummaryapp.ui.paper.PaperFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    Button newsButton, paperButton, judgmButton, magzButton;
    Fragment currentFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        newsButton = (Button) root.findViewById(R.id.newsButton);
        paperButton = (Button) root.findViewById(R.id.paperButton);
        judgmButton = (Button) root.findViewById(R.id.judgmButton);
        magzButton = (Button) root.findViewById(R.id.magzButton);

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity)getActivity()).replaceFragment(NewsFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });


        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    //첫번째 버튼 행동
                    case R.id.newsButton:
                        currentFragment = MainActivity.manager.findFragmentById(R.id.fragment_home);
                        // 이동버튼 클릭할 때 stack에 push
                        MainActivity.fragmentStack.push(currentFragment);
                        ((MainActivity)getActivity()).replaceFragment(NewsFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                        break;
                    //두번째 버튼 행동
                    case R.id.paperButton:
                        currentFragment = MainActivity.manager.findFragmentById(R.id.fragment_home);
                        // 이동버튼 클릭할 때 stack에 push
                        MainActivity.fragmentStack.push(currentFragment);
                        ((MainActivity)getActivity()).replaceFragment(PaperFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                        break;
                    case R.id.judgmButton:
                        currentFragment = MainActivity.manager.findFragmentById(R.id.fragment_home);
                        ((MainActivity)getActivity()).replaceFragment(JudmFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                        break;
                    case R.id.magzButton:
                        currentFragment = MainActivity.manager.findFragmentById(R.id.fragment_home);
                        ((MainActivity)getActivity()).replaceFragment(MagzFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                        break;
                }
            }
        };

        newsButton.setOnClickListener(onClickListener);
        paperButton.setOnClickListener(onClickListener);
        judgmButton.setOnClickListener(onClickListener);
        magzButton.setOnClickListener(onClickListener);

        return root;

    }
}