package org.techtown.mysummaryapp.ui.paper;

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

public class PaperFragment extends Fragment {

    private PaperViewModel PaperViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PaperViewModel =
                new ViewModelProvider(this).get(PaperViewModel.class);
        View root = inflater.inflate(R.layout.fragment_paper, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        PaperViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}