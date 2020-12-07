package org.techtown.gwangjubus.ui.setting;

import android.content.Intent;
import android.net.Uri;
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

import org.techtown.gwangjubus.R;

public class SettingFragment extends Fragment {

    private SettingViewModel settingViewModel;
    private long contentView;
    private Object setContentView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        Button setting_button1 = rootView.findViewById(R.id.setting_button1);
        Button setting_button2 = rootView.findViewById(R.id.setting_button2);
        Button setting_button3 = rootView.findViewById(R.id.setting_button3);
        Button setting_button4 = rootView.findViewById(R.id.setting_button4);

        setting_button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

        setting_button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(("https://baroeungdap.gwangju.go.kr/")));
                startActivity(myIntent);
            }
        });

        setting_button3.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v){
                                           Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(("tel:062-2222-222")));
                                           startActivity(myIntent);
                                       }
                                   }
        );

        setting_button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){


            }
        });

        return rootView;
    }
}