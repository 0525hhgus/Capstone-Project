package org.techtown.gwangjubus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.techtown.gwangjubus.ui.home.HomeFragment;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static String busId;
    public static String lineId;
    public static String busstopName;
    public static String busstopId;
    private AppBarConfiguration mAppBarConfiguration;
    public static FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);


        // manager = getSupportFragmentManager();
        // FragmentTransaction fragmentTransaction = manager.beginTransaction();

        FloatingActionButton homeButton = (FloatingActionButton) findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                //replaceFragment(HomeFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                // Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                // addPreferencesFromResource(R.layout.alarm_setting);
                // fragmentTransaction.replace(R.id.fragment_home, HomeFragment.newInstance()).commit();
            }

        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_busstop, R.id.navigation_location, R.id.navigation_home, R.id.navigation_alarm, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*
        new IntentIntegrator(this).initiateScan();

        qrScan.setCaptureActivity(CustomScannerActivity.class);
        qrScan.setOrientationLocked(false); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("Sample Text!");
        qrScan.initiateScan();
        // manager.beginTransaction().replace(R.id.fragment_home, HomeFragment.newInstance()).commit();

         */

    }
/*
    protected void onResume(){
        super.onResume();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();
    }
*/
    /*

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
    */
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home, fragment);
        fragmentTransaction.commit();
}
}

