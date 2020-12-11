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
import org.techtown.gwangjubus.R;
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
    ArrayList<BusArriveImf> list = null;
    BusArriveImf bus = null;
    BusArriveAdapter adapter;
    String search_id; // 찾고자 하는 버스 id

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
        EditText hacha_bus_id = rootView.findViewById((R.id.hacha_bus_id));
        EditText hacha_station = rootView.findViewById((R.id.hacha_station));
        hacha_busid_text = hacha_bus_id.getText().toString();  //버스 아이디 텍스트 가져오기
        hacha_station_text = hacha_station.getText().toString(); //내릴 정류장 텍스트 가져오기

        // end

        search_id = hacha_busid_text;
        BusArriveTask(search_id);

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

    private void BusArriveTask(String search){

        RequestQueue requestQueue = Volley.newRequestQueue(context);



        String StationId = null; // 정류소 ID

        try {
            StationId = URLEncoder.encode(search,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 버스 도착정보 목록 조회
        String url = "http://api.gwangju.go.kr/xml/arriveInfo?serviceKey="+key+"&BUSSTOP_ID="+StationId+"";
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


        list = new ArrayList<BusArriveImf>();

        // https://androidfreetutorial.wordpress.com/2016/11/28/how-to-convert-xml-to-json-for-android/
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.d(TAG, "jsonObject:"+jsonObject);

        // JSON 에서 배열은 [] 대괄호 사용, Objext 는 {} 중괄호 사용
        try {
            JSONObject response = jsonObject.getJSONObject("ns2:ARRIVE_INFO");
            JSONObject result = response.getJSONObject("RESULT");



            String resultCode = result.optString("RESULT_CODE");
            Log.d(TAG, "String resultCode :"+resultCode);

            if(resultCode.equals("SUCCESS")){

                JSONObject arrive_list = response.getJSONObject("ARRIVE_LIST");
                JSONArray array = arrive_list.getJSONArray("ARRIVE");

                for(int i=0; i < array.length(); i++){
                    JSONObject obj = array.getJSONObject(i);
                    // optString which returns the value mapped by name if it exists
                    String busId =obj.optString("BUS_ID"); // 첫번째 차량 번호
                    String busName = obj.optString("LINE_NAME"); // 버스 이름
                    String lineId = obj.optString("LINE_ID");
                    String busArriveTime = obj.optString("REMAIN_MIN"); // 도착 예정 시간
                    String busstopName =obj.optString("BUSSTOP_NAME"); // 첫번째 차량 위치 정보
                    Log.d(TAG, "jString busId :"+busId);
                    Log.d(TAG, "jString busName :"+busName);
                    Log.d(TAG, "jString Lineid :"+lineId);
                    Log.d(TAG, "jString busArriveTime :"+busArriveTime);
                    Log.d(TAG, "jString busstopName :"+busstopName);

                    bus = new BusArriveImf(busId, busName, lineId, busArriveTime, busstopName);

                    list.add(bus);

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

        adapter = new BusArriveAdapter(getActivity().getApplicationContext(), list);
        hacha_search_recyclerview.setAdapter(adapter);
        hacha_search_recyclerview.setClickable(true);
        adapter.setOnItemClicklistener(new OnBusArriveClickListener() {
            @Override
            public void onItemClick(BusArriveAdapter.MyViewHolder holder, View view, int position) {
                BusArriveImf item = adapter.getItem(position);
                ((MainActivity)getActivity()).lineId = item.getLineId();
                ((MainActivity)getActivity()).busstopName = item.getBusstopName();
                System.out.println("아이템 선택 " + item.getBusName());
                show();
            }
        });
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
