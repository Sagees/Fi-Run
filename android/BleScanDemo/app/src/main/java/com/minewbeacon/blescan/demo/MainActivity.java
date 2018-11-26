package com.minewbeacon.blescan.demo;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minew.beacon.BeaconValueIndex;
import com.minew.beacon.BluetoothState;
import com.minew.beacon.MinewBeacon;
import com.minew.beacon.MinewBeaconManager;
import com.minew.beacon.MinewBeaconManagerListener;
import com.yuliwuli.blescan.demo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.lang.String;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private MinewBeaconManager mMinewBeaconManager;
    private RecyclerView mRecycle;
    private BeaconListAdapter mAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private boolean isScanning;

    UserRssi comp = new UserRssi();
    private TextView mStart_scan;
    private boolean mIsRefreshing;
    private int state;


    public String return_msg = "";


    // 내 변수
    private String str;
    private MinewBeacon ooMinewBeacon;

    // 그림그리기 배열 받아오기 핸들러
    private Handler myHandler;

    // 임시 변수
    private String tmp = null;

    private String tmp02 = null;


    // 받기 합쳤다.
    private TextView output;

    public NewHandler handler01;


    // 그림그리기
    EditText et;
    drawmap map;
    static int i=5,j=5;
//    static int[][] lmap = {{0,0,3,0,0},{0,0,0,4,0},{0,0,1,4,1},{3,0,1,0,4},{0,0,1,0,2}};
//    static int[][] lmap = arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        output = (TextView) findViewById(R.id.output);

        // drawmap
        map = (drawmap)findViewById(R.id.painter);
        map.setupHandler();

        // 그림용 배열 받아오는거 핸들러
        myHandler = map.mHandler;




        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int dw = size. x/i;
        int dh = size. y/j;
        Log. e("Width", "" + dw);
        Log. e("height", "" + dh);
        if(dw < dh)map.setUnit(dw);
        else map.setUnit(dh);

        // drawmap 끝





        // 소켓 받아오는거 핸들러 선언
        handler01 = new NewHandler();

        initView();
        initManager();
        checkBluetooth();
        initListener();

    }



/*
        // 배열로 만들어주는거 처리 핸들러
    private class mHandler extends Handler {
        @Override

        public void handleMessage(Message msg){

            if(msg.what == 1){
                Log.d("handler : ", "ok");
                int[][] lmap = (int[][]) msg.obj;
//                int[][] temp01 = (int[][]) msg.obj;


            }else {
                Log.d("handler : ", "error");
            }
        }
    }

*/


    // setText 핸들러
    private class NewHandler extends Handler {
        @Override

        public void handleMessage(Message msg){

            if(msg.what == 1){
                Log.d("handler : ", "ok");
                // Toast.makeText(MainActivity.this, "msg:" + msg.obj, Toast.LENGTH_SHORT).show();
                // socket1.sendString( (String)msg.obj );
//                output.setText("완료!");
                output.setText((String)msg.obj);
                //socket1.recvString();

            }else {
                Log.d("handler : ", "error");
            }
        }
    }

    class TCPclient implements Runnable{

        // 이 핸들러 변수 선언이 문제...
//    public NewHandler handler01;

        public String return_msg = "";


//        private static final String serverIP = "203.252.34.46";

        //private static final String serverIP = "203.252.34.43";
        private static final String serverIP = "203.252.34.112";
        private static final int serverPort = 4000;
        private Socket inetSocket = null;
        private String msg;

        public TCPclient(String _msg){
            this.msg = _msg;
        }

        public void run(){
            try{
                Log.d("확인","Connecting...");

                inetSocket = new Socket(serverIP, serverPort);
//                inetSocket.connect(socketAddr);

                try{
                    Log.d("@@1보낸메시지 확인= ",msg);
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(inetSocket.getOutputStream())
                    ), true);

                    out.println(msg);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(inetSocket.getInputStream())
                    );

                    // 겹쳐서 잠시 주석
                    // read line 1
//                    return_msg = in.readLine();

//                    Log.d("확인","다시받은 메시지 = "+return_msg);


                    String receiveString;


                    // 배열 전환 부분
//                    while(true)
//                    {
                        // 한 줄 씩 읽는다.
                        // read line 2
                        receiveString = in.readLine();
                    Log.d("여기 오나? ","확인1");
                    Log.d("확인","receiveString = "+receiveString);

                    // 스페이스바로 나눈다.

                    String[] integerStrings = receiveString.split(" ");

//                    String[] str2 = integerStrings[2].split("");

//                    String str3 = integerStrings[2];
//                    String[] str2 = str3.split("");


                        Log.d("여기 오나? ","확인2");
//                    Log.d("split string 문자열 확인",String.valueOf(str2));
                    Log.d("길이 확인",String.valueOf(integerStrings.length));
                    Log.d("길이 확인",String.valueOf(integerStrings[2]));


                    int I=Integer.parseInt(integerStrings[0]);
                    int J=Integer.parseInt(integerStrings[1]);

                    map.setupDrawmap(I,J);

                    Log.d("@@N@@확인 ",String.valueOf(I));
                    Log.d("@@M@@확인 ",String.valueOf(J));


                    int[][] arr = new int[I][J];
                    for(int i=0;i<I;i++){
                        for(int j=0;j<J;j++){
                            Log.d("여기 오나? ","확인3");
//                            arr[i][j] = Integer.parseInt(integerStrings[2].substring(i*I+j));
//                            arr[i][j] = Integer.parseInt(integerStrings[2].substring(i*I+j,i*I+j+1));
//                            arr[i][j] = Integer.parseInt(String.valueOf(integerStrings[2].charAt(i*I+j)));
//                            arr[i][j] = Character.getNumericValue(String.valueOf(integerStrings[2].charAt(i*I+j)));
                            arr[i][j] = Character.getNumericValue(integerStrings[2].charAt(I*i+j));
//                            arr[i][j] = Integer.parseInt(str2[i*I+j]);
                            Log.d("배열 출력 확인",String.valueOf(arr[i][j]));



                            // 핸들러를 사용해서 배열을 선언해주자.
                            Message message = Message.obtain();
                            message.what = 1;

                            message.obj = arr;
                            myHandler.sendMessage(message);



                        }
                    }
                    Log.d("배열 출력 확인 [0][0]",String.valueOf(arr[0][0]));


                    // handler 부분
                    // setText 하기 위해 필요하다.
                    Message message = Message.obtain();
                    message.what = 1;
//                            message.obj = str;


//                    message.obj = return_msg;
                    message.obj = receiveString;

                    handler01.sendMessage(message);


//                    setT();
                }
                catch (Exception e){
                    Log.e("에러1","C:",e);

                }
                finally{
//                    output.setText(return_msg);
                    inetSocket.close();
                }

            }
            catch(Exception e){
                Log.e("에러2","에러2",e);
            }
        }

    }

    // check Bluetooth state
    private void checkBluetooth() {
        BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
        switch (bluetoothState) {
            case BluetoothStateNotSupported:
                Toast.makeText(this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BluetoothStatePowerOff:
                showBLEDialog();
                break;
            case BluetoothStatePowerOn:
                break;
        }
    }


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStart_scan = (TextView) findViewById(R.id.start_scan);

        mRecycle = (RecyclerView) findViewById(R.id.recyeler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycle.setLayoutManager(layoutManager);
        mAdapter = new BeaconListAdapter();
        mRecycle.setAdapter(mAdapter);
        mRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager
                .HORIZONTAL));
    }

    private void initManager() {
        mMinewBeaconManager = MinewBeaconManager.getInstance(this);
    }


    private void initListener() {
        mStart_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMinewBeaconManager != null) {
                    BluetoothState bluetoothState = mMinewBeaconManager.checkBluetoothState();
                    switch (bluetoothState) {
                        case BluetoothStateNotSupported:
                            Toast.makeText(MainActivity.this, "Not Support BLE", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        case BluetoothStatePowerOff:
                            showBLEDialog();
                            return;
                        case BluetoothStatePowerOn:
                            break;
                    }
                }
                if (isScanning) {
                    isScanning = false;
                    mStart_scan.setText("Start");
                    if (mMinewBeaconManager != null) {
                        mMinewBeaconManager.stopScan();
                    }
                } else {
                    isScanning = true;
                    mStart_scan.setText("Stop");
                    try {
                        mMinewBeaconManager.startScan();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                state = newState;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mMinewBeaconManager.setDeviceManagerDelegateListener(new MinewBeaconManagerListener() {
            /**
             *   if the manager find some new beacon, it will call back this method.
             *
             *  @param minewBeacons  new beacons the manager scanned
             */
            @Override
            public void onAppearBeacons(List<MinewBeacon> minewBeacons) {

            }

            /**
             *  if a beacon didn't update data in 10 seconds, we think this beacon is out of rang, the manager will call back this method.
             *
             *  @param minewBeacons beacons out of range
             */
            @Override
            public void onDisappearBeacons(List<MinewBeacon> minewBeacons) {
                /*for (MinewBeacon minewBeacon : minewBeacons) {
                    String deviceName = minewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                    Toast.makeText(getApplicationContext(), deviceName + "  out range", Toast.LENGTH_SHORT).show();
                }*/
            }

            /**
             *  the manager calls back this method every 1 seconds, you can get all scanned beacons.
             *  @param minewBeacons all scanned beacons
             */
            @Override
            public void onRangeBeacons(final List<MinewBeacon> minewBeacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(minewBeacons, comp);
                        Log.e("tag", state + "");
                        if (state == 1 || state == 2) {
                        } else {
                            mAdapter.setItems(minewBeacons);
                        }

                        // 내가 넣은 코드

                        if (minewBeacons.size() == 0) {

                        } else {
                            ooMinewBeacon = minewBeacons.get(0);
                            str = ooMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                            tmp = str;

                            if (tmp.equals(str)) {

                            } else {
                                tmp = str;
//                                str = tmp;
                            }


                                tmp02 = "s " + str;

//                                 mMinewBeaconSetting.getDeviceId()
                                Log.d("@@str@@", String.valueOf(str));
                                Log.d("@@tmp02@@", String.valueOf(tmp02));
                                Log.d("그냥 비콘 가져오면 ", String.valueOf(ooMinewBeacon));


                            TCPclient tcpThread = new TCPclient(tmp02);
                            Thread thread = new Thread(tcpThread);
                            thread.start();

                                /*
                                Message message = Message.obtain();
                                message.what = 1;
                            message.obj = str;
                                message.obj = tmp02;
                                myHandler.sendMessage(message);
                                */

                            }
//                        }

                    }
                        /*
                        for (int i = 0; i < minewBeacons.size(); i++) {
                            ooMinewBeacon = minewBeacons.get(i);
//                            str = ooMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                            str = ooMinewBeacon.getBeaconValue(BeaconValueIndex.MinewBeaconValueIndex_Name).getStringValue();
                            // mMinewBeaconSetting.getDeviceId()
                            Log.d("@@NAME@@", String.valueOf(str));
                            Log.d("순서확인", String.valueOf(ooMinewBeacon));
                        }
                        */

//                    }
                });
                }


            /**
             *  the manager calls back this method when BluetoothStateChanged.
             *
             *  @param state BluetoothState
             */
            @Override
            public void onUpdateState(BluetoothState state) {
                switch (state) {
                    case BluetoothStatePowerOn:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOn", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothStatePowerOff:
                        Toast.makeText(getApplicationContext(), "BluetoothStatePowerOff", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop scan
        if (isScanning) {
            mMinewBeaconManager.stopScan();
        }
    }

    private void showBLEDialog() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                break;
        }
    }
}
