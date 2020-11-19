package org.techtown.mysummaryapp.ui.repo;

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

public class RepoFragment extends Fragment {

    private RepoViewModel repoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        repoViewModel =
                new ViewModelProvider(this).get(RepoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        // final TextView textView = root.findViewById(R.id.text_slideshow);
        repoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });
        return root;
    }
}