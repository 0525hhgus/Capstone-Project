package org.techtown.mysummaryapp.ui.magz;

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
import org.techtown.mysummaryapp.ui.news.NewsFragment;

public class MagzFragment extends Fragment {

    private MagzViewModel MagzViewModel;

    public static MagzFragment newInstance() {
        return new MagzFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MagzViewModel =
                new ViewModelProvider(this).get(MagzViewModel.class);
        View root = inflater.inflate(R.layout.fragment_magz, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        MagzViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}