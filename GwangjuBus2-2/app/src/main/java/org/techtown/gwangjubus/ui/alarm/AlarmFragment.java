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

import java.io.UnsupportedEncodingException;
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

    ArrayList<StationList> list = null;
    String station = null;

    String hacha_busid_text; //add
    String hacha_station_text; //add

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_alarm, container, false); //add

        hacha_search_recyclerview = (RecyclerView) rootView.findViewById(R.id.hacha_search_recyclerview); // fragment_alarm의 recycler view
        hacha_search_recyclerview.setHasFixedSize(true);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hacha_search_recyclerview.setLayoutManager(layoutManager);

        // add
        Button hacha_reservation_cancel_button = rootView.findViewById(R.id.hacha_reservation_cancel_button);
        Button hacha_reservation_button = rootView.findViewById(R.id.hacha_reservation_button);
        EditText hacha_station = rootView.findViewById((R.id.hacha_station));
        hacha_station_text = hacha_station.getText().toString(); //내릴 정류장 텍스트 가져오기

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

    private void StationSearchTask(){

        RequestQueue requestQueue = Volley.newRequestQueue(context);



        /*
        String StationId = null; // 정류소 ID

        try {
            StationId = URLEncoder.encode(search,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */

        // 버스 도착정보 목록 조회
        String url = "\thttp://api.gwangju.go.kr/xml/stationInfo?serviceKey="+key+"";
        Log.d(TAG, "URL:"+url);

        StringRequest request= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        XMLtoJSONData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue.add(request);
    }

    private void XMLtoJSONData(String xml){


        list = new ArrayList<StationList>();

        // https://androidfreetutorial.wordpress.com/2016/11/28/how-to-convert-xml-to-json-for-android/
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.d(TAG, "jsonObject:"+jsonObject);

        // JSON 에서 배열은 [] 대괄호 사용, Objext 는 {} 중괄호 사용
        try {
            JSONObject response = jsonObject.getJSONObject("ns2:STATION_INFO");
            JSONObject result = response.getJSONObject("RESULT");



            String resultCode = result.optString("RESULT_CODE");
            Log.d(TAG, "String resultCode :"+resultCode);

            if(resultCode.equals("SUCCESS")){

                JSONObject arrive_list = response.getJSONObject("STATION_LIST");
                JSONArray array = arrive_list.getJSONArray("STATION");

                for(int i=0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    // optString which returns the value mapped by name if it exists
                    String staionId =obj.optString("BUSSTOP_ID"); // 첫번째 차량 번호
                    String stationName = obj.optString("BUSSTOP_NAME"); // 버스 이름
                    Log.d(TAG, "jString busId :"+staionId);
                    Log.d(TAG, "jString busName :"+stationName);
                    station = new StationList(staionId, stationName);

                    if(search.equals(stationName)){
                        list.add(station);
                    }


                }


            } else if(resultCode.equals("ERROR")){
                Toast.makeText(context, "시스템 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
            } else if(resultCode.equals("1")){
                Toast.makeText(context, "결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            } else if(resultCode.equals("8")){
                Toast.makeText(context, "요청 제한을 초과하였습니다", Toast.LENGTH_SHORT).show();
            } else if(resultCode.equals("23")){
                Toast.makeText(context, "버스 도착 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                    }
                });
        builder.show();
    }


}
