package org.techtown.mysummaryapp.ui.judm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.mysummaryapp.R;
import org.techtown.mysummaryapp.ui.magz.MagzViewModel;
import org.techtown.mysummaryapp.ui.news.NewsFragment;

public class JudmFragment extends Fragment {

    private JudmViewModel JudmViewModel;

    public static JudmFragment newInstance() {
        return new JudmFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JudmViewModel =
                new ViewModelProvider(this).get(JudmViewModel.class);
        View root = inflater.inflate(R.layout.fragment_judm, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        JudmViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}