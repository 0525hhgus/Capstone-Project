package org.techtown.gwangjubus.action;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import org.techtown.gwangjubus.MainActivity;
import org.techtown.gwangjubus.R;
import org.techtown.gwangjubus.ui.home.HomeFragment;
import org.techtown.gwangjubus.ui.location.LocationFragment;

import kotlin.ReplaceWith;

public class PopupFragment extends Fragment {

    Button okButton, cancleButton;
    LocationFragment locationFragment = new LocationFragment();

    public static PopupFragment newInstance() {
        return new PopupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.geton_dialog, container, false);

        // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().setContentView(R.layout.geton_dialog);

        okButton = (Button) getActivity().findViewById(R.id.okButton);
        cancleButton = (Button) getActivity().findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(locationFragment);
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.popup_fragment, fragment);
        fragmentTransaction.commit();
    }
}