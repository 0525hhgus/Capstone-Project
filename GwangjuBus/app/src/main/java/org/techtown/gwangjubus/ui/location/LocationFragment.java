package org.techtown.gwangjubus.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.techtown.gwangjubus.MainActivity;
import org.techtown.gwangjubus.R;
import org.techtown.gwangjubus.ui.home.HomeFragment;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationViewModel =
                new ViewModelProvider(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alarm, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        locationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        return root;
    }
}