package org.techtown.gwangjubus.ui.alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.gwangjubus.BusArriveAdapter;
import org.techtown.gwangjubus.BusArriveImf;
import org.techtown.gwangjubus.MainActivity;
import org.techtown.gwangjubus.OnBusArriveClickListener;
import org.techtown.gwangjubus.OnStationClickListener;
import org.techtown.gwangjubus.R;
import org.techtown.gwangjubus.StationAdapter;
import org.techtown.gwangjubus.StationList;
import org.techtown.gwangjubus.ui.home.HomeViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class AlarmFragment extends Fragment  {

    Context context;
    public static final String TAG = MainActivity.class.getSimpleName();
    String key = "bSeWawCaoDQIh9pHcqEVx3Q1BiCDyxhCdoJ4CiXqip2TY3zLfTxJSyjZTyZ%2BIFXmwPbFnkiokLjqo0EI0NDRyw%3D%3D";
    String busstopData;
    private HomeViewModel homeViewModel;
    IntentResult result;
    RecyclerView hacha_search_recyclerview;
    BusArriveImf bus = null;
    BusArriveAdapter adapter;
    String search_id; // 찾고자 하는 버스 id
    TextView hachaBusname;
    TextView busCurrentStation;
    TextView hachaBusstop;

    ArrayList<StationList> list = null;
    String station = null;

    String hacha_busid_text; //add
    String hacha_station_text; //add
    EditText hacha_station;

    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_alarm, container, false); //add




        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        hachaBusname = (TextView) rootView.findViewById(R.id.hacha_busName);
        hachaBusname.setText(((MainActivity)getActivity()).lineName);
        busCurrentStation = (TextView) rootView.findViewById(R.id.bus_current_station);
        busCurrentStation.setText(((MainActivity)getActivity()).busstopName);
        hachaBusstop = (TextView)  rootView.findViewById(R.id.hacha_busstop);

        // add
        Button hacha_reservation_cancel_button = rootView.findViewById(R.id.hacha_reservation_cancel_button);
        Button hacha_reservation_button = rootView.findViewById(R.id.hacha_reservation_button);
        hacha_station = (EditText) rootView.findViewById((R.id.hacha_station));


        // end

        search_id = ((MainActivity)getActivity()).busId;

        hacha_reservation_cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                show2(); //하차 취소 여부 묻는 창

                //  StationSearchTask();
                //취소 되면 예약창 사라지게 하기
            }
        });

        hacha_reservation_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                show(); //하차 예약 여부 묻는 창
                // 하차예약되면 밑에 창 보이기
            }
        });

        return rootView;
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home, fragment);
        fragmentTransaction.commit();
    }

    void show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("하차 예약");
        builder.setMessage("하차 예약 하시겠습니까?");
        builder.setPositiveButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"하차가 예약 되었습니다..",Toast.LENGTH_LONG).show();
                        hacha_station_text = hacha_station.getText().toString(); //내릴 정류장 텍스트 가져오기
                        hachaBusstop.setText(hacha_station_text);

                        String busid = ((MainActivity)getActivity()).busId;

                        Thread worker = new Thread(){
                            public void run(){
                                try {
                                    socket = new Socket("168.131.151.207", 8911);
                                    socket_out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                                    if (busid != null && hacha_station_text != null){
                                        socket_out.println(busid);
                                        socket_out.println(hacha_station_text);
                                    }

                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                                try {

                                    while(true) {
                                        ((MainActivity)getActivity()).busId = socket_in.readLine();
                                        hacha_station_text = socket_in.readLine();
                                    }

                                } catch (Exception e){

                                }


                            }
                        };

                        worker.start();

                    }
                });

        builder.show();
    }

    void show2()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("하차 취소");
        builder.setMessage("하차를 취소 하시겠습니까?");
        builder.setPositiveButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"하차가 취소 되었습니다..",Toast.LENGTH_LONG).show();
                        hachaBusstop.setText(null);
                    }
                });
        builder.show();
    }


}
