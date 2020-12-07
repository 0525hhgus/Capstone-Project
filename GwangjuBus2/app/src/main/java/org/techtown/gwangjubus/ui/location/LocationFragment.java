package org.techtown.gwangjubus.ui.location;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.gwangjubus.BusArriveAdapter;
import org.techtown.gwangjubus.BusArriveImf;
import org.techtown.gwangjubus.BusLineAdapter;
import org.techtown.gwangjubus.MainActivity;
import org.techtown.gwangjubus.OnBusArriveClickListener;
import org.techtown.gwangjubus.OnBusLineClickListener;
import org.techtown.gwangjubus.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class LocationFragment extends Fragment {

    String key = "bSeWawCaoDQIh9pHcqEVx3Q1BiCDyxhCdoJ4CiXqip2TY3zLfTxJSyjZTyZ%2BIFXmwPbFnkiokLjqo0EI0NDRyw%3D%3D";
    private static final String TAG = "BusLocation";
    private LocationViewModel locationViewModel;
    Context context;
    ArrayList<String> list = null;
    String bus = null;
    BusLineAdapter adapter;

    TextView mybustext;
    RecyclerView recyclerView;

    NavController navController;

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity().getBaseContext();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //locationViewModel =
        //        new ViewModelProvider(this).get(LocationViewModel.class);

        // System.out.println("gwetdsf");
        View root = inflater.inflate(R.layout.fragment_location, container, false);


        mybustext = (TextView) root.findViewById(R.id.mybus_text);

        recyclerView = (RecyclerView) root.findViewById(R.id.line_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        String lineId = ((MainActivity)getActivity()).lineId;

        BusArriveTask(lineId);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void BusArriveTask(String search){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String lineId = null;

        try {
            lineId = URLEncoder.encode(search,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 버스 도착정보 목록 조회
        String url = "http://api.gwangju.go.kr/xml/lineStationInfo?serviceKey="+key+"&LINE_ID="+lineId+"";
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

    private void XMLtoJSONData(String xml) {


        list = new ArrayList<String>();

        // https://androidfreetutorial.wordpress.com/2016/11/28/how-to-convert-xml-to-json-for-android/
        XmlToJson xmlToJson = new XmlToJson.Builder(xml).build();
        // convert to a JSONObject
        JSONObject jsonObject = xmlToJson.toJson();
        Log.d(TAG, "jsonObject:" + jsonObject);

        int pos = 0;
        boolean posend = false;

        // JSON 에서 배열은 [] 대괄호 사용, Objext 는 {} 중괄호 사용
        try {
            JSONObject response = jsonObject.getJSONObject("ns2:BUSSTOP_INFO");
            JSONObject result = response.getJSONObject("RESULT");


            String resultCode = result.optString("RESULT_CODE");
            Log.d(TAG, "String resultCode :" + resultCode);

            if (resultCode.equals("SUCCESS")) {

                JSONObject arrive_list = response.getJSONObject("BUSSTOP_LIST");
                JSONArray array = arrive_list.getJSONArray("BUSSTOP");

                String busName = null;

                for (int i = 0; i < array.length() / 2 + 1; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String lineName = obj.optString("BUSSTOP_NAME"); // 첫번째 차량 번호
                    busName = obj.optString("LINE_NAME");

                    bus = lineName;

                    if(lineName.equals(((MainActivity)getActivity()).busstopName)){
                        posend = true;
                    } else if (!posend){
                        pos = pos + 1;
                    }

                    list.add(bus);

                }


                mybustext.setText(busName);



            } else if (resultCode.equals("ERROR")) {
                Toast.makeText(context, "시스템 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
            } else if (resultCode.equals("1")) {
                Toast.makeText(context, "결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            } else if (resultCode.equals("8")) {
                Toast.makeText(context, "요청 제한을 초과하였습니다", Toast.LENGTH_SHORT).show();
            } else if (resultCode.equals("23")) {
                Toast.makeText(context, "버스 도착 정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new BusLineAdapter(getActivity().getApplicationContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setClickable(true);

        recyclerView.scrollToPosition(pos);

        adapter.setOnItemClicklistener(new OnBusLineClickListener() {
            @Override
            public void onItemClick(BusLineAdapter.LineViewHolder holder, View view, int position) {
                String item = adapter.getItem(position);
                //((MainActivity)getActivity()).lineId = item.getLineId();
                System.out.println("아이템 선택 " + item);
            }
        });
    }
}