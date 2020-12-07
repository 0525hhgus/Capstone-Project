package org.techtown.gwangjubus.ui.busstop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class BusstopFragment extends Fragment {

    Context context;
    public static final String TAG = "BusstopSearch";
    String key = "bSeWawCaoDQIh9pHcqEVx3Q1BiCDyxhCdoJ4CiXqip2TY3zLfTxJSyjZTyZ%2BIFXmwPbFnkiokLjqo0EI0NDRyw%3D%3D";
    String stationData;
    IntentResult result;
    RecyclerView stationRecylerView;
    ArrayList<StationList> list = null;
    StationList station = null;
    StationAdapter adapter;
    EditText searchbusstoptext;
    Button searchButton;
    String search;

    StationFragment stationFragment = new StationFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_busstop, container, false);


        searchButton = (Button) root.findViewById(R.id.searchbusstopbutton);

        searchbusstoptext = (EditText) root.findViewById(R.id.searchbusstoptext);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {

                search = searchbusstoptext.getText().toString();
                StationSearchTask();
            }
        });

        stationRecylerView = (RecyclerView) root.findViewById(R.id.busstopsearchrecyclerview);
        stationRecylerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stationRecylerView.setLayoutManager(layoutManager);

        return root;
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

        //System.out.println(adapter.getItem());
        adapter = new StationAdapter(getActivity().getApplicationContext(), list);
        stationRecylerView.setAdapter(adapter);
        //stationRecylerView.scrollToPosition(list.indexOf(search));
        stationRecylerView.setClickable(true);

        adapter.setOnItemClicklistener(new OnStationClickListener() {
            @Override
            public void onItemClick(StationAdapter.StationViewHolder holder, View view, int position) {
                StationList item = adapter.getItem(position);
                System.out.println("아이템 선택 " + item.getBusstopName());
                ((MainActivity)getActivity()).busstopId = item.getBusstopId();
                replaceFragment(stationFragment);
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_busstop, fragment);
        fragmentTransaction.commit();
    }



}
