package org.techtown.gwangjubus.ui;

import com.journeyapps.barcodescanner.CaptureActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.gwangjubus.R;

public class ZxingActivity extends CaptureActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView title_view = new TextView(this);
        title_view.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
        title_view.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        title_view.setPadding(150, 100, 100, 100);
        title_view.setTextColor(Color.parseColor("#FFFFFF"));
        title_view.setTextSize(30);
        title_view.setText("QR 코드 입력화면");

        this.addContentView(title_view, layoutParams);

    }
}