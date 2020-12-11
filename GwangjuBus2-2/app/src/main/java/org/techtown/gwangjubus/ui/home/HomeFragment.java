package org.techtown.gwangjubus.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.gwangjubus.BusArriveAdapter;
import org.techtown.gwangjubus.BusArriveImf;
import org.techtown.gwangjubus.MainActivity;
import org.techtown.gwangjubus.OnBusArriveClickListener;
import org.techtown.gwangjubus.R;
import org.techtown.gwangjubus.action.PopupFragment;
import org.techtown.gwangjubus.ui.ZxingActivity;
import org.techtown.gwangjubus.ui.location.LocationFragment;
import org.techtown.gwangjubus.ui.location.LocationViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class HomeFragment extends Fragment {

    Context context;
    public static final String TAG = MainActivity.class.getSimpleName();
    String key = "bSeWawCaoDQIh9pHcqEVx3Q1BiCDyxhCdoJ4CiXqip2TY3zLfTxJSyjZTyZ%2BIFXmwPbFnkiokLjqo0EI0NDRyw%3D%3D";
    String busstopData;
    private HomeViewModel homeViewModel;
    IntentResult result;
    RecyclerView recyclerView;
    ArrayList<BusArriveImf> list = null;
    BusArriveImf bus = null;
    BusArriveAdapter adapter;

    private Socket socket;
    BufferedReader socket_in;
    PrintWriter socket_out;


    LocationFragment locationFragment = new LocationFragment();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    private DecoratedBarcodeView barcodeScannerView;

    TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getActivity().getBaseContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomeFragment.this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("Scan QR code");
        integrator.setBeepEnabled(false);

        integrator.setCaptureActivity(ZxingActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.initiateScan();

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        text = root.findViewById(R.id.text);


        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


        // qrFragment = new QRFragment();
        // ((MainActivity) getActivity()).replaceFragment(qrFragment);




        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (result != null) {
            if (result.getContents() == null) {
                System.out.println("Cancelled");
                Toast.makeText(getActivity(), "You cancelled the scanning!", Toast.LENGTH_LONG).show();
            } else {
                System.out.println("Worked: " + result.getContents());
                Toast.makeText(getActivity(), "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                text.setText(result.getContents());

                // ApiExplorer api = new ApiExplorer();

                BusArriveTask("2873");

                /*
                try {
                    busstopData = api.BusstopExlporer(result.getContents());
                    System.out.println(busstopData);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
        recyclerView.setAdapter(adapter);
        recyclerView.setClickable(true);
        adapter.setOnItemClicklistener(new OnBusArriveClickListener() {
            @Override
            public void onItemClick(BusArriveAdapter.MyViewHolder holder, View view, int position) {
                BusArriveImf item = adapter.getItem(position);
                ((MainActivity)getActivity()).busId = item.getBusId();
                ((MainActivity)getActivity()).lineId = item.getLineId();
                ((MainActivity)getActivity()).busstopName = item.getBusstopName();
                ((MainActivity)getActivity()).lineName = item.getBusName();
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
        builder.setTitle("승차 요청");
        builder.setMessage("승차하시겠습니까?");
        builder.setPositiveButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                        // 승차벨 이벤트!!!!!
                        String busid = ((MainActivity)getActivity()).busId;


                        Thread worker = new Thread(){
                            public void run(){
                                try {
                                    socket = new Socket("168.131.151.207", 4567);
                                    socket_out = new PrintWriter(socket.getOutputStream(), true);
                                    socket_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                    if (busid != null){
                                        socket_out.println(busid);
                                    }

                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                                try {
                                    while(true) {
                                        if (((MainActivity)getActivity()).busId != null){
                                            ((MainActivity)getActivity()).busId = socket_in.readLine();
                                        }

                                    }
                                } catch (Exception e){

                                }


                            }
                        };

                        worker.start();

                        ((MainActivity)getActivity()).replaceFragment(locationFragment);
                    }
                });
        builder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            if(socket != null) {
                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}